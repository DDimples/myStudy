package com.mystudy.web.controller;

import com.alibaba.fastjson.JSON;
import com.mystudy.web.common.BaseController;
import com.mystudy.web.common.log.LogUtil;
import com.mystudy.web.controller.util.MultithreadUtil;
import com.mystudy.web.model.Goods;
import com.mystudy.web.service.GoodsService;
import com.mystudy.web.service.TestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.util.StopWatch;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Created by 程祥 on 15/11/26.
 * Function：
 */

@Controller
public class IndexController extends BaseController {
    private final Logger logger = LogUtil.getCommonLogger();
    private  final Logger testLogger = LogUtil.getTestLogger();

    @Autowired
    private TestService testService;

    @Autowired
    private GoodsService goodsService;

    @RequestMapping(value = "/test2")
    public ModelAndView index(){
        logger.debug("index ********");
        ModelAndView mv = new ModelAndView("test2");
        mv.addObject("test","测试~~args~");
        return mv;
    }

    @RequestMapping(value = "/index")
    public ModelAndView index2(){
        logger.debug("index2 ********");
        ModelAndView mv = new ModelAndView("test");
        mv.addObject("test", "测试~~~");
        mv.addObject("title","测试title");
        return mv;
    }

    @RequestMapping(value = "/test",produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
    @ResponseBody
    public Object index3(String name){
        Goods goods = new Goods();
        goods.setNum(100.00);
        goods.setName(name);
        goods.setPrice(new BigDecimal(Math.random() * 100));
        try {
            goodsService.insert(goods);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return goods.toString();
    }

    @RequestMapping(value = "/database",produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object databaseTest() throws Exception{
        return JSON.toJSONString(goodsService.selectAll());
    }

    @RequestMapping(value = "/bf",produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object testBf(){
        ThreadPoolExecutor voidExecutor = new ThreadPoolExecutor(
                10, 100, 4, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        TestWorker t1 = new TestWorker(goodsService);
        TestWorker t2 = new TestWorker(goodsService);
        TestWorker t3 = new TestWorker(goodsService);
        TestWorker t4 = new TestWorker(goodsService);
        TestWorker t5 = new TestWorker(goodsService);
        for(int i=0;i<10;i++){
            voidExecutor.execute(t1);
            voidExecutor.execute(t2);
            voidExecutor.execute(t3);
            voidExecutor.execute(t4);
            voidExecutor.execute(t5);
        }

        voidExecutor.shutdown();
        return "success 1 测试并发";
    }

    @RequestMapping(value = "/bf2",produces = "application/json; charset=UTF-8")
    @ResponseBody
    public Object testBf2(){
        ThreadPoolExecutor voidExecutor = new ThreadPoolExecutor(
                10, 100, 4, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        TestWorker2 t1 = new TestWorker2(goodsService);
        TestWorker2 t2 = new TestWorker2(goodsService);
        TestWorker2 t3 = new TestWorker2(goodsService);
        TestWorker2 t4 = new TestWorker2(goodsService);
        TestWorker2 t5 = new TestWorker2(goodsService);
        for(int i=0;i<10;i++){
            voidExecutor.execute(t1);
            voidExecutor.execute(t2);
            voidExecutor.execute(t3);
            voidExecutor.execute(t4);
            voidExecutor.execute(t5);
        }

//        t3.run();
        voidExecutor.shutdown();
        return "success 测试并发";
    }

    private class TestWorker implements Runnable{

        private GoodsService service;

        public TestWorker(GoodsService service){
            this.service = service;
        }

        @Override
        public void run() {
            try {
                service.updateGoods3();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();

            }
        }
    }

    private class TestWorker2 implements Runnable{

        private GoodsService service;

        public TestWorker2(GoodsService service){
            this.service = service;
        }

        @Override
        public void run() {
            try {
                service.updateGoods2();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();

            }
        }
    }
}
