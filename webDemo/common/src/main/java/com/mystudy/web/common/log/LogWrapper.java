package com.mystudy.web.common.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.Marker;

/**
 * Created by 程祥 on 15/11/27.
 * Function：
 */
public class LogWrapper implements Logger {

    private final  Logger logger;

    public Logger getLogger() {
        return logger;
    }

    public LogWrapper(String name){
        logger = LoggerFactory.getLogger(name);
    }

    public LogWrapper(Class<?> classType){
        logger = LoggerFactory.getLogger(classType);
    }


    public String getName() {
        return logger.getName();
    }

    public boolean isTraceEnabled() {
        return logger.isDebugEnabled();
    }

    public void trace(String msg) {
        logger.trace(msg);
    }

    public void trace(String format, Object arg) {
        logger.trace(format,arg);
    }

    public void trace(String format, Object arg1, Object arg2) {
        logger.trace(format,arg1,arg2);
    }

    public void trace(String format, Object... arguments) {
        logger.trace(format,arguments);
    }

    public void trace(String msg, Throwable t) {
        logger.trace(msg,t);
    }

    public boolean isTraceEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    public void trace(Marker marker, String msg) {

    }

    public void trace(Marker marker, String format, Object arg) {

    }

    public void trace(Marker marker, String format, Object arg1, Object arg2) {

    }

    public void trace(Marker marker, String format, Object... argArray) {

    }

    public void trace(Marker marker, String msg, Throwable t) {

    }

    public boolean isDebugEnabled() {
        return logger.isDebugEnabled();
    }

    public void debug(String msg) {
        logger.debug(msg);
    }

    public void debug(String format, Object arg) {
        logger.debug(format,arg);
    }

    public void debug(String format, Object arg1, Object arg2) {
        logger.debug(format,arg1,arg2);
    }

    public void debug(String format, Object... arguments) {
        logger.debug(format,arguments);
    }

    public void debug(String msg, Throwable t) {
        logger.debug(msg,t);
    }

    public boolean isDebugEnabled(Marker marker) {
        return logger.isDebugEnabled(marker);
    }

    public void debug(Marker marker, String msg) {
        logger.debug(marker,msg);
    }

    public void debug(Marker marker, String format, Object arg) {
        logger.debug(marker,format,arg);
    }

    public void debug(Marker marker, String format, Object arg1, Object arg2) {
        logger.debug(marker,format,arg1,arg2);
    }

    public void debug(Marker marker, String format, Object... arguments) {

    }

    public void debug(Marker marker, String msg, Throwable t) {

    }

    public boolean isInfoEnabled() {
        return logger.isInfoEnabled();
    }

    public void info(String msg) {
        logger.info(msg);
    }

    public void info(String format, Object arg) {
        logger.info(format,arg);
    }

    public void info(String format, Object arg1, Object arg2) {
        logger.info(format,arg1,arg2);
    }

    public void info(String format, Object... arguments) {
        logger.info(format,arguments);
    }

    public void info(String msg, Throwable t) {
        logger.info(msg,t);
    }

    public boolean isInfoEnabled(Marker marker) {
        return logger.isInfoEnabled(marker);
    }

    public void info(Marker marker, String msg) {

    }

    public void info(Marker marker, String format, Object arg) {

    }

    public void info(Marker marker, String format, Object arg1, Object arg2) {

    }

    public void info(Marker marker, String format, Object... arguments) {

    }

    public void info(Marker marker, String msg, Throwable t) {

    }

    public boolean isWarnEnabled() {
        return false;
    }

    public void warn(String msg) {

    }

    public void warn(String format, Object arg) {

    }

    public void warn(String format, Object... arguments) {

    }

    public void warn(String format, Object arg1, Object arg2) {

    }

    public void warn(String msg, Throwable t) {

    }

    public boolean isWarnEnabled(Marker marker) {
        return false;
    }

    public void warn(Marker marker, String msg) {

    }

    public void warn(Marker marker, String format, Object arg) {

    }

    public void warn(Marker marker, String format, Object arg1, Object arg2) {

    }

    public void warn(Marker marker, String format, Object... arguments) {

    }

    public void warn(Marker marker, String msg, Throwable t) {

    }

    public boolean isErrorEnabled() {
        return false;
    }

    public void error(String msg) {
        logger.error(msg);
    }

    public void error(String format, Object arg) {
        logger.error(format,arg);
    }

    public void error(String format, Object arg1, Object arg2) {
        logger.error(format,arg1,arg2);
    }

    public void error(String format, Object... arguments) {
        logger.error(format,arguments);
    }

    public void error(String msg, Throwable t) {
        logger.error(msg,t);
    }

    public boolean isErrorEnabled(Marker marker) {
        return false;
    }

    public void error(Marker marker, String msg) {

    }

    public void error(Marker marker, String format, Object arg) {

    }

    public void error(Marker marker, String format, Object arg1, Object arg2) {

    }

    public void error(Marker marker, String format, Object... arguments) {

    }

    public void error(Marker marker, String msg, Throwable t) {

    }
}
