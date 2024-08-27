package com.study.newbeeMall.dao;

import com.study.newbeeMall.entity.Carousel;


import java.util.List;


public interface CarouselMapper {

    List<Carousel> findCarouselsByNum(int number);
}




