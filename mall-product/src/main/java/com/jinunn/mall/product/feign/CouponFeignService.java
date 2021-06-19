package com.jinunn.mall.product.feign;

import com.jinunn.common.to.SkuReductionTo;
import com.jinunn.common.to.SpuBoundTo;
import com.jinunn.common.utils.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * 调用优惠价服务的接口
 *
 * @author jinunn
 */
@FeignClient("mall-coupon")
public interface CouponFeignService {

    /**
     *  调用优惠服务 保存积分信息
     * @param spuBoundTo 积分信息
     * @return true or false
     */
    @PostMapping("/coupon/spubounds/save")
    R saveSpuBounds(@RequestBody  SpuBoundTo spuBoundTo);

    /**
     * sku的优惠、满减等信息
     * @param skuReductionTo sku的优惠、满减等信息
     * @return true/false
     */
    @RequestMapping("coupon/skufullreduction/save/reduction")
    R saveReduction(@RequestBody SkuReductionTo skuReductionTo);
}
