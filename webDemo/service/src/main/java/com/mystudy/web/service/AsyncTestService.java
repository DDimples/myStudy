package com.mystudy.web.service;

import com.mystudy.web.model.Persion;

import java.util.concurrent.Future;

/**
 * Created by 程祥 on 15/12/22.
 * Function：spring Async 异步服务类
 */
public interface AsyncTestService {

    void voidAsyncTest();

    Future<String> asyncTest(String name);

    //这个不是异步的
    String syncTest(String name);

}
