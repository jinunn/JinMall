package com.jinunn.mall.product.vo;

import lombok.Data;

/**
 * 用于显示根据分类id查询商品属性列表
 *
 * @author : JinDun
 * @date : 2021/6/12 23:58
 */
@Data
public class AttrRespVo{

    /**
     * 属性id
     */
    private Long attrId;
    /**
     * 属性名
     */
    private String attrName;
    /**
     * 是否需要检索[0-不需要，1-需要]
     */
    private Integer searchType;
    /**
     * 属性图标
     */
    private String icon;
    /**
     * 可选值列表[用逗号分隔]
     */
    private String valueSelect;
    /**
     * 属性类型[0-销售属性，1-基本属性，2-既是销售属性又是基本属性]
     */
    private Integer attrType;
    /**
     * 启用状态[0 - 禁用，1 - 启用]
     */
    private Long enable;
    /**
     * 所属分类
     */
    private Long catelogId;
    /**
     * 快速展示【是否展示在介绍上；0-否 1-是】，在sku中仍然可以调整
     */
    private Integer showDesc;

    /**
     * 属性分组的名称
     */
    private String attrGroupName;

    /**
     * 分类的名称
     */
    private String catelogName;
}
