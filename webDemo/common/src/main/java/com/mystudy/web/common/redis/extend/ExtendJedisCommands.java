package com.mystudy.web.common.redis.extend;

import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Client;

public interface ExtendJedisCommands {

	// Key
	Boolean exists(String cluster, String key) throws Exception;

	void persist(String cluster, String key) throws Exception;

	String type(String cluster, String key) throws Exception;

	void expire(String cluster, String key, int seconds) throws Exception;

	void expireAt(String cluster, String key, long unixTime) throws Exception;

	Long ttl(String cluster, String key) throws Exception;

	void del(String cluster, String... keys) throws Exception;

	Set<String> keys(String cluster, String pattern) throws Exception;

	// String

	void set(String cluster, String key, Object value) throws Exception;

	Object get(String cluster, String key) throws Exception;

	void setex(String cluster, String key, int seconds, Object value)
			throws Exception;

	List<Object> mget(String cluster, String... keys) throws Exception;

	void mset(String cluster, Map<String, Object> keyvalue) throws Exception;

	// Hash

	void hset(String cluster, String key, String field, Object value)
			throws Exception;

	Object hget(String cluster, String key, String field) throws Exception;

	void hmset(String cluster, String key, Map<String, Object> hash)
			throws Exception;

	List<Object> hmget(String cluster, String key, String... fields)
			throws Exception;

	Boolean hexists(String cluster, String key, String field) throws Exception;

	void hdel(String cluster, String key, String... field) throws Exception;

	Long hlen(String cluster, String key) throws Exception;

	Set<String> hkeys(String cluster, String key) throws Exception;

	List<Object> hvals(String cluster, String key) throws Exception;

	Map<String, Object> hgetAll(String cluster, String key) throws Exception;

	// List
	void rpush(String cluster, String key, Object... value) throws Exception;

	void lpush(String cluster, String key, Object... value) throws Exception;

	Long llen(String cluster, String key) throws Exception;

	List<Object> lrange(String cluster, String key, long start, long end)
			throws Exception;

	void ltrim(String cluster, String key, long start, long end)
			throws Exception;

	Object lindex(String cluster, String key, long index) throws Exception;

	void lset(String cluster, String key, long index, Object value)
			throws Exception;

	Object lpop(String cluster, String key) throws Exception;

	Object rpop(String cluster, String key) throws Exception;

	void linsert(String cluster, String key, Client.LIST_POSITION where,
			String pivot, Object value) throws Exception;

	// Set
	void sadd(String cluster, String key, Object... member) throws Exception;

	Set<Object> smembers(String cluster, String key) throws Exception;

	Object spop(String cluster, String key) throws Exception;

	Long scard(String cluster, String key) throws Exception;

	Boolean sismember(String cluster, String key, Object member)
			throws Exception;

	void close() throws Exception;

	/**
	 * increase key without expire time
	 * @param cluster
	 * @param key
	 * @return
	 * @throws Exception
	 */
	long incr(String cluster, String key) throws Exception;
	
	/**
	 * decrease key without expire time
	 * @param cluster
	 * @param key
	 * @return
	 * @throws Exception
	 */
	long decr(String cluster, String key) throws Exception;
	/**
	 * increase key with expire time
	 * @param cluster
	 * @param key
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	long incr(String cluster, String key,int seconds) throws Exception;
	
	/**
	 * decrease key with expire time
	 * @param cluster
	 * @param key
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	long decr(String cluster, String key,int seconds) throws Exception;

	/**
	 * 
	 * @param cluster
	 * @param key
	 * @param incrAmount
	 * @param seconds
	 * @return
	 * @throws Exception
	 */
	long incrby(String cluster, String key, int seconds, long incrAmount) throws Exception;
	
	long setInitCount(String cluster, String key ,int seconds, long initcount) throws Exception;

}
