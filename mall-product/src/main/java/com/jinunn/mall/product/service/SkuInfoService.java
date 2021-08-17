package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.SkuInfoEntity;
import com.jinunn.mall.product.entity.SpuInfoEntity;

import java.util.List;
import java.util.Map;

/**
 * sku信息
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
public interface SkuInfoService extends IService<SkuInfoEntity> {

    PageUtils queryPage(Map<String, Object> params);

    List<SkuInfoEntity> getSkuByspuId(Long spuId);
}

