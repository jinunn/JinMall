package com.jinunn.mall.product.vo;

import lombok.Data;

/**
 * @author : JinDun
 * @date : 2021/6/18 23:21
 */
@Data
public class BaseAttrsVo {
    private Long attrId;
    private String attrValues;
    /**
     * 是否快速展示
     */
    private int showDesc;
}
