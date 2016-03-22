package com.mystudy.web.common.util;

/**
 * simple wrapper
 * 
 * @author xiang.liu
 *
 */
public  class Wrapper<T> {
	private T t;

	public Wrapper(T t) {
		this.t = t;
	}

	public void setT(T t) {
		this.t = t;
	}

	public T getT() {
		return t;
	}
}
