package com.mystudy.web.cache;


import com.mystudy.web.cache.config.CacheItemConfig;

public class CacheObject {
	private CacheItemConfig config;
	private String key;
	private Object value;

	public CacheItemConfig getConfig() {
		return config;
	}

	public void setConfig(CacheItemConfig config) {
		this.config = config;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return "CacheObject key=" + key + ", value="
				+ (value == null ? "null" : value.getClass()) + "]";
	}

}