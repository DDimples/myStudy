package com.mystudy.web.cache.ehcache;

import java.util.HashMap;

import com.mystudy.web.cache.CacheException;
import com.mystudy.web.cache.CacheProvider;
import com.mystudy.web.cache.config.CacheExpire;
import com.mystudy.web.cache.config.CacheSetting;
import com.mystudy.web.common.SpringContext;
import com.mystudy.web.common.util.StringUtil;
import net.sf.ehcache.Ehcache;
import org.slf4j.LoggerFactory;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
//import net.sf.ehcache.Ehcache;



public class EhCacheProvider implements CacheProvider {

	CacheManager manager;
	private final HashMap<String, EhCache> cacheManager = new HashMap<String, EhCache>();

	public EhCacheProvider() {
	}

	@Override
	public String name() {
		return "ehcache";
	}

	@Override
	public EhCache buildCache(CacheSetting setting) throws CacheException {
		String name = StringUtil.joinString(setting.getRegion(),
				setting.getConfigKey());
		EhCache ehcache = cacheManager.get(name);
		if (ehcache == null) {
			try {
				synchronized (cacheManager) {
					ehcache = cacheManager.get(name);
					if (ehcache == null) {
						// log.debug("Could not find configuration [" + name
						// + "]; using defaults.");
						Ehcache ehcache2 = manager.addCacheIfAbsent(name);
						if (ehcache2 != null) {
							if (setting.getCacheExpire() == CacheExpire.SlidingTime) {
								ehcache2.getCacheConfiguration()
										.setTimeToIdleSeconds(
												setting.getExpireTime());
								ehcache2.getCacheConfiguration()
										.setTimeToLiveSeconds(0);
							} else if (setting.getCacheExpire() == CacheExpire.AbsoluteTime) {
								ehcache2.getCacheConfiguration()
										.setTimeToLiveSeconds(
												setting.getExpireTime());
								ehcache2.getCacheConfiguration()
										.setTimeToIdleSeconds(0);
							}
						}
						Cache cache = manager.getCache(name);
						// log.debug("started EHCache region: " + name);
						LoggerFactory.getLogger(this.getClass()).info(
								"init ehcache " + cache);
						ehcache = new EhCache(cache, setting);
						cacheManager.put(name, ehcache);
					}
				}
			} catch (Exception e) {
				throw new CacheException(e);
			}
		}
		return ehcache;
	}

	@Override
	public void start() throws CacheException {
		if (manager == null) {
			try {
				Object bean = SpringContext.getBean("ehcache");
				if (CacheManager.class.isInstance(bean)) {
					manager = (CacheManager) bean;
				}
			} catch (Exception e) {
			}
			if (manager == null) {
				try {
					manager = SpringContext.getBean(CacheManager.class);
				} catch (Exception e) {
				}
				if (manager == null) {
					manager = CacheManager.create();
				}
			}
		}
	}

	@Override
	public void stop() {
		if (manager != null) {
			manager.shutdown();
			manager = null;
		}
	}
}
