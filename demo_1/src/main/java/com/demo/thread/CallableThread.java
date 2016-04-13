package com.demo.thread;

import java.util.concurrent.Callable;

/**
 * Created by 程祥 on 16/3/31.
 * Function：
 */
public class CallableThread implements Callable<String> {

    private String a;

    public CallableThread(String a) {
        this.a = a;
    }

    @Override
    public String call() throws Exception {

        return a+"call_"+Thread.currentThread().getId();
    }
}
