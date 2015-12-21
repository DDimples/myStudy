package com.mystudy.web.common;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.web.context.ContextLoaderListener;

/**
 * Created by 程祥 on 15/11/13.
 * Function：
 */
public class SpringContextUtil implements ApplicationContextAware, DisposableBean {

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
        applicationContext = null;
    }

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        if (SpringContextUtil.applicationContext != null) {
            System.out.println("SpringContext中的ApplicationContext被覆盖, 原有ApplicationContext为:"
                    + SpringContextUtil.applicationContext);
        }
        this.applicationContext = applicationContext; //NOSONAR
    }

    public void destroy() throws Exception {
        SpringContextUtil.clearContext();
    }



    public static String getContextPath() {
        return ContextLoaderListener.getCurrentWebApplicationContext().getServletContext().getContextPath();
    }

    public static String getResourceToken() {
        return ContextLoaderListener.getCurrentWebApplicationContext().getServletContext().getInitParameter("resource_token");
    }
}
