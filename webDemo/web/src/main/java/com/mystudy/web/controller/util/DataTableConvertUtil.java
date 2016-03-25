package com.mystudy.web.controller.util;

import com.mystudy.web.model.request.DataTableEditRequest;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Field;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by 程祥 on 16/3/25.
 * Function：
 */
public class DataTableConvertUtil {

    public static <T>DataTableEditRequest<T> converRest(HttpServletRequest request,Class<T> c) throws IllegalAccessException, InstantiationException, NoSuchFieldException {
        DataTableEditRequest<T> paramObj = new DataTableEditRequest();
        Map map = request.getParameterMap();
        Set<String> set = map.keySet();
        Iterator<String> it = set.iterator();

        String p_str = "data\\[(\\w+)\\]\\[(\\w+)\\]";
        Pattern pattern = Pattern.compile(p_str);

        List<T> requestData = new ArrayList<T>();
        Map<String,T> tempMap = new HashMap<String, T>();
        while (it.hasNext()){
            String param = it.next();
            Matcher matcher = pattern.matcher(param);
            if(matcher.matches()){
                String id = matcher.group(1);
                String fieldName = matcher.group(2);
                T t = tempMap.get(id);
                if(null == t){
                    t = c.newInstance();
                    tempMap.put(id,t);
                    Field fieldObj=c.getDeclaredField("id");
                    fieldObj.set(t, id);
                }
                Field fieldObj=c.getDeclaredField(fieldName);
                fieldObj.set(t,request.getParameter(param));
            }else {
                System.out.println("NO MATCH"+param);
            }
        }
        paramObj.setAction(request.getParameter("action"));
        Iterator<String> iterator = tempMap.keySet().iterator();
        while (iterator.hasNext()){
            String key = iterator.next();
            T t = tempMap.get(key);
            requestData.add(t);
        }
        paramObj.setDataList(requestData);
        return paramObj;
    }



}
