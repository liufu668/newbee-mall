package com.study.newbeeMall.api;

import com.study.newbeeMall.api.vo.IndexCategoryVO;
import com.study.newbeeMall.common.NewbeeMallException;
import com.study.newbeeMall.common.ServiceResultEnum;
import com.study.newbeeMall.service.GoodsCategoryService;
import com.study.newbeeMall.util.Result;
import com.study.newbeeMall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

/**
 * 分类
 */
@RestController
@Api(value = "v1", tags = "新蜂商城分类页面接口")
public class GoodsCategoryAPI {

    @Resource
    private GoodsCategoryService goodsCategoryService;

    @GetMapping("/categories")
    @ApiOperation(value = "获取分类数据", notes = "分类页面使用")
    public Result<List<IndexCategoryVO>> getCategories(){
        List<IndexCategoryVO> categories = goodsCategoryService.getCategoriesForIndex();
        if(CollectionUtils.isEmpty(categories)){
            NewbeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return ResultGenerator.genSuccessResult(categories);
    }
}
