package com.mystudy.web.common.redis.provider;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.redis.config.ClusterConfig;
import com.mystudy.web.common.redis.config.ClusterInfo;
import com.mystudy.web.common.redis.config.RedisOption;

public abstract class ClientProvider {

	private final ClusterConfig config;

	protected ClientProvider(ClusterConfig config) {
		this.config = config;
	}

	public String getKey(String cluster, String... keys) {

		StringBuilder cacheKey = new StringBuilder(cluster);
		for (String key : keys) {
			cacheKey.append(config.getSplitTag()).append(key);
		}
		return cacheKey.toString();
	}

	public String[] getKeys(String cluster, String... keys) {
		String[] cacheKeys = new String[keys.length];
		for (int i = 0; i < keys.length; i++) {
			cacheKeys[i] = getKey(cluster, keys[i]);
		}
		return cacheKeys;
	}

	public ClusterInfo getClusterInfo(RedisOption option, String cluster,
			String... keys) {
		ClusterInfo info = new ClusterInfo();
		info.setCluster(cluster);
		info.setOption(option);
		info.setCacheKey(getKey(cluster, keys));
		return info;
	}

	protected byte[][] string2byte(String... keys) {
		byte[][] byteKey = new byte[keys.length][];
		for (int i = 0; i < keys.length; i++) {
			byteKey[i] = keys[i].getBytes();
		}
		return byteKey;
	}

	protected byte[][] list2byte(String cluster, String... keys) {
		byte[][] byteKey = new byte[keys.length][];
		for (int i = 0; i < keys.length; i++) {
			byteKey[i] = getKey(cluster, keys[i]).getBytes();
		}

		return byteKey;
	}

	protected byte[][] list2byte(String cluster, Map<String, Object> keyvalue)
			throws Exception {
		byte[][] byteKey = new byte[keyvalue.size() * 2][];
		int i = 0;
		for (Entry<String, Object> item : keyvalue.entrySet()) {
			byteKey[i] = getKey(cluster, item.getKey()).getBytes();
			byteKey[i + 1] = object2byte(item.getValue());
			i += 2;
		}
		return byteKey;
	}

	protected Map<byte[], byte[]> map2byte(Map<String, Object> map)
			throws Exception {
		Map<byte[], byte[]> mapList = new HashMap<byte[], byte[]>(map.size());
		for (Entry<String, Object> item : map.entrySet()) {
			mapList.put(item.getKey().getBytes(), object2byte(item.getValue()));
		}

		return mapList;
	}

	protected Map<String, Object> byte2map(Map<byte[], byte[]> map)
			throws Exception {
		Map<String, Object> mapList = new HashMap<String, Object>(map.size());
		for (Entry<byte[], byte[]> item : map.entrySet()) {
			mapList.put(new String(item.getKey()), byte2object(item.getValue()));

		}

		return mapList;
	}

	protected Set<Object> byte2set(Set<byte[]> set) throws Exception {
		Set<Object> setList = new HashSet<Object>(set.size());
		for (byte[] bytes : set) {
			setList.add(byte2object(bytes));
		}
		return setList;
	}

	protected byte[] object2byte(Object obj) {
		if (obj == null)
			return null;
		try {
			return seriaObject(obj);
		} catch (ConcurrentModificationException e) {
			// try again
			try {
				return seriaObject(obj);
			} catch (Exception e1) {
				LogUtil.getCommonLogger().error(
						"error in seria Object[" + obj.getClass()
								+ "], after ConcurrentModificationException",
						e1);
				return null;
			}
		} catch (Exception e) {
			// throw e;
			LogUtil.getCommonLogger().error(
					"error in seria Object[" + obj.getClass() + "]", e);
			return null;
		}
	}

	private byte[] seriaObject(Object obj) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		ObjectOutputStream oos = new ObjectOutputStream(baos);
		// try {
		oos.writeObject(obj);
		oos.flush();
		return baos.toByteArray();
		// } finally {
		// close of ByteArrayOutputStream do nothing
		// try {
		// oos.close();
		// } catch (IOException e) {
		// // ignore
		// }
		// }
	}

	protected byte[][] object2byte(Object... objects) throws Exception {
		byte[][] byteKey = new byte[objects.length][];
		for (int i = 0; i < objects.length; i++) {
			byteKey[i] = object2byte(objects[i]);
		}
		return byteKey;

	}

	protected Object byte2object(byte[] bits) {
		if (bits == null || bits.length == 0)
			return null;
		ObjectInputStream ois = null;
		try {
			ByteArrayInputStream bais = new ByteArrayInputStream(bits);
			ois = new ObjectInputStream(bais);
			return ois.readObject();
		} catch (Exception e) {
			// throw e;
			LogUtil.getCommonLogger().error("error in de-seria Object", e);
			return null;
		} finally {
			if (ois != null)
				try {
					ois.close();
				} catch (IOException e) {
					// ignore
				}
		}

	}

	protected List<Object> byte2object(List<byte[]> bits) throws Exception {
		List<Object> list = new ArrayList<Object>();
		for (byte[] bytes : bits) {
			list.add(byte2object(bytes));
		}
		return list;
	}

	protected List<byte[]> object2byte(List<Object> obj) throws Exception {
		List<byte[]> list = new ArrayList<byte[]>();
		for (Object object : obj) {
			list.add(object2byte(object));
		}

		return list;
	}
}
