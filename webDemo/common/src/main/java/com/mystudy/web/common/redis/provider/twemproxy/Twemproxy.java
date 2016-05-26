package com.mystudy.web.common.redis.provider.twemproxy;


import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.provider.PoolProvider;

public class Twemproxy extends PoolProvider<TwemproxyJedis> {

	public Twemproxy(ClusterConfig config) {
		super(config);
		pool = new TwemproxyClientPool(config);
	}

}
