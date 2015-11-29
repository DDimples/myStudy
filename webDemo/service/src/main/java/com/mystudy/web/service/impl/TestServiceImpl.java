package com.mystudy.web.service.impl;

import com.mystudy.web.service.TestService;
import org.springframework.stereotype.Service;

/**
 * Created by 程祥 on 15/11/29.
 * Function：
 */
@Service
public class TestServiceImpl implements TestService{

    public String testMethod(String name) throws Exception {
        System.out.println("sleep 200 ms");
        Thread.sleep(200);
        return name+"service return value";
    }
}
