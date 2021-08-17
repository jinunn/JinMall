package com.jinunn.mall.product.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.mall.product.entity.AttrEntity;
import com.jinunn.mall.product.vo.AttrRespVo;
import com.jinunn.mall.product.vo.AttrVo;

import java.util.List;
import java.util.Map;

/**
 * 商品属性
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:29:35
 */
public interface AttrService extends IService<AttrEntity> {

    PageUtils queryPage(Map<String, Object> params);

    void saveAttr(AttrVo attr);

    PageUtils queryBasePage(Map<String, Object> params, Long catelogId, Integer code);

    AttrRespVo getAttrInfo(Long attrId);

    void updateAttr(AttrRespVo attr);

    List<AttrEntity> attRelation(Long attrgroupId);

    /**
     * 获取本分类 属性分组里面还没有关联的其他基本属性
     * @param params 模糊查询
     * @param attrgroupId 分类id
     * @return 获取本分类 属性分组里面还没有关联的其他基本属性列表
     */
    PageUtils getattrNoRelation(Map<String, Object> params, Long attrgroupId);

    /**
     * 在指定的所有属性集合里面，查询出可以被检索的属性
     * @param attrIds 属性id
     * @return 可被检索的属性
     */
    List<Long> selectSearchAttrs(List<Long> attrIds);
}

