package com.mystudy.web.common.util;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Properties;

public class ConfigUtil {
	
	/**
	 * 通过文件名获取资源文件信息
	 * 
	 * @param fileName
	 *            文件名
	 * @return
	 * @throws FileNotFoundException
	 *             ,IOException
	 */
	public static Properties getProperties(String fileName)
			throws FileNotFoundException, IOException {
		if (fileName == null || fileName.trim().length() == 0) {
			//logger.info("The fileName parameter of getProperties(.) function is empty or null!");
			return null;
		}
		Properties p = new Properties();
		InputStream is = null;
		try {
			is = new BufferedInputStream(new FileInputStream(fileName));
			p.load(is);
		} catch (FileNotFoundException e) {
			throw e;
		} catch (IOException e) {
			throw e;
		} finally {
			if (is != null) {
				is.close();
			}
		}
		return p;
	}

	/**
	 * 
	 * @param fileName
	 * @return
	 * @throws FileNotFoundException
	 * @throws IOException
	 */
	public static HashMap<String, String> getPropertiesValue(String fileName)
			throws FileNotFoundException, IOException {

		HashMap<String, String> configList = new HashMap<String, String>();
		Properties propFile = getProperties(fileName);
		Enumeration<?> keyList = propFile.propertyNames();
		while (keyList.hasMoreElements()) {
			String key = (String) keyList.nextElement();
			String value = propFile.getProperty(key);
			configList.put(key, value);
		}
		return configList;
	}
}