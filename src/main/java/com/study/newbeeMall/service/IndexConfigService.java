package com.study.newbeeMall.service;


import com.study.newbeeMall.api.vo.IndexConfigGoodsVO;

import java.util.List;

public interface IndexConfigService{
    List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int type, int indexGoodsHotNumber);
}
