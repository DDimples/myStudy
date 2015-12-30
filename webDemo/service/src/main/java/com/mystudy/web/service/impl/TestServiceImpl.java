package com.mystudy.web.service.impl;

import com.mystudy.web.service.TestService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

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

    @Async
    public Future<String> testMultithreading(){
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return new AsyncResult<String>("testMultithreading");
    }
}
