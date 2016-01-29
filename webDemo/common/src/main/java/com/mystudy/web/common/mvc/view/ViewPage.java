package com.mystudy.web.common.mvc.view;

import java.io.IOException;

import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.view.freemarker.FreeMarkerView;

import freemarker.template.SimpleHash;
import freemarker.template.Template;
import freemarker.template.TemplateException;

public class ViewPage extends FreeMarkerView{

	@Override
	protected void processTemplate(Template template, SimpleHash model, HttpServletResponse response)
			throws IOException, TemplateException {
    
		template.process(model, response.getWriter());
	
	
	}
	
}
