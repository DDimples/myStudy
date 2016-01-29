package com.mystudy.web.common.mvc.view;

import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

public class ViewPageResolver extends FreeMarkerViewResolver {

	@Override
	protected void initApplicationContext() {

		super.initApplicationContext();
		this.setAttributesMap(ViewStaticMethod.getStaticList());
       
	}

}
