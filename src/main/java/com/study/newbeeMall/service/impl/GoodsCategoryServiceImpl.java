package com.study.newbeeMall.service.impl;

import com.study.newbeeMall.api.vo.IndexCategoryVO;
import com.study.newbeeMall.api.vo.SecondLevelCategoryVO;
import com.study.newbeeMall.api.vo.ThirdLevelCategoryVO;
import com.study.newbeeMall.common.CategoryLevelEnum;
import com.study.newbeeMall.common.Constants;
import com.study.newbeeMall.dao.GoodsCategoryMapper;
import com.study.newbeeMall.entity.GoodsCategory;
import com.study.newbeeMall.service.GoodsCategoryService;
import com.study.newbeeMall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.groupingBy;

@Service
public class GoodsCategoryServiceImpl implements GoodsCategoryService {

    @Autowired
    private GoodsCategoryMapper goodsCategoryMapper;

    @Override
    public List<IndexCategoryVO> getCategoriesForIndex() {

        List<IndexCategoryVO> indexCategoryVOS = new ArrayList<>();

        //获取一级分类的固定数量的数据
        List<GoodsCategory> firstLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(Collections.singletonList(0L), CategoryLevelEnum.LEVEL_ONE.getLevel(), Constants.INDEX_CATEGORY_NUMBER);
        if(!CollectionUtils.isEmpty(firstLevelCategories)){
            List<Long> firstLevelCategoryIds = firstLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());

            //获取二级分类的数据
            List<GoodsCategory> secondLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(firstLevelCategoryIds, CategoryLevelEnum.LEVEL_TWO.getLevel(), 0);
            if(!CollectionUtils.isEmpty(secondLevelCategories)){
                List<Long> secondeLevelCategoryIds = secondLevelCategories.stream().map(GoodsCategory::getCategoryId).collect(Collectors.toList());

                //获取三级分类的数据
                List<GoodsCategory> thirdLevelCategories = goodsCategoryMapper.selectByLevelAndParentIdsAndNumber(secondeLevelCategoryIds, CategoryLevelEnum.LEVEL_THREE.getLevel(), 0);
                if(!CollectionUtils.isEmpty(thirdLevelCategories)){

                    //根据parentId将thirdLevelCategories分组
                    Map<Long, List<GoodsCategory>> thirdLevelCategoryMap = thirdLevelCategories.stream().collect(groupingBy(GoodsCategory::getParentId));
                    List<SecondLevelCategoryVO> secondLevelCategoryVOS = new ArrayList<>();

                    //处理二级分类
                    for(GoodsCategory secondLevelCategory: secondLevelCategories){
                        SecondLevelCategoryVO secondLevelCategoryVO = new SecondLevelCategoryVO();
                        BeanUtil.copyProperties(secondLevelCategory, secondLevelCategoryVO);
                        //如果二级分类下有数据,则放入secondLevelCategoryVOS对象中
                        if(thirdLevelCategoryMap.containsKey(secondLevelCategory.getCategoryId())){
                            //根据二级分类的id取出 thirdLevelCategoryMap分组中的三级分类列表
                            List<GoodsCategory> tempGoodsCategories = thirdLevelCategoryMap.get(secondLevelCategory.getCategoryId());
                            secondLevelCategoryVO.setThirdLevelCategoryVOS(BeanUtil.copyList(tempGoodsCategories, ThirdLevelCategoryVO.class));
                            secondLevelCategoryVOS.add(secondLevelCategoryVO);
                        }
                    }

                    //处理一级分类
                    if (!CollectionUtils.isEmpty(secondLevelCategoryVOS)){
                        //根据parentId将thirdLevelCategories分组
                        Map<Long, List<SecondLevelCategoryVO>> secondLevelCategoryVOMap = secondLevelCategoryVOS.stream().collect(groupingBy(SecondLevelCategoryVO::getParentId));
                        for(GoodsCategory firstCategory: firstLevelCategories){
                            IndexCategoryVO indexCategoryVO = new IndexCategoryVO();
                            BeanUtil.copyProperties(firstCategory, indexCategoryVO);
                            //如果一级分类下有数据,则放入IndexCategoryVOS对象中
                            if(secondLevelCategoryVOMap.containsKey(firstCategory.getCategoryId())){
                                //根据一级分类的id取出secondLevelCategoryVOMap分组中的二级分类列表
                                List<SecondLevelCategoryVO> tempGoodsCategories = secondLevelCategoryVOMap.get(firstCategory.getCategoryId());
                                indexCategoryVO.setSecondLevelCategoryVOS(tempGoodsCategories);
                                indexCategoryVOS.add(indexCategoryVO);

                            }
                        }

                    }

                }
            }
            return indexCategoryVOS;
        }else{
            return null;
        }
    }
}




