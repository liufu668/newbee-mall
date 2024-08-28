package com.study.newbeeMall.service.impl;

import com.study.newbeeMall.api.vo.IndexConfigGoodsVO;
import com.study.newbeeMall.dao.GoodsMapper;
import com.study.newbeeMall.dao.IndexConfigMapper;
import com.study.newbeeMall.entity.Goods;
import com.study.newbeeMall.entity.IndexConfig;
import com.study.newbeeMall.service.IndexConfigService;
import com.study.newbeeMall.util.BeanUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class IndexConfigServiceImpl implements IndexConfigService {

    @Autowired
    private IndexConfigMapper indexConfigMapper;

    @Autowired
    private GoodsMapper goodsMapper;

    /**
     * 返回固定数量的配置项对象供首页数据渲染
     */@Override
    public List<IndexConfigGoodsVO> getConfigGoodsesForIndex(int configType, int number) {

        // 创建一个指定大小的列表来存储结果
        List<IndexConfigGoodsVO> indexConfigGoodsVOS = new ArrayList<>(number);

        // 根据类型和数量从数据库中查询 IndexConfig 对象列表
        List<IndexConfig> indexConfigs = indexConfigMapper.findIndexConfigsByTypeAndNum(configType, number);

        // 如果查询结果不为空
        if(!CollectionUtils.isEmpty(indexConfigs)){
            // 提取所有的 goodsId
            List<Long> goodsIds = indexConfigs.stream().map(IndexConfig::getGoodsId).collect(Collectors.toList());

            // 根据 goodsId 查询 Goods 对象列表
            List<Goods> goods = goodsMapper.selectByPrimaryKeys(goodsIds);

            // 将 Goods 对象列表转换为 IndexConfigGoodsVO 对象列表
            indexConfigGoodsVOS = BeanUtil.copyList(goods, IndexConfigGoodsVO.class);

            // 遍历结果列表，对每个 IndexConfigGoodsVO 对象的商品名称进行处理
            for(IndexConfigGoodsVO indexConfigGoodsVO: indexConfigGoodsVOS){
                String goodsName = indexConfigGoodsVO.getGoodsName();

                // 如果商品名称长度超过30个字符，则进行截断，并添加省略号
                if(goodsName.length() > 30){
                    goodsName = goodsName.substring(0, 30) + "...";
                    indexConfigGoodsVO.setGoodsName(goodsName);
                }
            }
        }

        // 返回处理后的 IndexConfigGoodsVO 列表
        return indexConfigGoodsVOS;
    }
}




