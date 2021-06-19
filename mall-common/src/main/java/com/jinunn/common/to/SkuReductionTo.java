package com.jinunn.common.to;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : JinDun
 * @date : 2021/6/19 23:00
 */
@Data
public class SkuReductionTo {

    private Long skuId;
    private int fullCount;
    private BigDecimal discount;
    private int countStatus;
    private BigDecimal fullPrice;
    private BigDecimal reducePrice;
    private int priceStatus;
    /**
     * 会员价格
     */
    private List<MemberPrice> memberPrice;
}
