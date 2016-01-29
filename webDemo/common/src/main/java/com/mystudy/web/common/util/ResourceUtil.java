package com.mystudy.web.common.util;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class ResourceUtil {

	public static String getCurrentPath(String... path) {
		URL url = Thread.currentThread().getContextClassLoader()
				.getResource("");
		Path p = Paths.get(URI.create(url.toString()));
		return Paths.get(p.toString(), path).toString();
	}

	/**
	 * 读取配置文件，需要传入目录和文件名，返回为map
	 * 
	 * @param dir
	 * @param fileName
	 * @return
	 * @throws ClassNotFoundException
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static Map<String, String> getProperties(String dir, String fileName)
			throws ClassNotFoundException, FileNotFoundException, IOException {
		String path = ResourceUtil.getCurrentPath(dir, fileName);
		return ConfigUtil.getPropertiesValue(path);
	}

	/**
	 * 
	 * @param propertiesMap
	 * @param key
	 * @return 返回符合条件的keys及其values
	 */
	public static List<String[]> getKeyValueList(
			Map<String, String> propertiesMap, String key) {
		if (propertiesMap != null) {
			Set<String> keySet = propertiesMap.keySet();
			List<String[]> res = new ArrayList<String[]>();
			for (String item : keySet) {
				if (item.matches(key)) {
					String[] temp = new String[2];
					temp[0] = item;
					temp[1] = propertiesMap.get(item);
					res.add(temp);
				}
			}
			return res;

		}
		return null;
	}

	/**
	 * 
	 * @param propertiesMap
	 * @param key
	 * @return 返回符合条件的values
	 */
	public static List<String> getValueList(Map<String, String> propertiesMap,
			String key) {
		if (propertiesMap != null) {
			Set<String> keySet = propertiesMap.keySet();
			List<String> res = new ArrayList<String>();
			for (String item : keySet) {
				if (item.matches(key)) {
					res.add(propertiesMap.get(item));
				}
			}
			return res;

		}
		return null;
	}
}
