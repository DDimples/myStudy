package com.mystudy.web.common.redis.provider.shared;


import com.mystudy.web.common.redis.extend.ExtendJedis;

public class ShardedJedis extends ExtendJedis {

	public ShardedJedis(ExtendShardInfo extendShardInfo) {
		super(extendShardInfo);
		this.extendShardInfo = extendShardInfo;
	}

	private final ExtendShardInfo extendShardInfo;

	public ExtendShardInfo getExtendShardInfo() {
		return extendShardInfo;
	}

}
