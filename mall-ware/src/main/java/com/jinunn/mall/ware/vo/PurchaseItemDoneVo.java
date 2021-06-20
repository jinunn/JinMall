package com.jinunn.mall.ware.vo;

import lombok.Data;

/**
 * @author : JinDun
 * @date : 2021/6/20 18:28
 */
@Data
public class PurchaseItemDoneVo {

    /**
     * 采购项id
     */
    private Long itemId;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 原因
     */
    private String reason;
}
