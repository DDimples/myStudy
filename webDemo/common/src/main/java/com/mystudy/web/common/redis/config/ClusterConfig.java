package com.mystudy.web.common.redis.config;

import java.util.List;

import redis.clients.jedis.JedisPoolConfig;

/**
 * redis cluster config
 * 
 * @author Liwl
 * 
 */
public class ClusterConfig {

	private String cluster;
	private boolean readWriteSpliting = false;
	private String hostList;
	private String splitTag = ":";

	private JedisPoolConfig poolConfig;
	private TwemproxyConfig twemproxyConfig;
	private ParallelConfig parallelConfig;
	private ClusterType clusterType;
	private List<String> clusterList;
	private double percent;

	public String getSplitTag() {
		return splitTag;
	}

	public void setSplitTag(String splitTag) {
		this.splitTag = splitTag;
	}

	public List<String> getClusterList() {
		return clusterList;
	}

	public void setClusterList(List<String> clusterList) {
		this.clusterList = clusterList;
	}

	public String getCluster() {
		return cluster;
	}

	public void setCluster(String cluster) {
		this.cluster = cluster;
	}

	public ClusterType getClusterType() {
		return clusterType;
	}

	public void setClusterType(ClusterType clusterType) {
		this.clusterType = clusterType;
	}

	public JedisPoolConfig getPoolConfig() {
		return poolConfig;
	}

	public void setPoolConfig(JedisPoolConfig poolConfig) {
		this.poolConfig = poolConfig;
	}

	public boolean isReadWriteSpliting() {
		return readWriteSpliting;
	}

	public String getClusterKey(RedisOption option) {
		return this.getCluster() + "_" + option.toString();
	}

	public void setReadWriteSpliting(boolean readWriteSpliting) {
		this.readWriteSpliting = readWriteSpliting;
	}

	public String getHostList() {
		return hostList;
	}

	public void setHostList(String hostList) {
		this.hostList = hostList;
	}

	public TwemproxyConfig getTwemproxyConfig() {
		return twemproxyConfig;
	}

	public void setTwemproxyConfig(TwemproxyConfig twemproxyConfig) {
		this.twemproxyConfig = twemproxyConfig;
	}

	public ParallelConfig getParallelConfig() {
		return parallelConfig;
	}

	public void setParallelConfig(ParallelConfig parallelConfig) {
		this.parallelConfig = parallelConfig;
	}

	public double getPercent() {
		return percent;
	}

	public void setPercent(double percent) {
		this.percent = percent;
	}

}
