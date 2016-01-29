package com.mystudy.web.common.util;


import freemarker.template.TemplateModel;

public class UrlUtil implements TemplateModel {

	private static final String ELONGSTATIC_WEBSITE = "elongstatic";
	private static final String APPVERSION = "appversion";
	private static final String JAVASCRIPT_PATH = "jspath";

	public static String getCommonUrl(String file) {
		String url = "";
//		try {
//			url = ResourceUtil.getWebProperties().get(ELONGSTATIC_WEBSITE);
//			String appVersion = ResourceUtil.getWebProperties().get(APPVERSION);
//			if (!file.isEmpty()) {
//				url += file + "?t=" + appVersion;
//			}
//
//		} catch (Exception e) {
//			LogUtil.getApplicationLogger().error(
//					"The getCommonUrl(.) function happen error!", e);
//		}
		return url;
	}

	public static String getEmdCheckCodeUrl(String file) {
		String url = "";
//		try {
//			url = ResourceUtil.getWebProperties().get(ELONGSTATIC_WEBSITE);
//			if (!file.isEmpty()) {
//				HttpServletRequest request = HttpContext.getRequest();
//				if (request != null)
//					url = request.getContextPath() + file;
//			}
//
//		} catch (Exception e) {
//			LogUtil.getApplicationLogger().error(
//					"The getCommonUrl(.) function happen error!", e);
//		}
		return url;
	}

	public static String getStaticUrl(String file) {
		String url = "";
//		try {
//			url = ResourceUtil.getWebProperties().get(ELONGSTATIC_WEBSITE);
//			String appVersion = ResourceUtil.getWebProperties().get(APPVERSION);
//			if (!file.isEmpty()) {
//				url += file + "?" + appVersion;
//			}
//		} catch (Exception e) {
//			LogUtil.getApplicationLogger().error(
//					"The getStaticUrl(.) function happen error!", e);
//		}
		return url;
	}

	public static String getRequireJsPath(String file) {
		String url = "";
//		try {
//			String jsPath = ResourceUtil.getWebProperties()
//					.get(JAVASCRIPT_PATH);
//			String appPath = ResourceUtil.getAppPath();
//
//			if (DebugUtil.isDebug()) {
//				HttpServletRequest request = HttpContext.getRequest();
//				if (request != null)
//					url = StringUtil.isNotBlank(appPath) ? appPath+request.getContextPath() + jsPath + file
//							:request.getContextPath() + jsPath + file;
//			} else {
//				url += "../../.." + jsPath + file;
//			}
//		} catch (Exception e) {
//			LogUtil.getApplicationLogger().error(
//					"The getRequireJsPath(.) function happen error!", e);
//		}
		return url;
	}

	public static String getLocalUrl(String file) {
		String url = "";
//		try {
//			url = ResourceUtil.getWebProperties().get(ELONGSTATIC_WEBSITE);
//			String appVersion = ResourceUtil.getWebProperties().get(APPVERSION);
//			String appPath = ResourceUtil.getAppPath();
//			String jsPath = ResourceUtil.getWebProperties()
//					.get(JAVASCRIPT_PATH);
//			if (!file.isEmpty()) {
//				if (DebugUtil.isDebug()) {
//					HttpServletRequest request = HttpContext.getRequest();
//					if (request != null)
//						url =StringUtil.isNotBlank(appPath) ? appPath+request.getContextPath() + jsPath + file + "?t="
//								+ appVersion :
//								request.getContextPath() + jsPath + file + "?t="
//								+ appVersion;;
//				} else {
//					url += jsPath + file + "?" + appVersion;
//				}
//			}
//		} catch (Exception e) {
//			LogUtil.getApplicationLogger().error(
//					"The getLocalUrl(.) function happen error!", e);
//		}
		return url;
	}

	public static String getJumpUrl(String paths) {
		String url = "";
//		try {
//			String appPath = ResourceUtil.getAppPath();
//			HttpServletRequest request = HttpContext.getRequest();
//			if (request != null)
//				url = StringUtil.isNotBlank(appPath) ?
//					  appPath + request.getContextPath() + paths :
//					  request.getContextPath() + paths;
//		} catch (Exception e) {
//			LogUtil.getApplicationLogger().error(
//					"The getLocalUrl(.) function happen error!", e);
//		}
		return url;
	}

}
