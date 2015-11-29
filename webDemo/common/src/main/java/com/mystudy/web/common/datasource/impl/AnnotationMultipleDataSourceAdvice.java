package com.mystudy.web.common.datasource.impl;

import com.mystudy.web.common.datasource.AbstractDataSourceAdvice;
import com.mystudy.web.common.datasource.DataSourceSwitcher;
import com.mystudy.web.common.datasource.annotation.DataSource;

import java.lang.reflect.Method;


public class AnnotationMultipleDataSourceAdvice extends AbstractDataSourceAdvice{
    public void before(Method method, Object[] args, Object target) throws Throwable {
        DataSource dataSource = method.getAnnotation(DataSource.class);
        String datasourceKey=dataSource!=null ? dataSource.value():null;
        DataSourceSwitcher.setDataSource(datasourceKey);
        System.out.println("ttt******ttt");
    }

    public void afterReturning(Object arg0, Method method, Object[] args, Object target) throws Throwable {
        DataSourceSwitcher.clearDataSource();
    }

    public void afterThrowing(Method method, Object[] args, Object target, Exception ex) throws Throwable {
        DataSourceSwitcher.clearDataSource();
    }
}
  