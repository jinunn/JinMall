package com.jinunn.mall.product.feign;

import com.jinunn.common.to.es.SkuEsModel;
import com.jinunn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 37213
 */
@FeignClient("mall-search")
public interface SearchFeignService {

    @PostMapping("search/save/product")
    public R productStatusUp(@RequestBody List<SkuEsModel> skuEsModels);
}
