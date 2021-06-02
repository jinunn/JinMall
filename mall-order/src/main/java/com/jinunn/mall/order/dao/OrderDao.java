package com.jinunn.mall.order.dao;

import com.jinunn.mall.order.entity.OrderEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 订单
 * 
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:38:17
 */
@Mapper
public interface OrderDao extends BaseMapper<OrderEntity> {
	
}
