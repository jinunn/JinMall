package com.jinunn.mall.product.dao;

import com.jinunn.mall.product.entity.CategoryEntity;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品三级分类
 * 
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
@Mapper
public interface CategoryDao extends BaseMapper<CategoryEntity> {
	
}
