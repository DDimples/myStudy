package com.mystudy.web.cache.config;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.List;

public class CacheItemConfig {

	private String region;
	private String configKey;
	// default to NaN. 0 could be set override percent from CacheConfig
	private float percent = Float.NaN;
	/**
	 * max millisecond to wait before get result, default 1000ms
	 */
	private long asyncTimeout = 1000;
	private List<CacheLevelConfig> cacheLevelList;
	private List<CacheSetting> cacheList;

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.region = region;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.configKey = configKey;
	}

	public List<CacheLevelConfig> getCacheLevelList() {
		return cacheLevelList;
	}

	public void setCacheLevelList(List<CacheLevelConfig> cacheLevelList) {
		this.cacheLevelList = cacheLevelList;
		this.cacheList = null;// rebuild cache
	}

	public List<CacheSetting> getCacheSettingMap() {
		if (cacheList == null) {
			synchronized (this) {
				if (cacheList == null) {
					initCacheSetting();
				}
			}
		}
		return cacheList;
	}

	private void initCacheSetting() {
		if (cacheList == null) {
			EnumMap<CacheLevel, CacheSetting> cacheMap = new EnumMap<CacheLevel, CacheSetting>(
					CacheLevel.class);
			for (CacheLevelConfig levelConfig : this.getCacheLevelList()) {
				CacheLevel level = levelConfig.getCacheLevel();
				CacheSetting setting = new CacheSetting();
				setting.setCacheLevel(level);
				setting.setConfigKey(this.configKey);
				setting.setRegion(this.region);
				setting.setCacheExpire(levelConfig.getCacheExpire());
				setting.setExpireTime(levelConfig.getExpireTime());
				cacheMap.put(level, setting);
			}
			this.cacheList = new ArrayList<CacheSetting>(cacheMap.values());
		}
	}

	public long getAsyncTimeout() {
		return asyncTimeout;
	}

	public void setAsyncTimeout(long asyncTimeout) {
		this.asyncTimeout = asyncTimeout;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

}
