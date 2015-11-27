package com.mystudy.web.common.log;

import org.slf4j.Logger;

/**
 * Created by 程祥 on 15/11/27.
 * Function：记录日志的公共方法
 */
public class LogUtil {
    private static final Logger commonLogger = new LogWrapper("CommonLogger");
    private static final Logger testLogger = new LogWrapper("TestLogger");

    public static Logger getCommonLogger(){
        return commonLogger;
    }

    public static Logger getTestLogger(){
        return testLogger;
    }


}
