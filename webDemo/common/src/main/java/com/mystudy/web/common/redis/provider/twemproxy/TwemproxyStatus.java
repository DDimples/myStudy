package com.mystudy.web.common.redis.provider.twemproxy;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.provider.shared.ExtendShardInfo;
import org.slf4j.Logger;


public class TwemproxyStatus {
	private final ExtendShardInfo jedisInfo;
	private final ArrayBlockingQueue<TwemproxyJedis> jedisQueue;
	private final Lock lock = new ReentrantLock();
	private volatile long wakeTime;
	private final int capacity;
	private volatile int version = 0;
	private volatile int count;

	public TwemproxyStatus(ExtendShardInfo info, int capacity, int initNum) {
		jedisQueue = new ArrayBlockingQueue<TwemproxyJedis>(capacity);
		this.capacity = capacity;
		this.jedisInfo = info;
		for (int i = 0; i < initNum; i++) {
			TwemproxyJedis jedis = new TwemproxyJedis(this);
			jedis.setVersion(this.version);
			if (jedisQueue.offer(jedis)) {
				count++;
			} else {
				break;
			}
		}
	}

	public ExtendShardInfo getJedisInfo() {
		return jedisInfo;
	}

	public boolean checkStatus() {
		return wakeTime <= System.currentTimeMillis();
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
		if (this.version == version) {
			this.wakeTime = wt;
			List<TwemproxyJedis> brokenJedises = null;
			lock.lock();
			try {
				if (this.version == version) {
					this.version++;
					brokenJedises = removeAll();
					int clear = count;
					count = 0;
					return clear;
				}
			} finally {
				lock.unlock();
				disconnectAll(brokenJedises);
			}
		}
		return 0;
	}

	private List<TwemproxyJedis> removeAll() {
		List<TwemproxyJedis> brokenJedises = new ArrayList<TwemproxyJedis>(
				jedisQueue.size());
		int size = jedisQueue.drainTo(brokenJedises);
		return size > 0 ? brokenJedises : null;
	}

	private void disconnectAll(List<TwemproxyJedis> brokenJedises) {
		if (brokenJedises != null) {
			final Logger logger = LogUtil.getCommonLogger();
			for (TwemproxyJedis jedis : brokenJedises) {
				try {
					jedis.disconnect();
				} catch (Exception e) {
					logger.error("error in disconnect " + jedisInfo, e);
				}
			}
			if (logger.isDebugEnabled()) {
				logger.debug("disconnect all:" + jedisInfo + " count:"
						+ brokenJedises.size());
			}
		}
	}

	public TwemproxyJedis borrow(long waitOnce, TimeUnit timeUnit) {
		TwemproxyJedis jedis = null;
		try {
			jedis = jedisQueue.poll(waitOnce, timeUnit);
		} catch (InterruptedException e) {
			LogUtil.getCommonLogger().error("error in polling", e);
		}
		if (jedis == null) {
			lock.lock();
			try {
				jedis = create0();
			} finally {
				lock.unlock();
			}
		}
		return jedis;
	}

	public static void giveback(TwemproxyJedis jedis) {
		if (jedis != null) {
			jedis.getTwemproxyStatus().giveback0(jedis);
		}
	}

	/**
	 * always success
	 * 
	 * @param jedis
	 * @return
	 */
	private void giveback0(TwemproxyJedis jedis) {
		boolean disconnect = true;
		lock.lock();
		try {
			if (jedis.getVersion() == version) {
				if (jedisQueue.offer(jedis)) {
					disconnect = false;
				} else {
					// should not happen, overflow?
					count--;
				}
			} else {
				// wrong version
			}
		} finally {
			lock.unlock();
		}
		// disconnect after unlock
		if (disconnect) {
			jedis.disconnect();
		}
	}

	/**
	 * create one by using one access, need lock before
	 * 
	 * @return
	 */
	private TwemproxyJedis create0() {
		if (count < capacity && checkStatus()) {
			TwemproxyJedis jedis = new TwemproxyJedis(this);
			jedis.setVersion(version);
			count++;
			final Logger logger = LogUtil.getCommonLogger();
			if (logger.isDebugEnabled()) {
				logger.debug("create:" + this.jedisInfo + " count:" + count);
			}
			return jedis;
		}
		return null;
	}

	public int getClientNum() {
		return count;
	}

	public long getWakeTime() {
		return wakeTime;
	}

	public int getVersion() {
		return version;
	}

	public void close() {
		lock.lock();
		try {
			silence(version, 3600000);
		} catch (Exception e) {
		} finally {
			lock.unlock();
		}
	}
}
