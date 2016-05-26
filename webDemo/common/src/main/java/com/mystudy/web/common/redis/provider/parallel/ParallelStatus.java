package com.mystudy.web.common.redis.provider.parallel;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.config.RedisConnection;
import org.slf4j.Logger;


public class ParallelStatus {

	private final long silenceTime;
	private final ArrayBlockingQueue<ParallelJedis> jedisQueue;
	private final Lock lock = new ReentrantLock();
	private final RedisConnection redisConnection;
	// only used for log
	private int count;
	private volatile long wakeTime;
	private volatile int version = 0;
	private boolean needCheck = false;

	public ParallelStatus(RedisConnection connection, int capacity,
			int initNum, long silenceTime) {
		this.silenceTime = silenceTime > 0 ? silenceTime : 30000;
		this.redisConnection = connection;
		jedisQueue = new ArrayBlockingQueue<ParallelJedis>(capacity);
		for (int i = 0; i < initNum; i++) {
			ParallelJedis jedis = new ParallelJedis(this);
			jedis.setVersion(this.version);
			if (jedisQueue.offer(jedis)) {
				count++;
			} else {
				break;
			}
		}
		needCheck = true;
	}

	public boolean checkStatus() {
		return checkStatus(System.currentTimeMillis());
	}

	private boolean checkStatus(long now) {
		return wakeTime <= now;
	}

	/**
	 * silence and take off all access
	 * 
	 * @param version
	 * @param timeout
	 * @return
	 */
	public int silence(int version, long timeout) {
		long wt = System.currentTimeMillis() + timeout;
		// check before lock
		if (this.version == version) {
			this.wakeTime = wt;
			List<ParallelJedis> brokenJedises = null;
			final Lock lock = this.lock;
			lock.lock();
			try {
				if (this.version == version) {
					this.version++;
					brokenJedises = removeAll();
					int clear = count;
					count = 0;
					needCheck = true;
					return clear;
				}
			} finally {
				lock.unlock();
				// after unlock
				disconnectAll(brokenJedises);
			}
		}
		return 0;
	}

	private List<ParallelJedis> removeAll() {
		List<ParallelJedis> brokenJedises = new ArrayList<ParallelJedis>(
				jedisQueue.size());
		int size = jedisQueue.drainTo(brokenJedises);
		return size > 0 ? brokenJedises : null;
	}

	private void disconnectAll(List<ParallelJedis> brokenJedises) {
		if (brokenJedises != null) {
			final Logger logger = LogUtil.getCommonLogger();

			for (ParallelJedis jedis : brokenJedises) {
				try {
					jedis.disconnect();
				} catch (Exception e) {
					logger.error("error in disconnect " + redisConnection, e);
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("disconnect all:" + this.redisConnection
						+ " count:" + brokenJedises.size());
			}
		}
	}

	public ParallelJedis borrow(long waitOnce, TimeUnit timeUnit) {
		long now = System.currentTimeMillis();
		if (checkStatus(now)) {
			ParallelJedis jedis = null;
			try {
				jedis = jedisQueue.poll(waitOnce, timeUnit);
			} catch (InterruptedException e) {

				LogUtil.getCommonLogger().error("error in polling", e);
			}
			if (jedis == null) {
				if (checkStatus(now)) {
					final Lock lock = this.lock;
					lock.lock();
					try {
						if (checkStatus(now)) {
							// try again before create
							jedis = jedisQueue.poll();
							if (jedis != null) {
								return jedis;
							}
							jedis = create0(now);
						}
					} finally {
						lock.unlock();
					}
				}
			}
			return jedis;
		}
		return null;
	}

	public static void giveback(ParallelJedis jedis) {
		if (jedis != null) {
			jedis.getParallelStatus().giveback0(jedis);
		}
	}

	/**
	 * always success
	 * 
	 * @param jedis
	 * @return
	 */
	private void giveback0(ParallelJedis jedis) {
		final int jedisVersion = jedis.getVersion();
		boolean disconnect = true;
		if (jedis.broken()) {
			silence(jedisVersion, this.silenceTime);
		} else {
			// check before lock
			if (jedisVersion == this.version) {
				final Lock lock = this.lock;
				lock.lock();
				try {
					if (jedisVersion == this.version) {
						if (jedisQueue.offer(jedis)) {
							disconnect = false;
						} else {
							// too much client
							count--;
						}
					} else {
						// wrong version
					}
				} finally {
					lock.unlock();
				}
			}
		}
		// disconnect after unlock
		if (disconnect) {
			jedis.disconnect();
		}
	}

	/**
	 * create one, need lock before
	 * 
	 * @return
	 */
	private ParallelJedis create0(long now) {
		ParallelJedis jedis = new ParallelJedis(this);
		if (needCheck) {
			boolean checkResult = checkConnection(jedis);
			if (checkResult) {
				needCheck = false;
			} else {
				wakeTime = System.currentTimeMillis() + silenceTime;
				jedis.disconnect();
				return null;
			}
		}
		jedis.setVersion(this.version);
		count++;
		final Logger logger = LogUtil.getCommonLogger();
		if (logger.isDebugEnabled()) {
			logger.debug("create:" + this.redisConnection + " count:" + count);
		}
		return jedis;
	}

	/**
	 * check one JedisConnection,DO NOT need lock before
	 * 
	 * @return true while the connection is OK
	 */
	private boolean checkConnection(ParallelJedis jedis) {
		try {
			// all parallelJedis need to be readable and writable
			byte[] kv = "ParallelTesterKV".getBytes();
			jedis.setex(kv, 1, kv);
			jedis.get(kv);
			return true;
		} catch (Exception e) {
			LogUtil.getCommonLogger().error(
					"error in check connection for ParallelJedis["
							+ redisConnection + "]", e);
			return false;
		}
	}

	public int getClientNum() {
		return count;
	}

	public long getWakeTime() {
		return wakeTime;
	}

	public int getVersion() {
		return this.version;
	}

	public RedisConnection getRedisConnection() {
		return redisConnection;
	}

	public void close() {
		final Lock lock = this.lock;
		lock.lock();
		try {
			silence(this.version, 315360000000l);
		} finally {
			lock.unlock();
		}
	}

}
