package com.jinunn.mall.product.vo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.List;

/**
 * @author : JinDun
 * @date : 2021/6/18 23:15
 */
@Data
public class SpuSaveVo {

    private String spuName;
    private String spuDescription;
    private Long catalogId;
    private Long brandId;
    private BigDecimal weight;
    private int publishStatus;
    private List<String> decript;
    private List<String> images;
    private BoundsVo bounds;
    private List<BaseAttrsVo> baseAttrs;
    private List<SkusVo> skus;
}
