package com.mystudy.web.cache.config;

public class CacheSetting {

	private CacheLevel cacheLevel;
	private String region;
	private String configKey;
	private CacheExpire cacheExpire = CacheExpire.AbsoluteTime;
	private int expireTime;
	private Integer hash;

	public CacheSetting() {
	}

	public CacheLevel getCacheLevel() {
		return cacheLevel;
	}

	public void setCacheLevel(CacheLevel cacheLevel) {
		this.hash = null;
		this.cacheLevel = cacheLevel;
	}

	public String getRegion() {
		return region;
	}

	public void setRegion(String region) {
		this.hash = null;
		this.region = region;
	}

	public String getConfigKey() {
		return configKey;
	}

	public void setConfigKey(String configKey) {
		this.hash = null;
		this.configKey = configKey;
	}

	public CacheExpire getCacheExpire() {
		return cacheExpire;
	}

	public void setCacheExpire(CacheExpire cacheExpire) {
		this.hash = null;
		this.cacheExpire = cacheExpire;
	}

	/**
	 * 过期时间（秒）
	 */
	public int getExpireTime() {
		return expireTime;
	}

	public void setExpireTime(int expireTime) {
		this.hash = null;
		this.expireTime = expireTime;
	}

	@Override
	public int hashCode() {
		// cache hash at the beginning of method
		Integer currenthash = hash;
		// cache hash for this instance with the risk of non synchronous
		return currenthash == null ? hash = (cacheLevel.hashCode()
				^ region.hashCode() ^ configKey.hashCode()
				^ cacheExpire.hashCode() ^ expireTime)
				: (currenthash == hash ? currenthash : this.hashCode());
	}

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (CacheSetting.class.isInstance(obj)) {
			CacheSetting other = (CacheSetting) obj;
			return this.cacheLevel == other.cacheLevel
					&& this.expireTime == other.expireTime
					&& this.cacheExpire == other.cacheExpire
					&& (this.region == null ? other.region == null
							: this.region.equals(other.region))
					&& (this.configKey == null ? other.configKey == null
							: this.configKey.equals(other.configKey));
		}
		return false;
	}
}
