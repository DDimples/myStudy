package com.mystudy.web.common.interceptor;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mystudy.web.common.log.LogUtil;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;


public class WebExceptionResolver implements HandlerExceptionResolver {

	public ModelAndView resolveException(HttpServletRequest request,
			HttpServletResponse response, Object handler, Exception ex) {
		// TODO Auto-generated method stub
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw); 
		ex.printStackTrace(pw);
		response.setStatus(500);
		//ex.printStackTrace();
		LogUtil.getCommonLogger().error("MVC Error "+ex.getMessage(),ex);
		ModelAndView mv = new ModelAndView("common/error");
		mv.addObject("exception", sw.toString().replaceAll("\n", "<br/>"));
		return mv;
		
	}

}
