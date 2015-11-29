package com.mystudy.web.common.aop;

import com.mystudy.web.common.log.model.LogInfo;
import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.common.util.DateUtil;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Date;

/**
 * Created by 程祥 on 15/11/29.
 * Function：
 */
@Aspect
@Component
public class ServiceAop {

    private static Logger logger = LogUtil.getCheckListLogger();


    //由于是框架通用的，所以需要在config中配置，无法使用注解配置
//    @Around(value = "execution(* com.mystudy.web.service)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable{
        long startTime =System.currentTimeMillis();
        LogInfo logInfo = new LogInfo();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Object result = null;
        try {
            result = joinPoint.proceed();
        } catch (Throwable throwable) {
            logInfo.setException(throwable);
            logInfo.setExceptionMsg(throwable.getMessage());
        }
        long useTime =System.currentTimeMillis() - startTime;

        logInfo.setElapsedTime(useTime+"");
        logInfo.setLogTime(DateUtil.formatDate(new Date(), "yyyy-MM-dd HH:mm:ss.SSS"));
        logInfo.setServiceName(method.getName());
        logInfo.setLogType("0001");
        logInfo.setAppName("webDemo");
        logger.debug(logInfo.toString());
        return result;
    }



}
