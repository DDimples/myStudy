package com.mystudy.web.cache.ehcache;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.alibaba.fastjson.JSON;

public class EhCacheTool {

	@SuppressWarnings("rawtypes")
	public static Map<String, Map<String, String>> getValueMap(String pattern) {
		EhCacheProvider ehCacheProvider = new EhCacheProvider();
		ehCacheProvider.start();
		CacheManager cacheManager = ehCacheProvider.manager;
		String[] cacheNames = cacheManager.getCacheNames();
		if (cacheNames != null && cacheNames.length > 0) {
			Map<String, Map<String, String>> res = new HashMap<String,Map<String, String>>();
			for (String cacheName : cacheNames) {
				Cache cache = cacheManager.getCache(cacheName);
				if (cache == null) {
					continue;
				}
				Map<String, String> objMap = new HashMap<String, String>();
				List keyList = cache.getKeys();
				if (keyList != null && !keyList.isEmpty()) {
					for (Object object : keyList) {
						try {
							if (String.class.isInstance(object)) {
								String key = (String) object;
								if (key.contains(pattern)) {
									Element ele = cache.get(key);
									String objString = null;
									Object temp = ele.getObjectValue();
									try {
										objString = JSON.toJSONString(temp);
									} catch (Exception e) {
										objString = seriaObject(temp)
												.toString();
									}
									objMap.put(key, objString);
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							continue;
						}
					}
					if (objMap != null && !objMap.isEmpty()) {
						res.put(cacheName, objMap);
					}

				}
				return res;
			}
		}

		return null;
	}

	@SuppressWarnings("unchecked")
	public static int removeKey(String param) {
		if (param == null || param.isEmpty()) {
			return 0;
		}
		int count = 0;
		try {
			EhCacheProvider ehCacheProvider = new EhCacheProvider();
			ehCacheProvider.start();
			CacheManager cacheManager = ehCacheProvider.manager;
			Map<String, List<String>> cacheNameKeyMap = (Map<String, List<String>>) JSON
					.parse(param);
			for (Entry<String, List<String>> entry : cacheNameKeyMap.entrySet()) {
				String cacheName = entry.getKey();
				List<String> keysList = entry.getValue();
				if (keysList != null && !keysList.isEmpty()) {
					Cache cache = cacheManager.getCache(cacheName);
					if (cache == null) {
						continue;
					}
					for (String key : keysList) {
						if (key != null && key.length() > 0) {
							cache.remove(key);
							count++;
						}
					}
				}
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return count;
	}

	public static int removeKey(String cacheName, String keys) {
		if (keys == null || keys.isEmpty() || cacheName == null
				|| cacheName.isEmpty()) {
			return 0;
		}
		try {
			EhCacheProvider ehCacheProvider = new EhCacheProvider();
			ehCacheProvider.start();
			CacheManager cacheManager = ehCacheProvider.manager;
			Cache cache = cacheManager.getCache(cacheName);
			if (cache != null) {
				int count = 0;
				for (String key : keys.split(",")) {
					if (key != null && key.length() > 0) {
						cache.remove(key);
						count++;
					}
				}
				return count;
			}

		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return 0;
	}

	@SuppressWarnings("rawtypes")
	public static Set<String> getPreFixSet() {
		EhCacheProvider ehCacheProvider = new EhCacheProvider();
		ehCacheProvider.start();
		CacheManager cacheManager = ehCacheProvider.manager;
		String[] cacheNames = cacheManager.getCacheNames();
		if (cacheNames != null && cacheNames.length > 0) {
			Set<String> res = new HashSet<String>();
			for (String cacheName : cacheNames) {
				Cache cache = cacheManager.getCache(cacheName);
				if (cache == null) {
					continue;
				}
				List keyList = cache.getKeys();
				if (keyList != null && !keyList.isEmpty()) {
					for (Object object : keyList) {
						try {
							if (String.class.isInstance(object)) {
								String key = (String) object;
								if (key != null && !key.isEmpty()
										&& key.contains("_")) {
									String prefix = key.split("_")[0];
									res.add(prefix);
								}
							}
						} catch (Exception e) {
							// TODO: handle exception
							continue;
						}
					}

				}
				return res;
			}
		}

		return null;
	}

	@SuppressWarnings("finally")
	private static byte[] seriaObject(Object obj) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		try {
			oos.writeObject(obj);
			oos.flush();
			return baos.toByteArray();
		} finally {
			try {
				oos.close();

			} catch (IOException e) {
				// ignore
			}
			return null;
		}

	}
}
