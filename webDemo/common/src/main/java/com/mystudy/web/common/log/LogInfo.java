package com.mystudy.web.common.log;

import java.io.Serializable;

/**
 * Created by 程祥 on 15/11/27.
 * Function：日志信息model
 */
public class LogInfo implements Cloneable, Serializable{
    public static final char TAB = '\t';
    public static final char ENTER = '\n';


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
     * 日志类型 现在位运算只支持3位（1代表是、0代表否）， 右数第一位为代表计算进日志系统（mongodb中保存时间14天）
     * 右数第二位为代表计算进入checklist 右数第三位为代表计算hadoop日志 的pv和uv日志
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

    /**
     * 日志记录时间（格式：yyyy-MM-dd HH:mm:ss SSS）
     */
    private String logTime;
    /**
     *
     */
    private String span;
    /**
     * （用户自定义日志使用数字代表，此项可用于对日志的个性化处理的区分）
     */
    private String userLogType;
    /**
     * 用户id，无线设备id(可用于分析用户行为使用)
     */
    private String sessionId;
    /**
     * Client机器id
     */
    private String cookieId;
    /**
     * 耗时、毫秒数
     */
    private String elapsedTime;
    /**
     * 用户http请求的头信息内容（可自定义输出内容，必须json格式）
     */
    private String requestHeader;
    /**
     * 用户http请求的请求内容（必须json格式）（可以用于记录调用方法的参数）
     */
    private String requestBody;
    /**
     * 接口响应结果（0代表成功，非0代表失败
     */
    private String responseCode;
    /**
     * 接口响应结果（0代表成功，非0代表失败
     */
    private String businessErrorCode;
    /**
     * 返回结果（一次接口请求的结果数据，或者方法调用的放回内容）
     */
    private String responseBody;
    /**
     * 用于记录hadoop补充信息各业务先自定义内容要求使用#分隔， 在请求中不能完全给出hadoop日志需要的信息的时候， 就需要填写“Hadoop
     * content”这个字段来补充内容， 我们会通过“BusinessLine”字段来区分各业务系统的日志
     */
    private String hadoopContent;
    /**
     * 异常的堆栈信息
     */
    private Throwable exception;
    /**
     * exceptionmsg 异常的说明
     */
    private String exceptionMsg;
    /**
     * 扩展字段1(建议json传送)
     */
    private String extend1;
    /**
     * 扩展字段2
     */
    private String extend2;

    public String getLogTime() {
        return logTime;
    }

    public void setLogTime(String logTime) {
        this.logTime = logTime;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public String getUserLogType() {
        return userLogType;
    }

    public void setUserLogType(String userLogType) {
        this.userLogType = userLogType;
    }

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getCookieId() {
        return cookieId;
    }

    public void setCookieId(String cookieId) {
        this.cookieId = cookieId;
    }

    public String getElapsedTime() {
        return elapsedTime;
    }

    public void setElapsedTime(String elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    public String getRequestHeader() {
        return requestHeader;
    }

    public void setRequestHeader(String requestHeader) {
        this.requestHeader = requestHeader;
    }

    public String getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(String requestBody) {
        this.requestBody = requestBody;
    }

    public String getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(String responseCode) {
        this.responseCode = responseCode;
    }

    public String getBusinessErrorCode() {
        return businessErrorCode;
    }

    public void setBusinessErrorCode(String businessErrorCode) {
        this.businessErrorCode = businessErrorCode;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }

    public String getHadoopContent() {
        return hadoopContent;
    }

    public void setHadoopContent(String hadoopContent) {
        this.hadoopContent = hadoopContent;
    }

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public String getExceptionMsg() {
        return exceptionMsg;
    }

    public void setExceptionMsg(String exceptionMsg) {
        this.exceptionMsg = exceptionMsg;
    }

    public String getExtend1() {
        return extend1;
    }

    public void setExtend1(String extend1) {
        this.extend1 = extend1;
    }

    public String getExtend2() {
        return extend2;
    }

    public void setExtend2(String extend2) {
        this.extend2 = extend2;
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
