package com.mystudy.web.common.redis.provider.shared;

import java.util.List;
import java.util.regex.Pattern;


import com.mystudy.web.common.redis.extend.ExtendJedis;
import redis.clients.util.Hashing;
import redis.clients.util.Sharded;

/**
 * extend shardedjedis
 * 
 * @author Liwl
 *
 */  
public class ExtendShardedJedis extends Sharded<ExtendJedis, ExtendShardInfo> {

	public ExtendShardedJedis(List<ExtendShardInfo> shards) {
		super(shards);
	}

	public ExtendShardedJedis(List<ExtendShardInfo> shards, Hashing algo) {
		super(shards, algo);
	}

	public ExtendShardedJedis(List<ExtendShardInfo> shards, Hashing algo,
			Pattern keyTagPattern) {
		super(shards, algo, keyTagPattern);
	}

	public ExtendShardedJedis(List<ExtendShardInfo> shards,
			Pattern keyTagPattern) {
		super(shards, keyTagPattern);
	}

	public ExtendJedis getJedis(String key) {
		return getShard(key);
	}

	public void disconnect() {
		for(ExtendJedis jedis:this.getAllShards()){
			jedis.disconnect();
		}
	}
	@Override
	protected void finalize() throws Throwable {
		this.disconnect();
		super.finalize();
	}
}
