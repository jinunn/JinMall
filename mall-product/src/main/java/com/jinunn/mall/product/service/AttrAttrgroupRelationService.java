package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.AttrAttrgroupRelationEntity;
import com.jinunn.mall.product.entity.AttrGroupEntity;

import java.util.List;
import java.util.Map;

/**
 * 属性&属性分组关联
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
public interface AttrAttrgroupRelationService extends IService<AttrAttrgroupRelationEntity> {

    PageUtils queryPage(Map<String, Object> params);

    /**
     * 根据分类id和分组id 删除关联表数据
     * @param attrAttrgroupRelationEntities 关联表实体类
     */
    void deleteRelation(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities);

    void addRelation(List<AttrAttrgroupRelationEntity> relationEntity);
}

