package com.jinunn.common.to;

import lombok.Data;

import java.math.BigDecimal;

/**
 * @author : JinDun
 * @date : 2021/6/19 22:45
 */
@Data
public class SpuBoundTo {

    private Long spuId;
    /**
     * 购买积分
     */
    private BigDecimal buyBounds;
    /**
     * 成长积分
     */
    private BigDecimal growBounds;
}
