package com.study.newbeeMall.service;

import com.study.newbeeMall.api.vo.IndexCarouselVO;
import com.study.newbeeMall.entity.Carousel;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
 *
 */
public interface CarouselService{

    //返回固定数量的轮播图对象(首页调用)
    List<IndexCarouselVO> getCarouselsForIndex(int indexCarouselNumber);
}
