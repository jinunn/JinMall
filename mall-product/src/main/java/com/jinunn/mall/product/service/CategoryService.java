package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.CategoryEntity;

import java.util.List;
import java.util.Map;

/**
 * 商品三级分类
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
public interface CategoryService extends IService<CategoryEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 查询所有商品列表,以树形结构的方式显示
     * @return 查询所有商品列表,以树形结构的方式显示
     */
    List<CategoryEntity> getListWallTree();

    void removeMenuByIds(Long id);

    /**
     *  找到catelog的完整路径 【父/子/孙】
     * @param catelogId 分类id
     * @return 完整路径
     */
    Long[] getCateLogPath(Long catelogId);
}

