package com.mystudy.web.controller;

import com.mystudy.web.BaseJunit4Test;
import com.mystudy.web.model.Goods;
import com.mystudy.web.service.GoodsService;
import junit.framework.TestCase;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * Created by 程祥 on 16/1/8.
 * Function：
 */
public class IndexControllerTest extends BaseJunit4Test {

    @Autowired
    private GoodsService goodsService;

    @Test
//    @Transactional(rollbackFor = Exception.class)
    public void testBf() throws Exception{
//        Goods goods = goodsService.selectById("1");
        ThreadPoolExecutor voidExecutor = new ThreadPoolExecutor(
                10, 100, 4, TimeUnit.SECONDS, new SynchronousQueue<Runnable>());
        TestWorker t1 = new TestWorker(goodsService);
        for(int i=0;i<3;i++){
            voidExecutor.execute(t1);
        }
        TestWorker t2 = new TestWorker(goodsService);
        TestWorker t3 = new TestWorker(goodsService);
        TestWorker t4 = new TestWorker(goodsService);
        TestWorker t5 = new TestWorker(goodsService);
        TestWorker t6 = new TestWorker(goodsService);
        TestWorker t7 = new TestWorker(goodsService);
        TestWorker t8 = new TestWorker(goodsService);
        TestWorker t9 = new TestWorker(goodsService);
        TestWorker t10 = new TestWorker(goodsService);

//        t1.run();
//        t2.run();
//        t3.run();
//        t4.run();
//        t5.run();
//        t6.run();
//        t7.run();
//        t8.run();
//        t9.run();
//        t10.run();

        System.out.println("*********************");
    }


    private class TestWorker implements Runnable{

        private GoodsService service;

        public TestWorker(GoodsService service){
            this.service = service;
        }

        @Override
        public void run() {
            try {
                service.updateGoods();
            } catch (Exception e) {
                System.out.println(e.getMessage());
                e.printStackTrace();

            }
        }
    }


}