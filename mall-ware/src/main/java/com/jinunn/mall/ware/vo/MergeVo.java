package com.jinunn.mall.ware.vo;

import lombok.Data;

import java.util.List;

/**
 * @author : JinDun
 * @date : 2021/6/20 15:35
 */
@Data
public class MergeVo {
    /**
     * 整单id
     */
    private Long purchaseId;
    /**
     * 和并项集合
     */
    private List<Long> items;
}
