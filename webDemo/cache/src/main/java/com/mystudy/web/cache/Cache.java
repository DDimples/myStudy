package com.mystudy.web.cache;

import java.util.List;
import java.util.Map;

import com.mystudy.web.cache.config.CacheSetting;

public interface Cache {

	Object get(String key) throws CacheException;

	void put(String key, Object value) throws CacheException;

	void update(String key, Object value) throws CacheException;

	/**
	 * @return keys in list
	 * @throws CacheException
	 */
	List<?> keys() throws CacheException;

	void remove(String key) throws CacheException;

	List<?> mget(String... keys) throws CacheException;

	/**
	 * @deprecated unsafe method
	 * @throws CacheException
	 */
	@Deprecated
	void clear() throws CacheException;

	void setMap(String key, String field, Object value);

	Map<String, Object> getMap(String key, String... fields);

	CacheSetting getSetting();

	void setMap(String key, Map<String, Object> value);

	void lpush(String key, Object... value);

	Object lpop(String key);

	void hset(String key, String field, Object value);

	Object hget(String key, String field);

	void hmset(String key, Map<String, Object> hash);

	List<Object> hmget(String key, String... fields);

	long incr(String key);
	
	boolean hsupport();

	boolean msupport();

	boolean hmsupport();

	boolean lsupport();
	
	boolean incrsupport();
	

	void destroy() throws Exception;
}
