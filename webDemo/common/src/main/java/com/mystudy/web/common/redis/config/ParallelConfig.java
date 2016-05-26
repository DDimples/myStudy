package com.mystudy.web.common.redis.config;


public class ParallelConfig extends TwemproxyConfig {
	private boolean usingLocal = true;
	private String order;
	private int checkIndex = 1;

	public boolean isUsingLocal() {
		return usingLocal;
	}

	public void setUsingLocal(boolean usingLocal) {
		this.usingLocal = usingLocal;
	}

	public int getCheckIndex() {
		return checkIndex;
	}

	public void setCheckIndex(int checkIndex) {
		this.checkIndex = checkIndex;
	}

	public String getOrder() {
		return order;
	}

	public void setOrder(String order) {
		this.order = order;
	}

}
