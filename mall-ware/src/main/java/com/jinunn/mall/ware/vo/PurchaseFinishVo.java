package com.jinunn.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : JinDun
 * @date : 2021/6/20 18:26
 */
@Data
public class PurchaseFinishVo {

    /**
     * 采购单id
     */
    private Long id;
    /**
     * 采购项
     */
    private List<PurchaseItemDoneVo> items;
}
