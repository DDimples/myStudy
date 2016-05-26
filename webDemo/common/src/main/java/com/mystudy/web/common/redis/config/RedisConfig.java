package com.mystudy.web.common.redis.config;

 
import java.util.ArrayList;
import java.util.List;
 

public class RedisConfig {

	private List<ClusterConfig> clusterList;

	public List<ClusterConfig> getClusterList() {
		return clusterList;
	}

	public void setClusterList(List<ClusterConfig> clusterList) {
		this.clusterList = clusterList;
	}

	public String getClusterName(String cluster) {
		String clusterName = "*";
		for (String clusterStr : this.getClusterName()) {
			if (clusterStr.equalsIgnoreCase(cluster))
				clusterName = clusterStr;
		}
		return clusterName;
	}
	public List<String> getClusterName()
	{
		List<String> nameList =new ArrayList<String>();
		for(ClusterConfig config : clusterList)
		{
			nameList.add(config.getCluster());
		}
		return nameList;
	}

	
}
