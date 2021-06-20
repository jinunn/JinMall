package com.jinunn.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.jinunn.common.utils.R;
import com.jinunn.mall.ware.feign.ProductFeignService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.ware.dao.WareSkuDao;
import com.jinunn.mall.ware.entity.WareSkuEntity;
import com.jinunn.mall.ware.service.WareSkuService;


/**
 * @author Jinunn
 */
@Service("wareSkuService")
public class WareSkuServiceImpl extends ServiceImpl<WareSkuDao, WareSkuEntity> implements WareSkuService {

    @Autowired
    private ProductFeignService productFeignService;

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

    @Override
    public void addStock(Integer skuNum, Long skuId, Long wareId) {
        WareSkuEntity skuEntity = this.getOne(new LambdaQueryWrapper<WareSkuEntity>()
                .eq(WareSkuEntity::getSkuId, skuId)
                .eq(WareSkuEntity::getWareId, wareId));
        WareSkuEntity wareSkuEntity = new WareSkuEntity();
        wareSkuEntity.setSkuId(skuId);
        wareSkuEntity.setWareId(wareId);
        wareSkuEntity.setStock(skuNum);
        //远程查询sku的名字，如果失败，事务不进行回滚
        try {
            R info = productFeignService.info(skuId);
            if (info.getCode().equals(0)){
                Map<String,Object> skuInfo = (Map<String, Object>) info.get("skuInfo");
                wareSkuEntity.setSkuName((String) skuInfo.get("skuName"));
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error(e.getMessage(),e);
        }
        if (skuEntity!=null){
            wareSkuEntity.setStockLocked(0);
            wareSkuEntity.setSkuName("");
            this.save(wareSkuEntity);
        }else {
            this.update(new LambdaUpdateWrapper<WareSkuEntity>()
                    .eq(WareSkuEntity::getWareId,wareId).eq(WareSkuEntity::getSkuId,skuId));
        }
    }
}