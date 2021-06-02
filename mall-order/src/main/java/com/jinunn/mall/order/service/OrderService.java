package com.jinunn.mall.order.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.order.entity.OrderEntity;

import java.util.Map;

/**
 * 订单
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:38:17
 */
public interface OrderService extends IService<OrderEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

