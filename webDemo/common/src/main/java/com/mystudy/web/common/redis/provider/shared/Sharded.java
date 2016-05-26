package com.mystudy.web.common.redis.provider.shared;


import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.extend.ExtendJedis;
import com.mystudy.web.common.redis.provider.PoolProvider;

public class Sharded extends PoolProvider<ExtendJedis> {

	public Sharded(ClusterConfig config) {
		super(config);
		pool = new ShardedPool(config);
	}

	 
}
