package com.mystudy.web.common.redis.provider.twemproxy;

import com.mystudy.web.common.redis.config.AbstractConfigParser;
import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.config.RedisConnection;
import com.mystudy.web.common.redis.provider.RedisClientPool;
import com.mystudy.web.common.redis.provider.shared.ExtendShardInfo;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;



public class TwemproxyClientPool implements RedisClientPool<TwemproxyJedis> {

	private Map<String, TwemproxyClient> proxyList;
	private ClusterConfig config;

	public TwemproxyClientPool(ClusterConfig config) {
		proxyList = new HashMap<String, TwemproxyClient>();
		AbstractConfigParser parser = new TwemproxyConfigParser();
		for (Entry<String, List<RedisConnection>> item : parser
				.getConnectionPool(config).entrySet()) {
			List<ExtendShardInfo> extendShardInfos = new ArrayList<ExtendShardInfo>(
					item.getValue().size());
			for (RedisConnection connection : item.getValue()) {
				ExtendShardInfo extendShardInfo = new ExtendShardInfo(
						connection.getHost(), connection.getPort(),
						connection.getTimeout(), connection.getName());
				extendShardInfos.add(extendShardInfo);
			}
			proxyList.put(
					item.getKey(),
					new TwemproxyClient(config.getCluster() + "-"
							+ item.getKey(), extendShardInfos, config
							.getTwemproxyConfig()));
		}
		this.config = config;
	}

	public TwemproxyJedis borrow(ClusterInfo cluster) {
		TwemproxyJedis shardJedis = proxyList.get(
				config.getClusterKey(cluster.getOption())).borrow();
		return shardJedis;
	}

	public TwemproxyJedis reborrow(TwemproxyJedis jedis, ClusterInfo cluster) {
		// 当前重试的三种方式分别为retry,reborrow,testOnReborrow
		/*
		 * return proxyList .get(config.getClusterKey(cluster.getOption()))
		 * .reborrow(new ExtendShardInfo(jedis.getHost(), jedis.getPort()))
		 * .getShard(cluster.getCacheKey());
		 */

		return proxyList.get(config.getClusterKey(cluster.getOption()))
				.reborrow(jedis, cluster);
	}

	public void giveback(TwemproxyJedis jedis, ClusterInfo cluster) {
		proxyList.get(config.getClusterKey(cluster.getOption()))
				.giveback(jedis);
	}

	public void destory(ClusterInfo cluster) {

	}

	public void checkMmodifySupport() {
		throw new UnsupportedOperationException(
				"this method is not supported by twemproxy");
	}

	@Override
	public void close() {
		for (TwemproxyClient client : proxyList.values()) {
			client.close();
		}
	}
}
