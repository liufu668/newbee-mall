package com.study.newbeeMall.dao;

import com.study.newbeeMall.entity.IndexConfig;

import java.util.List;

public interface IndexConfigMapper {

    List<IndexConfig> findIndexConfigsByTypeAndNum(int configType, int number);
}




