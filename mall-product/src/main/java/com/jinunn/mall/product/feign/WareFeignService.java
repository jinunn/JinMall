package com.jinunn.mall.product.feign;

import com.jinunn.mall.product.vo.SkuHasStockVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * @author 37213
 */
@FeignClient("mall-ware")
public interface WareFeignService {

    @PostMapping("ware/waresku/hasStock")
    public List<SkuHasStockVo> getSkuHasStock(@RequestBody List<Long> skuIds);
}
