package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.ProductAttrValueEntity;

import java.util.Map;

/**
 * spu属性值
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
public interface ProductAttrValueService extends IService<ProductAttrValueEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

