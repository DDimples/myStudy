package com.mystudy.web.common.log.model;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.regex.Pattern;

/**
 * Created by 程祥 on 15/11/29.
 * Function：
 */
public class CheckListLog extends AbstractLogInfo{
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

    public Throwable getException() {
        return exception;
    }

    public void setException(Throwable exception) {
        this.exception = exception;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    protected static final Pattern enter = Pattern.compile("\n");

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(this.getAppName()).append(TAB);
        stringBuilder.append(this.getServiceName()).append(TAB);
        stringBuilder.append(this.getLogType()).append(TAB);
        stringBuilder.append(this.getLogTime()).append(TAB);
        stringBuilder.append(this.getElapsedTime()).append(TAB);
        stringBuilder.append(this.getBusinessErrorCode()).append(TAB);
        stringBuilder.append(this.getExceptionMsg()).append(TAB);
        String exceptionString = null;
        if(this.getException()!=null){
            StringWriter sw = new StringWriter(1024);
            PrintWriter pw = new PrintWriter(sw);
            exception.printStackTrace(pw);
            pw = null;
            StringBuffer ex = sw.getBuffer();
            sw = null;
            //替换换行符
            exceptionString = enter.matcher(ex).replaceAll("#");
        }
        stringBuilder.append(exceptionString).append(TAB);
        stringBuilder.append(this.getResponseBody()).append(TAB);
        stringBuilder.append(this.getSessionId()).append(TAB);
        stringBuilder.append(this.getServerIp()).append(TAB);
        stringBuilder.append(this.getServerName()).append(TAB);
        stringBuilder.append(this.getPageUrl()).append(TAB);
        stringBuilder.append(this.getQueryString()).append(TAB);
        return stringBuilder.toString();
    }
}
