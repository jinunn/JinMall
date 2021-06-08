package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.BrandEntity;

import java.util.Map;

/**
 * 品牌
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
public interface BrandService extends IService<BrandEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 修改品牌名称 关联表也要修改
     * @param brand 品牌id
     */
    void updateAll(BrandEntity brand);
}

