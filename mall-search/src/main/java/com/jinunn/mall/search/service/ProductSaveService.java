package com.jinunn.mall.search.service;

import com.jinunn.common.to.es.SkuEsModel;

import java.util.List;

/**
 * @author 37213
 */
public interface ProductSaveService {
    boolean productStatusUp(List<SkuEsModel> skuEsModels);
}
