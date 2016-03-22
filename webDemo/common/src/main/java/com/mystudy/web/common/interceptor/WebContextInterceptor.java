package com.mystudy.web.common.interceptor;

import com.mystudy.web.common.log.LogUtil;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by 程祥 on 15/11/27.
 * Function：
 */
public class WebContextInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        request.setAttribute("startTime",System.currentTimeMillis());

        return super.preHandle(request, response, handler);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {
        super.postHandle(request, response, handler, modelAndView);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        super.afterCompletion(request, response, handler, ex);
        afterActionLog(request,handler);
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        super.afterConcurrentHandlingStarted(request, response, handler);
    }

    private void afterActionLog(HttpServletRequest request, Object handler) {
        long startTime = (Long) request.getAttribute("startTime");
        long elapsedTime = System.currentTimeMillis() - startTime;
        String methodName = "";
        if (handler instanceof HandlerMethod) {
            HandlerMethod method = (HandlerMethod) handler;
            methodName = method.getBean().getClass().getSimpleName() + "."
                    + method.getMethod().getName();
        }
//        LogUtil.getCommonLogger().debug("方法执行时间："+ elapsedTime+"--方法名称："+methodName);

        System.out.println("方法执行时间："+ elapsedTime+"--方法名称："+methodName);

    }
}
