package com.jinunn.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.ware.entity.WareSkuEntity;
import com.jinunn.mall.ware.vo.SkuHasStockVo;

import java.util.List;
import java.util.Map;

/**
 * 商品库存
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:43:13
 */
public interface WareSkuService extends IService<WareSkuEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void addStock(Integer skuNum, Long skuId, Long wareId);

    /**
     * 查询商品是否有库存
     * @param skuIds 商品id
     * @return 商品库存信息
     */
    List<SkuHasStockVo> getSkuHasStock(List<Long> skuIds);
}

