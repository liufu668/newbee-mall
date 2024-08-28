package com.study.newbeeMall.dao;

import com.study.newbeeMall.entity.Goods;

import java.util.List;

public interface GoodsMapper {

    List<Goods> selectByPrimaryKeys(List<Long> goodsIds);
}




