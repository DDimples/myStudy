package com.mystudy.web.service.impl;

import com.mystudy.web.mapper.GoodsMapper;
import com.mystudy.web.model.Goods;
import com.mystudy.web.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by 程祥 on 15/12/21.
 * Function：
 */
@Service
@Transactional
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsDao;

    public List<Goods> selectAll() throws Exception {
        return goodsDao.selectAll();
    }

    @Transactional
    public Goods selectById(String id) throws Exception {
        return goodsDao.selectById(id);
    }

    @Transactional
    public int insertGoods(List<Goods> list) throws Exception {
        return goodsDao.insertGoods(list);
    }



    @Transactional(rollbackFor = Exception.class)
    public int updateGoods() throws Exception{
        System.out.println("开始："+Thread.currentThread().getId()+"service"+this);
        try{
            Goods goods = selectById("1");
            goods.setNum(goods.getNum() + 2);
            System.out.println(goods.getNum() + "updateGoods1*********" + Thread.currentThread().getName());
            Thread.sleep(100);
            return goodsDao.updateGoods(goods);
        }finally{
            System.out.println("结束："+Thread.currentThread().getId()+"service"+this);
        }
    }


    private Set<String> idLock = Collections.synchronizedSet(new HashSet<String>());

    public int updateGoods3() throws Exception{
        while (true){
            synchronized (idLock){
                if(idLock.add("1")){
                    try {
                        Goods goods = selectById("1");
                        goods.setNum(goods.getNum() + 2);
                        System.out.println(goods.getNum() + "updateGoods2*********" + Thread.currentThread().getName());
                        Thread.sleep(100);
                        return goodsDao.updateGoods(goods);
                    }catch (Exception e){
                        break;
                    }finally {
                        idLock.remove("1");
                        synchronized (this){
                            this.notifyAll();
                        }
                    }
                }else {
                    synchronized (this){
                        try{
                            this.wait();
                        }catch (Exception e){
                            break;
                        }
                    }
                }

            }
        }
        return 1;
    }


    @Transactional(readOnly = false ,rollbackFor = Exception.class)
    public synchronized int updateGoods2() throws Exception{
        System.out.println("开始："+Thread.currentThread().getId()+"service"+this);
        try{
            Goods goods = selectById("1");
            goods.setNum(goods.getNum() + 2);
            System.out.println(goods.getNum() + "updateGoods2*********" + Thread.currentThread().getName());
            Thread.sleep(100);
            return goodsDao.updateGoods(goods);
        }finally{
            System.out.println("结束：" + Thread.currentThread().getId() + "service" + this);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(Goods goods) throws Exception{

        goodsDao.insert(goods);

    }
}
