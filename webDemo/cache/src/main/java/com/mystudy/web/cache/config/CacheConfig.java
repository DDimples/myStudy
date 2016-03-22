package com.mystudy.web.cache.config;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.mystudy.web.cache.CacheClient;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;


public class CacheConfig implements InitializingBean, DisposableBean {
	private boolean usingasync = true;
	private float percent = 0;//default to 0
	private List<CacheItemConfig> configList;

	public List<CacheItemConfig> getConfigList() {
		return configList;
	}

	public void setConfigList(List<CacheItemConfig> configList) {
		this.configList = configList;
		Map<CaseIgnoreStringArray, CacheItemConfig> temp = new HashMap<CaseIgnoreStringArray, CacheItemConfig>();
		for (CacheItemConfig cacheItemConfig : configList) {
			CaseIgnoreStringArray priKey = new CaseIgnoreStringArray(
					cacheItemConfig.getRegion(), cacheItemConfig.getConfigKey());
			temp.put(priKey, cacheItemConfig);
		}
		configMap = temp;
	}

	private Map<CaseIgnoreStringArray, CacheItemConfig> configMap;

	public CacheItemConfig getConfig(String region, String configName) {
		return configMap.get(new CaseIgnoreStringArray(region, configName));
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		CacheClient.setCacheConfig(this);
	}

	@Override
	public void destroy() throws Exception {
		CacheClient.destroy(this);
	}

	public boolean isUsingasync() {
		return usingasync;
	}

	public void setUsingasync(boolean usingasync) {
		this.usingasync = usingasync;
	}

	public float getPercent() {
		return percent;
	}

	public void setPercent(float percent) {
		this.percent = percent;
	}

}