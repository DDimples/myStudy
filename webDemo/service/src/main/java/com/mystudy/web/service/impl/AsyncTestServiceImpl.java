package com.mystudy.web.service.impl;

import com.mystudy.web.model.Persion;
import com.mystudy.web.service.AsyncTestService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.util.concurrent.Future;

/**
 * Created by 程祥 on 15/12/22.
 * Function：
 */
@Service
public class AsyncTestServiceImpl implements AsyncTestService {

    @Async
    public void voidAsyncTest() {
        System.out.println(System.currentTimeMillis());
    }

    @Async
    public Future<String> asyncTest(String name) {
        try {
            //模仿接口处理，需要耗时两秒的接口
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        String result = name+"_"+Thread.currentThread().getName();
        return new AsyncResult<String>(result);
    }

    public String syncTest(String name) {
        try {
            //模仿接口处理，需要耗时两秒的接口
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return name+"_"+Thread.currentThread().getName();
    }
}
