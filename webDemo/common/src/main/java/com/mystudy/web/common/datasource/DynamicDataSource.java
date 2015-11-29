package com.mystudy.web.common.datasource;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

/**
 * Created by 程祥 on 15/10/21.
 * Function：
 */
public class DynamicDataSource extends AbstractRoutingDataSource{

    private String defaultDataSourceKey;

    @Override
    protected Object determineCurrentLookupKey() {
        String dataSource = DataSourceSwitcher.getDataSource();
        if(dataSource != null){
            return dataSource;
        }
        return defaultDataSourceKey;
    }

    public String getDefaultDataSourceKey() {
        return defaultDataSourceKey;
    }

    public void setDefaultDataSourceKey(String defaultDataSourceKey) {
        this.defaultDataSourceKey = defaultDataSourceKey;
    }
}
