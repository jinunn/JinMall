package com.jinunn.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : JinDun
 * @date : 2021/6/18 23:24
 */
@Data
public class MemberPrice {
    private Long id;
    private String name;
    private BigDecimal price;
}
