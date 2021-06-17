package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.AttrGroupEntity;
import com.jinunn.mall.product.vo.AttrGroupWithAttrsVo;

import java.util.List;
import java.util.Map;

/**
 * 属性分组
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
public interface AttrGroupService extends IService<AttrGroupEntity> {

    PageUtils queryPage(Map<String, Object> params,Long cateLogId);

    /**
     * 分组详情信息
     * @param attrGroupId 分类的id
     * @return 分组详情信息
     */
    AttrGroupEntity getAttrGroup(Long attrGroupId);

    /**
     * 根据分类id 获取该分类下所有属性分组和属性分组关联的属性
     * @param catelogId 三级分类id
     * @return 所有属性分组和属性分组关联的属性
     */
    List<AttrGroupWithAttrsVo> getAttrGroupWithAttrBycatelogId(Long catelogId);
}

