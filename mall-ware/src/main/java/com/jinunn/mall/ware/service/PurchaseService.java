package com.jinunn.mall.ware.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.ware.entity.PurchaseEntity;

import java.util.Map;

/**
 * 采购信息
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:43:13
 */
public interface PurchaseService extends IService<PurchaseEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询未领取的采购单
     * @param params 查询条件
     * @return 未领取的采购单
     */
    PageUtils queryPageUnaccalimed(Map<String, Object> params);
}

