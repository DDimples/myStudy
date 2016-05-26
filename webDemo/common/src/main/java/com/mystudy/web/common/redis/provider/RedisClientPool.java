package com.mystudy.web.common.redis.provider;

import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.extend.ExtendJedis;
import redis.clients.jedis.exceptions.JedisException;


public interface RedisClientPool<K extends ExtendJedis> {
	K borrow(ClusterInfo cluster);
	K reborrow (K jedis,ClusterInfo cluster) throws JedisException;
	void giveback (K jedis ,ClusterInfo cluster);
	void destory(ClusterInfo cluster);
	/**
	 * whether the operation update a few keys once like mset is supported
	 * @return
	 */
	void checkMmodifySupport() throws UnsupportedOperationException;
	void close();
}
