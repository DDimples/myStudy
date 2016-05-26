package com.mystudy.web.common.redis.provider.parallel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.config.RedisConnection;

public class ParallelClientPool {

	private Map<String, ParallelClient> parallelMap;
	private ClusterConfig config;

	public ParallelClientPool(ClusterConfig config) {
		parallelMap = new HashMap<String, ParallelClient>();
		ParallelConfigParser parser = new ParallelConfigParser();
		for (Entry<String, List<List<RedisConnection>>> item : parser
				.getParallelConnectionPool(config).entrySet()) {
			parallelMap.put(
					item.getKey(),
					new ParallelClient(config.getCluster() + "-"
							+ item.getKey(), item.getValue(), config
							.getParallelConfig()));
		}
		this.config = config;
	}

	public Iterable<ParallelJedis> borrow(ClusterInfo cluster) {
		Iterable<ParallelJedis> shardJedis = parallelMap.get(
				config.getClusterKey(cluster.getOption())).borrow(cluster.getCacheKey());
		return shardJedis;
	}

	public void giveback(ParallelJedis jedis) {
		ParallelStatus.giveback(jedis);
	}

	public void close() {
		for (ParallelClient parallelClient : parallelMap.values()) {
			parallelClient.close();
		}
	}
}
