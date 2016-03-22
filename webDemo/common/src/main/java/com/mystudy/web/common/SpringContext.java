package com.mystudy.web.common;


import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringContext implements ApplicationContextAware, DisposableBean {

	private static ApplicationContext applicationContext = null;

	/**
	 * 取得存储在静态变量中的ApplicationContext.
	 */
	public static ApplicationContext getApplicationContext() {
		
		return applicationContext;
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	@SuppressWarnings("unchecked")
	public static <T> T getBean(String name) {
		return (T) applicationContext.getBean(name);
	}

	/**
	 * 从静态变量applicationContext中取得Bean, 自动转型为所赋值对象的类型.
	 */
	public static <T> T getBean(Class<T> requiredType) {
		
		return applicationContext.getBean(requiredType);
	}

	/**
	 * 清除SpringWebContextHolder中的ApplicationContext为Null.
	 */
	public static void clearContext() {
		System.out.println("清除SpringWebContextHolder中的ApplicationContext:" + applicationContext);
		applicationContext = null;
	}

	/**
	 * 实现ApplicationContextAware接口, 注入Context到静态变量中.
	 */
	public void setApplicationContext(ApplicationContext applicationContext) {

		System.out.println("注入ApplicationContext到SpringContext:" + applicationContext);

		if (SpringContext.applicationContext != null) {
			System.out.println("SpringContext中的ApplicationContext被覆盖, 原有ApplicationContext为:"
					+ SpringContext.applicationContext);
		}
		SpringContext.applicationContext = applicationContext; //NOSONAR
	}

	/**
	 * 实现DisposableBean接口, 在Context关闭时清理静态变量.
	 */
	public void destroy() throws Exception {
		SpringContext.clearContext();
	}
	
}
