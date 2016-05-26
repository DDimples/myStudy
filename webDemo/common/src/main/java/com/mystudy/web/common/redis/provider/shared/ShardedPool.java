package com.mystudy.web.common.redis.provider.shared;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.config.AbstractConfigParser;
import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.config.RedisConnection;
import com.mystudy.web.common.redis.extend.ExtendJedis;
import com.mystudy.web.common.redis.provider.RedisClientPool;
import redis.clients.jedis.exceptions.JedisException;


public class ShardedPool implements RedisClientPool<ExtendJedis> {

	// private final static Logger logger = LoggerFactory
	// .getLogger(ShardedPool.class);

	private final Map<String, ExtendShardedJedisPool> pool;
	private ClusterConfig config;

	public ShardedPool(ClusterConfig config) {
		pool = new HashMap<String, ExtendShardedJedisPool>();
		if (config != null) {
			this.config = config;
			AbstractConfigParser parser = new SharedConfigParser();
			Map<String, List<RedisConnection>> connections = parser
					.getConnectionPool(this.config);
			for (Entry<String, List<RedisConnection>> item : connections
					.entrySet()) {
				List<ExtendShardInfo> extendShardInfos = new ArrayList<ExtendShardInfo>(
						item.getValue().size());
				for (RedisConnection connection : item.getValue()) {
					ExtendShardInfo extendShardInfo = new ExtendShardInfo(
							connection.getHost(), connection.getPort(),
							connection.getTimeout(), connection.getName());
					extendShardInfos.add(extendShardInfo);
				}
				pool.put(item.getKey(),
						new ExtendShardedJedisPool(config.getPoolConfig(),
								extendShardInfos));

			}
		}
	}

	public ExtendJedis borrow(ClusterInfo cluster) {
		// logger.debug("borrow jedis from pool");
		try {

			ExtendShardedJedis exJedis = (ExtendShardedJedis) pool.get(
					config.getClusterKey(cluster.getOption())).getResource();
			ExtendJedis jedis = exJedis.getShard(cluster.getCacheKey());
			jedis.setPoolObject(exJedis);
			return jedis;
		} catch (Exception e) {
			throw new JedisException("获取jedis连接失败 ,Sharded", e);
		}
	}

	public ExtendJedis reborrow(ExtendJedis jedis, ClusterInfo cluster) {
		try {

			pool.get(config.getClusterKey(cluster.getOption()))
					.returnBrokenResource(
							(ExtendShardedJedis) jedis.getPoolObject());
			return borrow(cluster);
		} catch (Exception e) {
			throw new JedisException("重新加载jedis连接失败", e);
		}
	}

	public void giveback(ExtendJedis jedis, ClusterInfo cluster) {
		try {

			pool.get(config.getClusterKey(cluster.getOption())).returnResource(
					(ExtendShardedJedis) jedis.getPoolObject());

		} catch (Exception e) {
			throw new JedisException("回收jedis连接失败", e);
		}

	}

	public void destory(ClusterInfo cluster) {
		pool.get(config.getClusterKey(cluster.getOption())).destroy();

	}

	public void checkMmodifySupport() {
	}

	@Override
	public void close() {
		for (ExtendShardedJedisPool jedisPool : pool.values()) {
			try {
				jedisPool.destroy();
			} catch (Exception e) {
				LogUtil.getCommonLogger().error(
						"error in close shardedJedis", e);
			}
		}
	}
}
