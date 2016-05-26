package com.mystudy.web.common.redis.config;


/**
 * Redis region Info
 * @author Liwl
 *
 */
public class ClusterInfo {

	private String cluster;
	
	
	private RedisOption option;
	
	private String cacheKey;

 
	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public RedisOption getOption() {
		return option;
	}

	public void setOption(RedisOption option) {
		this.option = option;
	}

	public String getCacheKey() {
		return cacheKey;
	}

	public void setCacheKey(String cacheKey) {
		this.cacheKey = cacheKey;
	}
}
