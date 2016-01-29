package com.mystudy.web.common.mvc.view;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.mystudy.web.common.util.DebugUtil;
import com.mystudy.web.common.util.UrlUtil;
import freemarker.ext.beans.BeansWrapper;
import freemarker.template.TemplateModelException;
@SuppressWarnings("rawtypes")
public class ViewStaticMethod {

	
	private static Class[] defaultStaticClasses = { UrlUtil.class,
			DebugUtil.class,URLEncoder.class,URLDecoder.class};


	public static Map<String, Object> getStaticList() {

		Map<String, Object> methodList = new HashMap<String, Object>();

		for (Class clz : defaultStaticClasses) {
			String name = clz.getSimpleName();
			methodList.put(name, getStaticModel(clz));
		}
		return methodList;
	}
	
	 
    private static Object getStaticModel(Class clz) {  
        BeansWrapper wrapper = BeansWrapper.getDefaultInstance();  
        try {  
            return wrapper.getStaticModels().get(clz.getName());  
        } catch (TemplateModelException e) {  
            //e.printStackTrace();  
        }  
        return null;  
    }  

}
