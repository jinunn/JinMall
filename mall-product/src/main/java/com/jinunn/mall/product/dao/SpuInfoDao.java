package com.jinunn.mall.product.dao;

import com.jinunn.common.constant.ProductConstant;
import com.jinunn.mall.product.entity.SpuInfoEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * spu信息
 * 
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
@Mapper
public interface SpuInfoDao extends BaseMapper<SpuInfoEntity> {


    /**
     *  商品上架，修改状态
     * @param spuId 商品id
     * @param code 上架
     */
    void upSpuStatus(@Param("spuId") Long spuId, @Param("code") Integer code);
}
