package com.mystudy.web.service.impl;

import com.mystudy.web.mapper.GoodsMapper;
import com.mystudy.web.model.Goods;
import com.mystudy.web.service.GoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by 程祥 on 15/12/21.
 * Function：
 */
@Service
public class GoodsServiceImpl implements GoodsService {

    @Autowired
    private GoodsMapper goodsDao;

    public List<Goods> selectAll() throws Exception {
        return goodsDao.selectAll();
    }

    public Goods selectById(String id) throws Exception {
        return goodsDao.selectById(id);
    }

    public int insertGoods(List<Goods> list) throws Exception {
        return goodsDao.insertGoods(list);
    }
}
