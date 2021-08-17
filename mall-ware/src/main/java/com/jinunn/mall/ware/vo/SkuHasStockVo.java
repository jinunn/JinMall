package com.jinunn.mall.ware.vo;

import lombok.Data;

/**
 * @author : JinDun
 * @date : 2021/8/17 23:15
 */
@Data
public class SkuHasStockVo {

    private Long skuId;

    /**
     * 是否剩余库存
     */
    private Boolean hasStock;
}
