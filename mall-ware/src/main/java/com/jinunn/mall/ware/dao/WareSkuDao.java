package com.jinunn.mall.ware.dao;

import com.jinunn.mall.ware.entity.WareSkuEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * 商品库存
 * 
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:43:13
 */
@Mapper
public interface WareSkuDao extends BaseMapper<WareSkuEntity> {

    /**
     * 查询当前sku的总库存量
     * @param skuid 商品id
     * @return 查询当前sku的总库存量
     */
    Long getSkuStock(@Param("skuid") Long skuid);
}
