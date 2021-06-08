package com.jinunn.mall.product.service.impl;

import com.jinunn.mall.product.service.CategoryBrandRelationService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.product.dao.BrandDao;
import com.jinunn.mall.product.entity.BrandEntity;
import com.jinunn.mall.product.service.BrandService;
import org.springframework.transaction.annotation.Transactional;


@Service("brandService")
public class BrandServiceImpl extends ServiceImpl<BrandDao, BrandEntity> implements BrandService {

    @Autowired
    private CategoryBrandRelationService categoryBrandRelationService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<BrandEntity> page = this.page(
                new Query<BrandEntity>().getPage(params),
                new QueryWrapper<BrandEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAll(BrandEntity brand) {
        //首先修改自己
        baseMapper.updateById(brand);
        if(StringUtils.isNotBlank(brand.getName())){
            categoryBrandRelationService.updateBrand(brand.getBrandId(),brand.getName());
            //TODO 更新其他关联
        }

    }

}