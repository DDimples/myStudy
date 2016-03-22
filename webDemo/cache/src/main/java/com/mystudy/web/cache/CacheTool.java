package com.mystudy.web.cache;

import com.mystudy.web.common.log.LogUtil;
import org.apache.commons.lang3.StringUtils;


/**
 * 缓存工具类
 *
 */
public class CacheTool {


	/**
	 * 取缓存
	 * @param region
	 * @param configkey
	 * @return
	 */
	public static Object getObject(String region, String configkey, String cacheKey) {
		CacheClient client = getCacheClient(region, configkey);
		return  getObject(client, cacheKey);
	}
	
	/**
	 * 取缓存
	 * @param client
	 * @return
	 */
	public static Object getObject(CacheClient client, String cacheKey) {
		if(client == null || StringUtils.isBlank(cacheKey)){
			return null;
		}
		try {
			return client.get(cacheKey).getValue();
		} catch (Exception e) {
			LogUtil.getCommonLogger().error("Get Cache error for key=" + cacheKey, e);
			return null;
		}
	}

	/**
	 * 写缓存
	 * @param region
	 * @param configkey
	 * @param obj
	 * @return
	 */
	public static boolean setObject(String region, String configkey, String cacheKey, Object obj) {
		CacheClient client = getCacheClient(region, configkey);
		return setObject(client, cacheKey, obj);	
	}
	
	/**
	 * 写缓存
	 * @param client
	 * @param obj
	 * @return
	 */
	public static boolean setObject(CacheClient client, String cacheKey, Object obj) {
		if (obj == null || client == null || StringUtils.isBlank(cacheKey)) {
			return false;
		}
		try {
			client.set(cacheKey, obj);
			return true;
		} catch (Exception e) {
			LogUtil.getCommonLogger().error("Set Cache error for key=" + cacheKey, e);
			return false;
		}
	}


	/**
	 * 删除缓存
	 * @param region
	 * @param configkey
	 * @param cacheKey
	 * @return
	 */
	public static boolean removeValue(String region, String configkey,String cacheKey) {
			CacheClient client = getCacheClient(region, configkey);
			return removeValue(client, cacheKey);

	}

	/**
	 * 删除缓存
	 * @param client
	 * @param cacheKey
	 * @return
	 */
	public static boolean removeValue(CacheClient client, String cacheKey) {
		
		if (client == null || StringUtils.isBlank(cacheKey)) {
			return false;
		}
		try {
			client.remove(cacheKey);
			return true;
		} catch (Exception e) {
			LogUtil.getCommonLogger().error("remove Cache error for key=" + cacheKey, e);
			return false;
		}
	}

	/**
	 * 获取客户端
	 * @param region
	 * @param configkey
	 * @return 
	 */
	public static CacheClient getCacheClient(String region, String configkey) {
		try {
			return CacheClient.getInstance(region, configkey);
		} catch (Exception e) {
			LogUtil.getCommonLogger().error(
					"Get CacheClient Error, region=" + region + ", configkey=" + configkey, e);
			return null;
		}
	}
}
