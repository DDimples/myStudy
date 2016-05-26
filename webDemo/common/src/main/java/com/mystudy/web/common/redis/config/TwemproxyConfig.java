package com.mystudy.web.common.redis.config;

public class TwemproxyConfig {
	private int min;
	private int max;
	private long checkPeriod;
	private long timeOut;
	private long timeToLive;

	public int getMin() {
		return min;
	}

	public void setMin(int min) {
		this.min = min;
	}

	public int getMax() {
		return max;
	}

	public void setMax(int max) {
		this.max = max;
	}

	public long getCheckPeriod() {
		return checkPeriod;
	}

	public void setCheckPeriod(long checkPeriod) {
		this.checkPeriod = checkPeriod;
	}

	public long getTimeOut() {
		return timeOut;
	}

	public void setTimeOut(long timeOut) {
		this.timeOut = timeOut;
	}

	public long getTimeToLive() {
		return timeToLive;
	}

	public void setTimeToLive(long timeToLive) {
		this.timeToLive = timeToLive;
	}
}
