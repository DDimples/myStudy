package com.mystudy.web.common.datasource;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by 程祥 on 15/10/21.
 * Function：
 */

public class DataSourceSwitcher {

    
    @SuppressWarnings("rawtypes")
    private static final ThreadLocal contextHolder = new ThreadLocal();

    @SuppressWarnings("unchecked")
    public static void setDataSource(String dataSource) {
        if(StringUtils.isNotEmpty(dataSource)){
        		contextHolder.set(dataSource);
        }
    }
    
    public static String getDataSource() {
        String currDataSource = (String) contextHolder.get();
        return currDataSource;
    }

    public static void clearDataSource() {
        contextHolder.remove();
    }
}
