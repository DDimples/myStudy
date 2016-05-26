package com.mystudy.web.common.redis.provider.parallel;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;
import java.util.NoSuchElementException;
import java.util.TreeMap;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import com.mystudy.web.common.beans.RandomNode;
import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.config.ParallelConfig;
import com.mystudy.web.common.redis.config.RedisConnection;
import com.mystudy.web.common.util.MurmurHashUtil;
import com.mystudy.web.common.util.OSUtil;
import org.slf4j.Logger;



public class ParallelClient {
	private static class ConnectionSet {
		private TreeMap<Integer, ParallelStatus> map = new TreeMap<Integer, ParallelStatus>();
		private final List<ParallelStatus> parallelStatusList;

		ConnectionSet(final List<ParallelStatus> parallelStatusList) {
			this.parallelStatusList = parallelStatusList;
			int fn = 0;
			for (ParallelStatus status : parallelStatusList) {
				String name = status.getRedisConnection().getConnectionInfo();
				for (int i = 0; i < 157; i++) {
					map.put(MurmurHashUtil
							.hash(((fn ^= i) + ":" + i + "@" + name).getBytes()),
							status);
				}
			}
		}

		ParallelJedis borrow(Object key, long waitOnce) {
			int hash = MurmurHashUtil.hash(key == null ? ThreadLocalRandom
					.current().nextInt() : key.hashCode());
			Entry<Integer, ParallelStatus> ceilEntry = map.ceilingEntry(hash);
			//faster and more effective
			return (ceilEntry != null || (ceilEntry = map.firstEntry()) != null) ? ceilEntry
					.getValue().borrow(waitOnce, TimeUnit.MILLISECONDS) : null;
		}

		void close() {
			for (ParallelStatus status : parallelStatusList) {
				status.close();
			}
		}
	}

	private static class ParallelStatusPool {
		private final List<RandomNode<ConnectionSet>> nodes;
		private final long waitOnce;

		public ParallelStatusPool(List<RandomNode<ConnectionSet>> nodes,
				long waitOnce) {
			this.nodes = nodes;
			this.waitOnce = waitOnce;
		}

		public Iterable<ParallelJedis> iterator(Object key) {
			return new ParallelItr(key);
		}

		class ParallelItr implements Iterator<ParallelJedis>,
				Iterable<ParallelJedis> {
			@Override
			public Iterator<ParallelJedis> iterator() {
				return this;
			}

			private Iterator<RandomNode<ConnectionSet>> itr;
			private Iterator<ConnectionSet> connectionIterator;
			private ConnectionSet connectionSet;
			private final Object key;
			private boolean hasnext = false;
			private boolean moved = false;

			public ParallelItr(Object key) {
				this.key = key;
			}

			@Override
			public boolean hasNext() {
				if (!moved) {
					hasnext = move();
					moved = true;
				}
				return hasnext;
			}

			private boolean move() {
				if (connectionIterator != null && connectionIterator.hasNext()) {
					connectionSet = connectionIterator.next();
					return true;
				}
				if (itr == null) {
					itr = nodes.iterator();
				}
				if (itr.hasNext()) {
					RandomNode<ConnectionSet> node = itr.next();
					if (node != null) {
						connectionIterator = node.iterator();
						return move();
					}
				}
				return false;
			}

			@Override
			public ParallelJedis next() {
				if (!moved) {
					hasnext = move();
				}
				moved = false;
				if (hasnext) {
					return connectionSet == null ? null : connectionSet.borrow(
							key, waitOnce);
				}
				throw new NoSuchElementException();
			}

			@Override
			public void remove() {
				// unsupported
			}

		}

		public void close() {
			for (RandomNode<ConnectionSet> node : nodes) {
				for (ConnectionSet connectionSet : node) {
					connectionSet.close();
				}
			}
		}

	}

	static ParallelStatusPool getNode(List<List<RedisConnection>> connections,
			int capacity, int initNum, long timeOut, long checkPeriod,
			CheckLocal checkLocal) {
		List<ConnectionSet> local = new ArrayList<ConnectionSet>();
		List<ConnectionSet> remote = new ArrayList<ConnectionSet>();
		if (checkLocal != null) {
			for (List<RedisConnection> connections2 : connections) {
				if (connections2.size() > 0) {
					List<ParallelStatus> status = new ArrayList<ParallelStatus>();
					for (RedisConnection connection : connections2) {
						status.add(new ParallelStatus(connection, capacity,
								initNum, checkPeriod));
					}
					boolean islocal = false;
					RedisConnection connection = connections2.get(0);
					try {
						String host = connection.getHost();
						islocal = checkLocal.check(host);
					} catch (Exception e) {
						LogUtil.getCommonLogger().warn(
								"error in check isLocal for " + connection, e);
					}
					ConnectionSet cset = new ConnectionSet(status);
					if (islocal) {
						local.add(cset);
					} else {
						remote.add(cset);
					}
				}
			}
		} else {
			List<ConnectionSet> psArr2 = new ArrayList<ConnectionSet>();
			for (List<RedisConnection> connections2 : connections) {
				List<ParallelStatus> status = new ArrayList<ParallelStatus>();
				for (RedisConnection connection : connections2) {
					ParallelStatus parallelStatus = new ParallelStatus(
							connection, capacity, initNum, checkPeriod);
					status.add(parallelStatus);
				}
				psArr2.add(new ConnectionSet(status));
			}
			remote = psArr2;
		}
		int localSize = local.size();
		int remoteSize = remote.size();
		if (localSize == 0) {
			RandomNode<ConnectionSet> randomNode = new RandomNode<ConnectionSet>(
					remote.toArray(new ConnectionSet[remoteSize]));
			return new ParallelStatusPool(Arrays.asList(randomNode), timeOut
					/ remoteSize);
		}
		if (remoteSize == 0) {
			RandomNode<ConnectionSet> randomNode = new RandomNode<ConnectionSet>(
					local.toArray(new ConnectionSet[localSize]));
			return new ParallelStatusPool(Arrays.asList(randomNode), timeOut
					/ localSize);
		}
		RandomNode<ConnectionSet> localSet = new RandomNode<ConnectionSet>(
				local.toArray(new ConnectionSet[localSize]));
		RandomNode<ConnectionSet> remoteSet = new RandomNode<ConnectionSet>(
				remote.toArray(new ConnectionSet[remoteSize]));
		return new ParallelStatusPool(Arrays.asList(localSet, remoteSet),
				timeOut / (localSize + remoteSize));
	}

	// private final static int TEST_EXPIRE = 10;
	// private final static String TESTKEY = "redisTestKey";
	// private final static String TESTVALUE = "redisTestValue";
	private volatile ParallelStatusPool node;
	private int max = 50;
	private int min = 12;
	private long checkPeriod = 5000;
	private static final long defaultTimeOut = 300;
	private static final Logger LOG = LogUtil.getCommonLogger();
	private final String name;

	public ParallelClient(String name, List<List<RedisConnection>> list,
			ParallelConfig parallelConfig) {
		long timeOut = defaultTimeOut;
		CheckLocal checkLocal = null;
		if (parallelConfig != null) {
			min = parallelConfig.getMin();
			max = parallelConfig.getMax();
			if (max < min) {
				LOG.warn("maxsize(" + max + ") is smaller than minsize(" + min
						+ "),using minsize as maxsize");
				max = min;
			}
			timeOut = parallelConfig.getTimeOut();
			if (timeOut <= 0) {
				LOG.warn("timeOut(" + timeOut
						+ ") should be positive,using default("
						+ defaultTimeOut + ")");
				timeOut = defaultTimeOut;
			}
			long checkPeriod = parallelConfig.getCheckPeriod();
			if (checkPeriod <= 0) {
				LOG.warn("invaild time(" + checkPeriod + "),using default("
						+ this.checkPeriod + ")");
			} else {
				this.checkPeriod = checkPeriod;
			}
			checkLocal = createCheckLocal(parallelConfig.isUsingLocal(),
					parallelConfig.getCheckIndex());
		}
		this.node = getNode(list, max, min, timeOut, checkPeriod, checkLocal);
		this.name = name;
	}

	private CheckLocal createCheckLocal(boolean usingLocal, int addrIndex) {
		return new CheckLocal(usingLocal, addrIndex);
	}

	private Iterable<ParallelJedis> get(String key) {
		final ParallelStatusPool node = this.node;
		return node.iterator(key);
	}

	public Iterable<ParallelJedis> borrow(String key) {
		return get(key);
	}

	@Override
	public String toString() {
		return this.name + "(" + super.toString() + ")";
	}

	public void close() {
		this.node.close();
	}

	class CheckLocal {
		protected final String addr;
		protected final boolean usingLocal;
		protected boolean equal;

		private String getAddrPrefix(int index) {
			InetAddress address = OSUtil.getLocalNetAddress();
			if (address != null) {
				byte[] addr = address.getAddress();
				if (addr != null && addr.length == 4 && index > 0) {
					StringBuilder sbd = new StringBuilder();
					for (int i = 0; i < index && i < addr.length; i++) {
						sbd.append(addr[i]).append('.');
					}
					return sbd.toString();
				}
			}
			return "NA";
		}

		public CheckLocal(boolean usingLocal) {
			this(usingLocal, 1);
		}

		public CheckLocal(boolean usingLocal, int index) {
			this.usingLocal = usingLocal;
			this.addr = usingLocal ? getAddrPrefix(index) : "NA";
		}

		boolean check(String host) {
			return usingLocal
					&& (host.length() < addr.length() ? (host + '.')
							.equals(addr) : host.startsWith(addr));
		}
	}

}
