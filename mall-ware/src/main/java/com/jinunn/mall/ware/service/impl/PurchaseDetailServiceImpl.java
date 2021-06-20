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

import com.jinunn.mall.ware.dao.PurchaseDetailDao;
import com.jinunn.mall.ware.entity.PurchaseDetailEntity;
import com.jinunn.mall.ware.service.PurchaseDetailService;


@Service("purchaseDetailService")
public class PurchaseDetailServiceImpl extends ServiceImpl<PurchaseDetailDao, PurchaseDetailEntity> implements PurchaseDetailService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<PurchaseDetailEntity> wrapper = new QueryWrapper<PurchaseDetailEntity>().lambda();
        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String wareId = (String) params.get("wareId");
        if (StringUtils.isNotBlank(key)){
            wrapper.and(wrappe->wrappe.eq(PurchaseDetailEntity::getPurchaseId,key).or().eq(PurchaseDetailEntity::getSkuId,key));
        }
        if (StringUtils.isNotBlank(status)){
            wrapper.eq(PurchaseDetailEntity::getStatus,status);
        }
        if (StringUtils.isNotBlank(wareId)){
            wrapper.eq(PurchaseDetailEntity::getWareId,wareId);
        }
        IPage<PurchaseDetailEntity> page = this.page(
                new Query<PurchaseDetailEntity>().getPage(params),wrapper
        );

        return new PageUtils(page);
    }

}