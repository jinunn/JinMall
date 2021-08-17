package com.jinunn.mall.search.controller;

import com.jinunn.common.exception.BizCodeEnum;
import com.jinunn.common.to.es.SkuEsModel;
import com.jinunn.common.utils.R;
import com.jinunn.mall.search.service.ProductSaveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author : JinDun
 * @date : 2021/8/18 0:05
 */
@RestController
@RequestMapping("search/save")
public class ElasticSaveController {

    @Autowired
    private ProductSaveService productSaveService;

    @PostMapping("/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels) {
        boolean flag = productSaveService.productStatusUp(skuEsModels);
        return flag ? R.ok() : R.error(BizCodeEnum.PRODUCT_UP_EXCEPTION.getCode(),BizCodeEnum.PRODUCT_UP_EXCEPTION.getMsg());
    }
}
