package com.mystudy.web.common.redis.provider.shared;

import java.util.List;
import java.util.regex.Pattern;

import org.apache.commons.pool.BasePoolableObjectFactory;
import org.apache.commons.pool.impl.GenericObjectPool;

import redis.clients.jedis.Jedis;
import redis.clients.util.Hashing;
import redis.clients.util.Pool;

/**
 * extend pool
 * @author Liwl
 *
 */
public class ExtendShardedJedisPool extends Pool<ExtendShardedJedis> {

	public ExtendShardedJedisPool(final GenericObjectPool.Config poolConfig,
			List<ExtendShardInfo> shards) {
		this(poolConfig, shards, Hashing.MURMUR_HASH);
	}

	public ExtendShardedJedisPool(final GenericObjectPool.Config poolConfig,
			List<ExtendShardInfo> shards, Hashing algo) {
		this(poolConfig, shards, algo, null);
	}

	public ExtendShardedJedisPool(final GenericObjectPool.Config poolConfig,
			List<ExtendShardInfo> shards, Pattern keyTagPattern) {
		this(poolConfig, shards, Hashing.MURMUR_HASH, keyTagPattern);
	}

	public ExtendShardedJedisPool(final GenericObjectPool.Config poolConfig,
			List<ExtendShardInfo> shards, Hashing algo, Pattern keyTagPattern) {
		super(poolConfig, new ExtendShardedJedisFactory(shards, algo,
				keyTagPattern));
	}

	/**
	 * PoolableObjectFactory custom impl.
	 */
	private static class ExtendShardedJedisFactory extends
			BasePoolableObjectFactory<ExtendShardedJedis> {
		private List<ExtendShardInfo> shards;
		private Hashing algo;
		private Pattern keyTagPattern;

		public ExtendShardedJedisFactory(List<ExtendShardInfo> shards,
				Hashing algo, Pattern keyTagPattern) {
			this.shards = shards;
			this.algo = algo;
			this.keyTagPattern = keyTagPattern;
		}

		public ExtendShardedJedis makeObject() throws Exception {
			ExtendShardedJedis jedis = new ExtendShardedJedis(shards, algo,
					keyTagPattern);
			return jedis;
		}

		public void destroyObject(final ExtendShardedJedis obj) throws Exception {
			if (obj != null) {
				ExtendShardedJedis shardedJedis = obj;
				for (Jedis jedis : shardedJedis.getAllShards()) {
					try {
						try {
							jedis.quit();
						} catch (Exception e) {

						}
						jedis.disconnect();
					} catch (Exception e) {

					}
				}
			}
		}

		public boolean validateObject(final ExtendShardedJedis obj) {
			try {
				if (obj == null) {
					return false;
				}
				ExtendShardedJedis jedis = obj;
				for (Jedis shard : jedis.getAllShards()) {
					if (!shard.ping().equals("PONG")) {
						return false;
					}
				}
				return true;
			} catch (Exception ex) {
				return false;
			}
		}
	}

}
