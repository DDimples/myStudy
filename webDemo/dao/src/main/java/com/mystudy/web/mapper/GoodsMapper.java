package com.mystudy.web.mapper;

import com.mystudy.web.model.Goods;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 程祥 on 15/12/21.
 * Function：
 */
@Repository
public interface GoodsMapper {

    List<Goods> selectAll();

    Goods selectById(String id);

    int insertGoods(List<Goods> list);

    int updateGoods(Goods goods);

    void insert(Goods obj);

}
