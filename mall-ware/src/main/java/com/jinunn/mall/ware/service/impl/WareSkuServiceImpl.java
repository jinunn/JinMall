package com.jinunn.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.ware.dao.WareSkuDao;
import com.jinunn.mall.ware.entity.WareSkuEntity;
import com.jinunn.mall.ware.service.WareSkuService;


@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareSkuEntity> wrapper = new QueryWrapper<WareSkuEntity>().lambda();
        String skuId = (String) params.get("skuId");
        String wareId = (String) params.get("wareId");
        if (StringUtils.isNotBlank(skuId)){
            wrapper.eq(WareSkuEntity::getSkuId,skuId);
        }
        if (StringUtils.isNotBlank(wareId)){
            wrapper.eq(WareSkuEntity::getWareId,wareId);
        }
        IPage<WareSkuEntity> page = this.page(
                new Query<WareSkuEntity>().getPage(params),wrapper
        );

        return new PageUtils(page);
    }

}