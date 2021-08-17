package com.jinunn.mall.product.dao;

import com.jinunn.mall.product.entity.AttrEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 商品属性
 * 
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
@Mapper
public interface AttrDao extends BaseMapper<AttrEntity> {

    /**
     * 在指定的所有属性集合里面，查询出可以被检索的属性
     * @param attrIds 属性id
     * @return 可被检索的属性
     */
    List<Long> selectSearchAttrs(@Param("attrIds") List<Long> attrIds);
}
