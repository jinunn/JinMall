package com.jinunn.mall.ware.feign;

import com.jinunn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

/**
 * @author : JinDun
 * @date : 2021/6/21 0:02
 */
@FeignClient("mall-product")
public interface ProductFeignService {

    /**
     *  1、让所有请求都通过网关调用
     *   1）: @FeignClient("mall-gateway") :给网关所在的服务发请求
     *   2）: api/product/skuinfo/info/{skuId}
     *  2、直接调用指定的服务
     *   1）: @FeignClient("mall-product")
     *   2): product/skuinfo/info/{skuId}
     */

    /**
     * sku信息
     */
    @GetMapping("product/skuinfo/info/{skuId}")
    R info(@PathVariable("skuId") Long skuId);
}
