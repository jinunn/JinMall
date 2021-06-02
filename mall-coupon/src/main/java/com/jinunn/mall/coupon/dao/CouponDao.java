package com.jinunn.mall.coupon.dao;

import com.jinunn.mall.coupon.entity.CouponEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 优惠券信息
 * 
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:17:32
 */
@Mapper
public interface CouponDao extends BaseMapper<CouponEntity> {
	
}
