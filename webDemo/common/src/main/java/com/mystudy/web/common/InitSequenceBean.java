package com.mystudy.web.common;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * Created by 程祥 on 15/12/24.
 * Function：测试  InitializingBean、init-method/destroy-method、@PostConstruct/@PreDestroy注解
 */
//@Component 在xml中配置了就不需要再加Component
public class InitSequenceBean implements InitializingBean{

    private String testInitValue;

    public String getTestInitValue() {
        return testInitValue;
    }

    public void setTestInitValue(String testInitValue) {
        this.testInitValue = testInitValue;
    }

    public InitSequenceBean() {
        System.out.println("InitSequenceBean constructor");
    }

    public void afterPropertiesSet() throws Exception {
        System.out.println("InitSequenceBean afterPropertiesSet");
    }

    @PostConstruct
    public void initMethodByAnnotation(){
        System.out.println("InitSequenceBean postConstruct");
    }

    public void initMethodByXml(){
        System.out.println("InitSequenceBean by xml");
    }
}
