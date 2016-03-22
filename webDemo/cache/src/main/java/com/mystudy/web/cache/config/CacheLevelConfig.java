package com.mystudy.web.cache.config;

public class CacheLevelConfig {

	private CacheLevel cacheLevel = CacheLevel.Level_1;

	public CacheLevel getCacheLevel() {
		return cacheLevel;
	}

	public void setCacheLevel(CacheLevel cacheLevel) {
		this.cacheLevel = cacheLevel;
	}

	private CacheExpire cacheExpire = CacheExpire.AbsoluteTime;

	public CacheExpire getCacheExpire() {
		return cacheExpire;
	}

	public void setCacheExpire(CacheExpire cacheExpire) {
		this.cacheExpire = cacheExpire;
	}

	/**
	 * 过期时间（秒）
	 */
	private int expireTime;

	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.expireTime = expireTime;
	}
}
