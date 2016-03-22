package com.mystudy.web.common.util;

import java.util.Arrays;
import java.util.UUID;

public final class LogHolder {
	private LogHolder() {
	}

	private enum Proterty {
		ACTION_NAME, TRACE_ID, CLIENT_IP, URL, QUERY_STRING,SESSIONID;
		static final int size = values().length;
	}

	private static String businessLine;
	private static String appName;
	private static String span;
	private static ThreadLocal<String[]> logHolder = new ThreadLocal<String[]>() {
		@Override
		protected String[] initialValue() {
			return new String[Proterty.size];
		}
	};

	public static String getActionName() {
		return logHolder.get()[Proterty.ACTION_NAME.ordinal()];
	}

	public static void setActionName(String actionName) {
		logHolder.get()[Proterty.ACTION_NAME.ordinal()] = actionName;
	}

	public static String getTraceId() {
		String[] localPros = logHolder.get();
		int traceIdOrd = Proterty.TRACE_ID.ordinal();
		String traceId = localPros[traceIdOrd];
		if (traceId == null) {
			traceId = UUID.randomUUID().toString();
			localPros[traceIdOrd] = traceId;
		}
		return traceId;
	}

	public static String getBusinessLine() {
		return businessLine;
	}

	public static String getAppName() {
		return appName;
	}

	public static String getUrl() {
		return logHolder.get()[Proterty.URL.ordinal()];
	}

	public static void setUrl(String url) {
		logHolder.get()[Proterty.URL.ordinal()] = url;
	}

	public static String getClientIp() {
		return logHolder.get()[Proterty.CLIENT_IP.ordinal()];
	}

	public static void setClientIp(String clientIp) {
		logHolder.get()[Proterty.CLIENT_IP.ordinal()] = clientIp;
	}

	public static String getQueryString() {
		return logHolder.get()[Proterty.QUERY_STRING.ordinal()];
	}

	public static void setQueryString(String qString) {
		logHolder.get()[Proterty.QUERY_STRING.ordinal()] = qString;
	}

	public static String getSpan() {
		return span;
	}

	public static boolean copyLogProps(final LogProp logProp) {
		if (logProp != null) {
			final String[] props = logProp.props;
			if (props != null) {
				final String[] currentProps = logHolder.get();
				if (currentProps.length == props.length) {
					System.arraycopy(props, 0, currentProps, 0, props.length);
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * initialize necessary properties
	 */
	public static void initProps() {
		// initTraceId
		getTraceId();
	}

	public static LogProp getLogProp() {
		return new LogProp(logHolder.get());
	}

	public static class LogProp {
		final String[] props;

		LogProp(String[] props) {
			this.props = props;
		}
	}

	public static void clear() {
		Arrays.fill(logHolder.get(), null);
	}

	static void setBusinessLine(String businessLine2) {
		businessLine = businessLine2;
	}

	static void setAppName(String appName2) {
		appName = appName2;
	}

	static void setSpan(String span2) {
		span = span2;
	}

	public static String getSessionId() {
		return logHolder.get()[Proterty.SESSIONID.ordinal()];
	}

	public static void setSessionId(String sessionId) {
		logHolder.get()[Proterty.SESSIONID.ordinal()] = sessionId;
	}
}
