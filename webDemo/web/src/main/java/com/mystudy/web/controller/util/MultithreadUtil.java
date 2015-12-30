package com.mystudy.web.controller.util;

import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.model.Persion;
import com.mystudy.web.service.AsyncTestService;
import com.mystudy.web.service.TestService;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * Created by 程祥 on 15/12/21.
 * Function：
 */
public class MultithreadUtil implements Runnable{

    private AsyncTestService service;

    private String name;

    private String result;

    public MultithreadUtil(AsyncTestService service,String name) {
        this.service = service;
        this.name = name;
    }

    @Override
    public void run() {
        result = service.syncTest(name);
    }

    public String getResult() {
        return result;
    }
}
