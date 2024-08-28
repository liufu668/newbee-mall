package com.study.newbeeMall.api;

import com.study.newbeeMall.api.vo.IndexCarouselVO;
import com.study.newbeeMall.api.vo.IndexConfigGoodsVO;
import com.study.newbeeMall.api.vo.IndexInfoVO;
import com.study.newbeeMall.common.Constants;
import com.study.newbeeMall.common.IndexConfigTypeEnum;
import com.study.newbeeMall.service.CarouselService;
import com.study.newbeeMall.service.IndexConfigService;
import com.study.newbeeMall.util.Result;
import com.study.newbeeMall.util.ResultGenerator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@Api(value = "v1", tags = "新蜂商城首页接口")
@RequestMapping("/api/v1")
public class IndexAPI {

    //轮播图
    @Resource
    private CarouselService carouselService;

    //首页配置
    @Resource
    private IndexConfigService indexConfigService;

    @GetMapping("/index-infos")
    @ApiOperation(value = "获取首页数据", notes = "轮播图,新品,推荐等")
    public Result<IndexInfoVO> indexInfo(){
        IndexInfoVO indexInfoVO = new IndexInfoVO();

        List<IndexCarouselVO> carousels = carouselService.getCarouselsForIndex(Constants.INDEX_CAROUSEL_NUMBER);
        //查询热销商品
        List<IndexConfigGoodsVO> hotGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_HOT.getType(), Constants.INDEX_GOODS_HOT_NUMBER);
        //查询新品推荐
        List<IndexConfigGoodsVO> newGoodses = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_NEW.getType(), Constants.INDEX_GOODS_NEW_NUMBER);
        //查询推荐商品
        List<IndexConfigGoodsVO> recommendGoodes = indexConfigService.getConfigGoodsesForIndex(IndexConfigTypeEnum.INDEX_GOODS_RECOMMOND.getType(), Constants.INDEX_GOODS_RECOMMOND_NUMBER);

        indexInfoVO.setCarousels(carousels);
        indexInfoVO.setHotGoodses(hotGoodses);
        indexInfoVO.setNewGoodses(newGoodses);
        indexInfoVO.setRecommendGoodses(recommendGoodes);

        return ResultGenerator.genSuccessResult(indexInfoVO);
    }

}
