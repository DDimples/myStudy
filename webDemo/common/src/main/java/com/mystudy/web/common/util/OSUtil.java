package com.mystudy.web.common.util;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

public class OSUtil {

	private static volatile String localIP;
	private static volatile InetAddress address;
	private static volatile String localName;
	private static volatile String hostName;
	private static volatile boolean isWinOS = false;
	static {
		address = getInetAdress();
		String tlocalIP = getLocalIP();
		localIP = (tlocalIP == null || tlocalIP.length() == 0) ? null
				: tlocalIP;
		String tlocalName = getLocalName();
		localName = (tlocalName == null || tlocalName.length() == 0) ? null
				: tlocalName;
		String thostName = getCanonicalHostName();
		hostName = (thostName == null || thostName.length() == 0) ? null
				: thostName;
		String osName = System.getProperty("os.name");
		isWinOS = (osName.toLowerCase().indexOf("windows") > -1) ? true : false;
	}

	/**
	 * 判断当前操作是否Windows.
	 * 
	 * @return true---是Windows操作系统
	 */
	public static boolean isWindowsOS() {
		return isWinOS;
	}

	/**
	 * 获取本机IP地址，并自动区分Windows还是Linux操作系统
	 * 
	 * @return String
	 */
	public static String getLocalIP() {
		if (localIP != null) {
			return localIP;
		}
		InetAddress ip = getLocalNetAddress();
		String sIP = "";

		if (null != ip) {
			sIP = ip.getHostAddress();
		}
		if (sIP != null && sIP.length() > 0) {
			localIP = sIP;
		}
		return sIP;
	}

	public static InetAddress getLocalNetAddress() {
		return address != null ? address : (address = getInetAdress());
	}

	public static String getLocalName() {
		if (localName != null) {
			return localName;
		}
		try {
			// simple but affective
			return localName = InetAddress.getLocalHost().getHostName();
		} catch (Exception e) {
		}
		InetAddress ip = getInetAdress();
		if (ip == null) {
			return "";
		}
		String sName = ip.getHostName();
		if (sName != null && sName.length() > 0) {
			localName = sName;
		}
		return sName;
	}

	public static String getCanonicalHostName() {
		if (hostName != null) {
			return hostName;
		}
		InetAddress ip = getInetAdress();
		if (ip == null) {
			return "";
		}
		String sCName = ip.getCanonicalHostName();
		if (sCName != null && sCName.length() > 0) {
			hostName = sCName;
		}
		return sCName;
	}

	private static InetAddress getInetAdress() {
		InetAddress ip = null;
		try {
			// 如果是Windows操作系统
			if (isWindowsOS()) {
				ip = InetAddress.getLocalHost();
			}
			// 如果是Linux操作系统
			else {
				boolean bFindIP = false;
				Enumeration<NetworkInterface> netInterfaces = NetworkInterface
						.getNetworkInterfaces();
				while (netInterfaces.hasMoreElements()) {
					if (bFindIP) {
						break;
					}
					NetworkInterface ni = netInterfaces.nextElement();
					// ----------特定情况，可以考虑用ni.getName判断
					// 遍历所有ip
					Enumeration<InetAddress> ips = ni.getInetAddresses();
					while (ips.hasMoreElements()) {
						ip = ips.nextElement();
						if (ip.isSiteLocalAddress() && !ip.isLoopbackAddress() // 127.开头的都是lookback地址
								&& ip.getHostAddress().indexOf(":") == -1) {
							bFindIP = true;
							break;
						}
					}

				}
			}
		} catch (Exception e) {
			return ip;
		}
		return ip;
	}
}
