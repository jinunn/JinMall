package com.jinunn.mall.product.vo;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用于显示根据分类id查询商品属性列表
 *
 * @author : JinDun
 * @date : 2021/6/12 23:58
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AttrRespVo extends AttrVo{

    /**
     * 属性分组的名称
     */
    private String attrGroupName;

    /**
     * 分类的名称
     */
    private String catelogName;

    /**
     * 分类完整路径
     */
    private Long[] catelogPath;
}
