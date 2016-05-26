package com.mystudy.web.common.redis.provider.twemproxy;

import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.config.TwemproxyConfig;
import com.mystudy.web.common.redis.provider.shared.ExtendShardInfo;
import org.slf4j.Logger;

import redis.clients.jedis.exceptions.JedisException;


public class TwemproxyClient {
	private class Proxys {
		public Proxys(TwemproxyStatus[] twems) {
			this.twems = twems;
			if (this.twems != null && this.twems.length > 0) {
				waitOnce = timeOut / (this.twems.length + 1);
			} else {
				waitOnce = 0;
			}
		}

		final TwemproxyStatus[] twems;
		volatile int index;
		final long waitOnce;
	}

	private final Lock priorQueueLock = new ReentrantLock();
	private final LinkedList<TwemproxyStatus> activeQueue;
	private final PriorityQueue<TwemproxyStatus> silentQueue;
	// private final static int TEST_EXPIRE = 10;
	// private final static String TESTKEY = "redisTestKey";
	// private final static String TESTVALUE = "redisTestValue";
	private volatile Proxys proxys;
	private int max = 50;
	private int min = 12;
	private long checkPeriod = 2000;
	private long timeToLive = 1000;
	private static final long defaultTimeOut = 300;
	private final long timeOut;
	private static final Logger LOG = LogUtil.getCommonLogger();
	private volatile long nextCheck = 0;
	private final String name;

	public TwemproxyClient(String name, List<ExtendShardInfo> shards,
			TwemproxyConfig twemproxyConfig) {
		long timeOut = defaultTimeOut;
		if (twemproxyConfig != null) {
			min = twemproxyConfig.getMin();
			int shardsSize = shards.size();
			if (min < shardsSize) {
				LOG.warn("minsize(" + min + ") is smaller than shardsSize("
						+ shardsSize + "),using shardsSize as minsize");
				min = shardsSize;
			}
			max = twemproxyConfig.getMax();
			if (max < min) {
				LOG.warn("maxsize(" + max + ") is smaller than minsize(" + min
						+ "),using minsize as maxsize");
				max = min;
			}
			timeOut = twemproxyConfig.getTimeOut();
			if (timeOut <= 0) {
				LOG.warn("timeOut(" + timeOut
						+ ") should be positive,using default("
						+ defaultTimeOut + ")");
				timeOut = defaultTimeOut;
			}
			long checkPeriod = twemproxyConfig.getCheckPeriod();
			if (checkPeriod <= 0) {
				LOG.warn("invaild time(" + checkPeriod + "),using default("
						+ this.checkPeriod + ")");
			} else {
				this.checkPeriod = checkPeriod;
			}
			long timeToLive = twemproxyConfig.getTimeToLive();
			if (timeToLive < this.timeToLive) {
				LOG.warn("timeToLive(" + timeToLive
						+ "ms) is too small to ensure safe, using default("
						+ this.timeToLive + ")");
			} else {
				this.timeToLive = timeToLive;
			}
		}
		int clientSize = shards.size();
		activeQueue = new LinkedList<TwemproxyStatus>();
		silentQueue = new PriorityQueue<TwemproxyStatus>(clientSize,
				new Comparator<TwemproxyStatus>() {
					@Override
					public int compare(TwemproxyStatus o1, TwemproxyStatus o2) {
						long res = o2.getWakeTime() - o1.getWakeTime();
						return res == 0 ? 0 : (res > 0 ? 1 : -1);
					}
				});
		for (int i = 0; i < clientSize; i++) {
			ExtendShardInfo info = shards.get(i);
			TwemproxyStatus twInfo = new TwemproxyStatus(info, max, 1);
			activeQueue.add(twInfo);
		}
		this.timeOut = timeOut;
		this.name = name;
		avgAccess();
	}

	// simple CAS lock, instead of real lock
	private final AtomicInteger simpleLock = new AtomicInteger(0);

	private void checkSilent(long now) {
		// check version and wake
		if (silentQueue.size() > 0 && nextCheck <= now) {
			if (simpleLock.compareAndSet(0, 1)) {
				try {
					boolean lock = true;
					if (activeQueue.isEmpty()) {
						priorQueueLock.lock();
					} else {
						lock = priorQueueLock.tryLock();
					}
					if (lock) {
						try {
							checkSilent0(now);
						} finally {
							priorQueueLock.unlock();
						}
					}
				} finally {
					simpleLock.set(0);
				}
			}
		}
	}

	/**
	 * check silentQueue and wake up proxies, should lock before
	 * 
	 * @param now
	 */
	private void checkSilent0(long now) {
		int size = silentQueue.size();
		TwemproxyStatus status2 = silentQueue.poll();
		if (status2 != null) {
			do {
				if (status2.getWakeTime() <= now) {
					activeQueue.offer(status2);
					status2 = silentQueue.poll();
				} else {
					silentQueue.offer(status2);
					nextCheck = status2.getWakeTime();
					break;
				}
			} while (status2 != null);
			if (silentQueue.size() != size) {
				avgAccess();
			}
		}
	}

	private TwemproxyJedis get() {
		long st = System.currentTimeMillis();
		checkSilent(st);
		TwemproxyJedis jedis = null;
		final Proxys proxys = this.proxys;
		if (proxys.twems.length == 0) {
			return null;
		}
		int i = proxys.index;
		proxys.index = i <= 0 ? proxys.twems.length - 1 : (i - 1);
		for (int l = 0; jedis == null && l < proxys.twems.length; l++) {
			TwemproxyStatus status = proxys.twems[i];
			// System.out.println(i + " " + status.used.incrementAndGet());
			if (status.checkStatus()) {
				jedis = status.borrow(proxys.waitOnce, TimeUnit.MILLISECONDS);
				if (jedis != null) {
					return jedis;
				}
			}
			i = i == 0 ? proxys.twems.length - 1 : (i - 1);
		}
		if (LOG.isDebugEnabled()) {
			LOG.debug(this
					+ " could not get an idle jedis try times but fails, time in ms:"
					+ (System.currentTimeMillis() - st));
		}
		return null;
	}

	public TwemproxyJedis borrow() {
		return get();
	}

	public TwemproxyJedis reborrow(TwemproxyJedis jedis, ClusterInfo cluster)
			throws JedisException {
		LOG.debug("reborrow");
		if (jedis != null) {
			giveback0(jedis);
		}
		return borrow();
	}

	/**
	 * the instance will disconnect when the queue is full
	 * 
	 * @param jedis
	 */
	public void giveback(TwemproxyJedis jedis) {
		giveback0(jedis);
	}

	/**
	 * average accesses to all active proxies, should lock before
	 */
	private void avgAccess() {
		int activeSize = activeQueue.size();
		if (activeSize > 0) {
			int avg = (max / activeSize) + 1;
			TwemproxyStatus[] twems = new TwemproxyStatus[activeSize];
			int corsor = 0;
			for (TwemproxyStatus status : activeQueue) {
				twems[corsor++] = status;
			}
			Proxys proxys = new Proxys(twems);
			this.proxys = proxys;
			if (LOG.isDebugEnabled()) {
				LOG.debug("max:" + max + " proxyNum:" + activeSize + " avg:"
						+ avg);
			}
		} else {
			this.proxys = new Proxys(new TwemproxyStatus[0]);
		}
	}

	/**
	 * real giving back
	 * 
	 * @param jedis
	 */
	private void giveback0(TwemproxyJedis jedis) {
		TwemproxyStatus status = jedis.getTwemproxyStatus();
		if (jedis.broken()) {
			priorQueueLock.lock();
			try {
				if (activeQueue.remove(status)) {
					status.silence(jedis.getVersion(), checkPeriod);
					silentQueue.offer(status);
					avgAccess();
					if (nextCheck > status.getWakeTime()) {
						// should not happen
						nextCheck = status.getWakeTime();
					}
				}
			} finally {
				priorQueueLock.unlock();
			}
		}
		TwemproxyStatus.giveback(jedis);
	}

	@Override
	public String toString() {
		return this.name + "(" + super.toString() + ")";
	}

	public void close() {
		for (TwemproxyStatus status : silentQueue) {
			status.close();
		}
		for (TwemproxyStatus status : activeQueue) {
			status.close();
		}
	}
}
