package com.mystudy.web.common.log.model;

import java.io.Serializable;

/**
 * Created by 程祥 on 15/11/29.
 * Function：
 */
public class AbstractLogInfo implements Cloneable, Serializable {

    public static final char TAB = '\t';
    public static final char ENTER = '\n';

    /**
     * 用于区分各业务线，主要日志加工时对不同得业务线进行个性化处理（枚举：mobile,web,）
     */
    private String businessLine;
    /**
     * 跟踪ID（用于跟踪用户的一次请求流程）
     */
    private String traceId;
    /**
     * 服务器名
     */
    private String serverName;
    /**
     * 服务器IP
     */
    private String serverIp;
    /**
     * 业务线分支名称
     */
    private String appName;
    /**
     * 日志类型 定义规则
     */
    private String logType;
    /**
     * 页面url地址
     */
    private String pageUrl;
    /**
     * 查询串
     */
    private String queryString;
    /**
     * 调用服务接口的名称（可以使地址，action名称）
     */
    private String serviceName;

    public String getBusinessLine() {
        return businessLine;
    }

    public void setBusinessLine(String businessLine) {
        this.businessLine = businessLine;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
    }

    public String getServerName() {
        return serverName;
    }

    public void setServerName(String serverName) {
        this.serverName = serverName;
    }

    public String getServerIp() {
        return serverIp;
    }

    public void setServerIp(String serverIp) {
        this.serverIp = serverIp;
    }

    public String getAppName() {
        return appName;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public String getLogType() {
        return logType;
    }

    public void setLogType(String logType) {
        this.logType = logType;
    }

    public String getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(String pageUrl) {
        this.pageUrl = pageUrl;
    }

    public String getQueryString() {
        return queryString;
    }

    public void setQueryString(String queryString) {
        this.queryString = queryString;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
    }
}
