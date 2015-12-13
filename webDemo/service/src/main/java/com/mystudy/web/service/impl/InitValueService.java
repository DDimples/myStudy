package com.mystudy.web.service.impl;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

/**
 * Created by 程祥 on 15/12/13.
 * Function：
 */
@Component
public class InitValueService implements InitializingBean, DisposableBean {


    public String test(){return "InitValueService";}

    public void destroy() throws Exception {
        //容器关闭时操作
    }

    public void afterPropertiesSet() throws Exception {
        //容器初始化后
        System.out.println("afterPropertiesSet");

    }
}
