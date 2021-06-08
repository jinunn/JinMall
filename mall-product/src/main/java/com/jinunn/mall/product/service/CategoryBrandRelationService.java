package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.CategoryBrandRelationEntity;

import java.util.List;
import java.util.Map;

/**
 * 品牌分类关联
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-01-03 13:42:36
 */
public interface CategoryBrandRelationService extends IService<CategoryBrandRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 获取当前品牌关联的所有分类列表
     * @param brandId
     * @return
     */
    List<CategoryBrandRelationEntity> getlist(Long brandId);

    /**
     * 保存
     * @param categoryBrandRelation 品牌和分组的信息
     */
    void saveDetail(CategoryBrandRelationEntity categoryBrandRelation);

    /**
     * 修改分类冗余字段
     */
    void updateCategory(Long catId, String name);

    /**
     *  关联更新
     * @param brandId 品牌id
     * @param name 品牌名称
     */
    void updateBrand(Long brandId, String name);
}

