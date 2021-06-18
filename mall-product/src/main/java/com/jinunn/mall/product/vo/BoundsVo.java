package com.jinunn.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : JinDun
 * @date : 2021/6/18 23:20
 */
@Data
public class BoundsVo {
    /**
     * 购买积分
     */
    private BigDecimal buyBounds;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;
}
