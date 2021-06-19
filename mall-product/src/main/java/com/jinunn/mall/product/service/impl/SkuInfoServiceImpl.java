package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.product.dao.SkuInfoDao;
import com.jinunn.mall.product.entity.SkuInfoEntity;
import com.jinunn.mall.product.service.SkuInfoService;


@Service("skuInfoService")
public class SkuInfoServiceImpl extends ServiceImpl<SkuInfoDao, SkuInfoEntity> implements SkuInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<SkuInfoEntity> wrapper = new QueryWrapper<SkuInfoEntity>().lambda();
        String key = (String) params.get("key");
        String catelogId = (String) params.get("catelogId");
        String brandId = (String) params.get("brandId");
        String min = (String) params.get("min");
        String max = (String) params.get("max");
        if (StringUtils.isNotBlank(key)){
            wrapper.and(wrappe->wrappe.eq(SkuInfoEntity::getSkuId,key).or().like(SkuInfoEntity::getSkuName,key));
        }

        if (StringUtils.isNotBlank(catelogId)){
            wrapper.eq(SkuInfoEntity::getCatalogId,catelogId);
        }

        if (StringUtils.isNotBlank(brandId)){
            wrapper.eq(SkuInfoEntity::getBrandId,brandId);
        }

        if (StringUtils.isNotBlank(min)){
            wrapper.ge(SkuInfoEntity::getPrice,min);
        }

        if (StringUtils.isNotBlank(max)){
            BigDecimal priceMax = new BigDecimal(max);
            //大于0 才会拼接sql
            if (priceMax.compareTo(new BigDecimal(0)) > 0) {
                wrapper.le(SkuInfoEntity::getPrice, max);
            }
        }

        IPage<SkuInfoEntity> page = this.page(
                new Query<SkuInfoEntity>().getPage(params), wrapper
        );

        return new PageUtils(page);
    }

}