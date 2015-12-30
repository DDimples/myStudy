package com.mystudy.web.controller;

import com.alibaba.fastjson.JSON;
import com.mystudy.web.common.BaseController;
import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.controller.util.MultithreadUtil;
import com.mystudy.web.model.Persion;
import com.mystudy.web.service.AsyncTestService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by 程祥 on 15/12/22.
 * Function：
 */
@Controller
public class AsyncController extends BaseController{

    @Autowired
    private AsyncTestService asyncTestService;

    @RequestMapping(value = RouteKey.ASYNCTEST)
    @ResponseBody
    public Object testAsync(){
        List<String> result = new ArrayList<String>(10);
        StopWatch sp = new StopWatch();
        sp.start();
        List<Future<String>> futures = new ArrayList<Future<String>>(10);
        for (int i=0;i<10;i++){
            futures.add(asyncTestService.asyncTest(i+""));
        }
        try {
            for (Future<String> future:futures){
                //future.get()会阻塞
                System.out.println("future.get()开始时间"+System.currentTimeMillis());
                result.add(future.get());
                System.out.println("future.get()结束时间"+System.currentTimeMillis());
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        sp.stop();
        System.out.println(sp.getTotalTimeSeconds());

        return JSON.toJSONString(result)+"_结果:"+sp.getTotalTimeSeconds();
    }


    @RequestMapping(value = RouteKey.MULTYTEST)
    @ResponseBody
    public Object testMultithreading(){
        List<String> result = new ArrayList<String>(10);
        StopWatch sp = new StopWatch();
        sp.start();
        ThreadPoolExecutor voidExecutor = new ThreadPoolExecutor(
                5, 10, 4, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());

        MultithreadUtil util = new MultithreadUtil(asyncTestService,"testMulti");
        voidExecutor.execute(util);
        MultithreadUtil util2 = new MultithreadUtil(asyncTestService,"testMulti");
        voidExecutor.execute(util2);
        MultithreadUtil util3 = new MultithreadUtil(asyncTestService,"testMulti");
        voidExecutor.execute(util3);
        MultithreadUtil util4 = new MultithreadUtil(asyncTestService,"testMulti");
        voidExecutor.execute(util4);
        MultithreadUtil util5 = new MultithreadUtil(asyncTestService,"testMulti");
        voidExecutor.execute(util5);
        MultithreadUtil util6 = new MultithreadUtil(asyncTestService,"testMulti");
        voidExecutor.execute(util6);
        //等待service执行
        while(util.getResult()==null
                ||util2.getResult()==null||
                util3.getResult()==null||
                util4.getResult()==null||
                util5.getResult()==null||
                util6.getResult()==null){
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                LogUtil.getCommonLogger().error(e.getMessage(), e);
            }
        }
        result.add(util.getResult());
        result.add(util2.getResult());
        result.add(util3.getResult());
        result.add(util4.getResult());
        result.add(util5.getResult());
        result.add(util6.getResult());
        voidExecutor.shutdown();
        sp.stop();
        System.out.println(sp.getTotalTimeSeconds());
        return JSON.toJSONString(result)+"_结果:"+sp.getTotalTimeSeconds();
    }



}
