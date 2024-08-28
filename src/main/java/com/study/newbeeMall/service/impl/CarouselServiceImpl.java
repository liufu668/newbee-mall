package com.study.newbeeMall.service.impl;

import com.study.newbeeMall.api.vo.IndexCarouselVO;
import com.study.newbeeMall.dao.CarouselMapper;
import com.study.newbeeMall.entity.Carousel;
import com.study.newbeeMall.service.CarouselService;
import com.study.newbeeMall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;

/**
 *
 */
@Service
public class CarouselServiceImpl implements CarouselService {

    @Autowired
    private CarouselMapper carouselMapper;

    /**
     * 返回固定数量的轮播图对象供应首页渲染
     */
    @Override
    public List<IndexCarouselVO> getCarouselsForIndex(int number) {

        List<IndexCarouselVO> indexCarouselVOS = new ArrayList<>(number);
        List<Carousel> carousels = carouselMapper.findCarouselsByNum(number);
        if(!CollectionUtils.isEmpty(carousels)){
            //将查询出来的轮播图对象转换为视图层对象IndexCarouselVO
            indexCarouselVOS = BeanUtil.copyList(carousels, IndexCarouselVO.class);
        }
        return indexCarouselVOS;
    }
}




