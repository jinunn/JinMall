package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.product.dao.ProductAttrValueDao;
import com.jinunn.mall.product.entity.ProductAttrValueEntity;
import com.jinunn.mall.product.service.ProductAttrValueService;
import org.springframework.transaction.annotation.Transactional;


@Service("productAttrValueService")
public class ProductAttrValueServiceImpl extends ServiceImpl<ProductAttrValueDao, ProductAttrValueEntity> implements ProductAttrValueService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<ProductAttrValueEntity> page = this.page(
                new Query<ProductAttrValueEntity>().getPage(params),
                new QueryWrapper<ProductAttrValueEntity>()
        );

        return new PageUtils(page);
    }

    @Override
    public List<ProductAttrValueEntity> listforspu(Long spuId) {
        return  baseMapper.selectList(new LambdaQueryWrapper<ProductAttrValueEntity>()
                .eq(ProductAttrValueEntity::getSpuId,spuId));
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateSpuAttr(Long supId, List<ProductAttrValueEntity> productAttrValueEntityList) {
        //1、首先删除这个spuId之前对应的所有属性
        baseMapper.delete(new LambdaQueryWrapper<ProductAttrValueEntity>().eq(ProductAttrValueEntity::getSpuId,supId));

        List<ProductAttrValueEntity> productAttrValueEntities = productAttrValueEntityList.stream()
                .peek(productAttrValueEntity -> productAttrValueEntity.setSpuId(supId)).collect(Collectors.toList());
        this.saveBatch(productAttrValueEntities);
    }
}