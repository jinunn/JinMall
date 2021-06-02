package com.jinunn.mall.coupon.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.coupon.entity.SeckillSkuNoticeEntity;

import java.util.Map;

/**
 * 秒杀商品通知订阅
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:17:32
 */
public interface SeckillSkuNoticeService extends IService<SeckillSkuNoticeEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

