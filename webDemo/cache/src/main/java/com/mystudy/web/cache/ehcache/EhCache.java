package com.mystudy.web.cache.ehcache;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

import com.mystudy.web.cache.AbstractCache;
import com.mystudy.web.cache.CacheException;
import com.mystudy.web.cache.config.CacheSetting;
import net.sf.ehcache.Ehcache;
import net.sf.ehcache.Element;
import net.sf.ehcache.event.CacheEventListener;

public class EhCache extends AbstractCache implements CacheEventListener {
	private net.sf.ehcache.Cache cache;

	private CacheSetting setting;

	EhCache(net.sf.ehcache.Cache cache, CacheSetting setting) {
		this.cache = cache;
		this.setting = setting;
	}

	public List<?> keys() throws CacheException {
		return this.cache.getKeys();
	}

	public CacheSetting getSetting() {
		return this.setting;
	}

	public Object get(String key) throws CacheException {

		try {
			if (key == null) {
				return null;
			} else {
				Element element = cache.get(key);
				if (element != null) {
					return element.getObjectValue();
				}
			}
			return null;
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	public void update(String key, Object value) throws CacheException {
		put(key, value);
	}

	public void put(String key, Object value) throws CacheException {
		try {
			Element element = new Element(key, value);
			cache.put(element);
		} catch (IllegalArgumentException e) {
			throw new CacheException(e);
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}

	}

	public void remove(String key) throws CacheException {
		try {
			cache.remove(key);
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	public void clear() throws CacheException {
		try {
			cache.removeAll();
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	public void destroy() throws CacheException {
		try {
			cache.getCacheManager().removeCache(cache.getName());
		} catch (IllegalStateException e) {
			throw new CacheException(e);
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}
	}

	public void dispose() {
	}

	public void notifyElementExpired(Ehcache cache, Element element) {

	}

	public void notifyElementEvicted(Ehcache cache, Element element) {
	}

	public void notifyElementPut(Ehcache cache, Element element)
			throws net.sf.ehcache.CacheException {
	}

	public void notifyElementRemoved(Ehcache cache, Element element)
			throws net.sf.ehcache.CacheException {
	}

	public void notifyElementUpdated(Ehcache cache, Element element)
			throws net.sf.ehcache.CacheException {
	}

	public void notifyRemoveAll(Ehcache cache) {
	}

	@SuppressWarnings("unchecked")
	@Override
	public Map<String, Object> getMap(String key, String... fields) {
		Map<String, Object> result;
		Map<String, Object> map = (Map<String, Object>) this.get(key);
		if (map != null && fields.length > 0) {
			result = new HashMap<String, Object>();
			for (String field : fields) {
				result.put(field, map.get(field));
			}
		} else {
			result = map;
		}
		return result;
	}

	private final Object mapLock = new Object();

	@SuppressWarnings("unchecked")
	@Override
	public void setMap(String key, String field, Object value) {
		synchronized (mapLock) {
			Map<String, Object> map = null;
			map = (Map<String, Object>) this.get(key);
			if (map == null) {
				map = new ConcurrentHashMap<String, Object>();
			}
			map.put(field, value);
			this.put(key, map);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public void setMap(String key, Map<String, Object> value) {
		synchronized (mapLock) {
			Map<String, Object> map = null;
			map = (Map<String, Object>) this.get(key);
			if (map == null) {
				map = new ConcurrentHashMap<String, Object>();
			}
			map.putAll(value);
			this.put(key, map);
		}
	}

	@Override
	public Object lpop(String key) {
		try {
			if (key != null) {
				Element element = cache.get(key);
				if (element != null) {
					synchronized (queueLock) {
						element = cache.get(key);
						if (element != null) {
							Object objValue = element.getObjectValue();
							if (Queue.class.isInstance(objValue)) {
								Object result = ((Queue<?>) objValue).poll();
								cache.put(new Element(key, objValue));
								return result;
							}
						}
					}
				}
			}
			return null;
		} catch (net.sf.ehcache.CacheException e) {
			throw new CacheException(e);
		}

	}

	// element is a copy, the lock need be a bigger one
	private final Object queueLock = new Object();

	@SuppressWarnings("unchecked")
	@Override
	public void lpush(String key, Object... value) {
		if (key == null || value == null || value.length == 0) {
			return;
		}
		Queue<Object> queue = null;
		synchronized (queueLock) {
			Element element = cache.get(key);
			if (element != null) {
				Object objValue = element.getObjectValue();
				if (objValue != null) {
					if (Queue.class.isInstance(objValue)) {
						queue = ((Queue<Object>) objValue);
					} else {
						/*
						 * throw new CacheException("given key[" + key +
						 * "] is not a queue but "+ (objValue == null ? "NULL" :
						 * objValue.getClass())); prefer to do nothing instead
						 * of throwing exceptions
						 */
						return;
					}
				}
			}
			if (queue == null) {
				queue = new ConcurrentLinkedQueue<Object>(Arrays.asList(value));
			} else {
				for (Object e : value) {
					queue.add(e);
				}
			}
			element = new Element(key, queue);
			cache.put(element);
		}
	}

	@Override
	public Object hget(String key, String field) {
		Map<String, Object> map = getMap(key, field);
		if (map != null) {
			return map.get(field);
		}
		return null;
	}

	@Override
	public List<Object> hmget(String key, String... fields) {
		Map<String, Object> map = getMap(key, fields);
		if (map != null) {
			List<Object> objs = new ArrayList<Object>();
			for (String field : fields) {
				Object obj = map.get(field);
				objs.add(obj);
			}
			return objs;
		}
		return null;
	}

	@Override
	public void hmset(String key, Map<String, Object> hash) {
		setMap(key, hash);
	}

	@Override
	/**
	 * unsupported 2014.11.25
	 */
	public void hset(String key, String field, Object value) {
		setMap(key, field, value);
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		throw new CloneNotSupportedException("could not clone EhCache");
	}

}