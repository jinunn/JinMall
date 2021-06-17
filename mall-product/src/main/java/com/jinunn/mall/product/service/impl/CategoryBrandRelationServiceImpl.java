package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;
import com.jinunn.mall.product.dao.BrandDao;
import com.jinunn.mall.product.dao.CategoryBrandRelationDao;
import com.jinunn.mall.product.dao.CategoryDao;
import com.jinunn.mall.product.entity.BrandEntity;
import com.jinunn.mall.product.entity.CategoryBrandRelationEntity;
import com.jinunn.mall.product.entity.CategoryEntity;
import com.jinunn.mall.product.service.BrandService;
import com.jinunn.mall.product.service.CategoryBrandRelationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Service("categoryBrandRelationService")
public class CategoryBrandRelationServiceImpl extends ServiceImpl<CategoryBrandRelationDao, CategoryBrandRelationEntity> implements CategoryBrandRelationService {

    @Resource
    private BrandDao brandDao;

    @Resource
    private CategoryDao categoryDao;

    @Autowired
    private BrandService brandService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<CategoryBrandRelationEntity> page = this.page(
                new Query<CategoryBrandRelationEntity>().getPage(params),
                new QueryWrapper<CategoryBrandRelationEntity>()
        );

        return new PageUtils(page);
    }

    /**
     * 获取当前品牌关联的所有分类列表
     */
    @Override
    public List<CategoryBrandRelationEntity> getlist(Long brandId) {
        QueryWrapper<CategoryBrandRelationEntity> wrapper = new QueryWrapper<>();
        wrapper.eq("brand_id", brandId);
        List<CategoryBrandRelationEntity> brandRelationEntityList = baseMapper.selectList(wrapper);
        return brandRelationEntityList;
    }

    /**
     * 保存当前品牌关联的所有分类列表
     */
    @Override
    public void saveDetail(CategoryBrandRelationEntity categoryBrandRelation) {
        //获取品牌的id
        Long brandId = categoryBrandRelation.getBrandId();
        //获取分类的id
        Long catelogId = categoryBrandRelation.getCatelogId();

        //查询品牌的名字
        BrandEntity brandEntity = brandDao.selectById(brandId);
        //查询分类的名字
        CategoryEntity categoryEntity = categoryDao.selectById(catelogId);

        //设置名称
        categoryBrandRelation.setBrandName(brandEntity.getName());
        categoryBrandRelation.setCatelogName(categoryEntity.getName());

        this.save(categoryBrandRelation);
    }

    /**
     *  修改分类冗余字段
     */
    @Override
    public void updateCategory(Long catId, String name) {
        CategoryBrandRelationEntity category = new CategoryBrandRelationEntity();
        category.setCatelogId(catId);
        category.setCatelogName(name);
        baseMapper.update(category, new UpdateWrapper<CategoryBrandRelationEntity>().eq("catelog_id", catId));
    }

    /**
     * 修改品牌冗余字段
     * @param brandId 品牌id
     * @param name 品牌名称
     */
    @Override
    public void updateBrand(Long brandId, String name) {
        CategoryBrandRelationEntity relationEntity = new CategoryBrandRelationEntity();
        relationEntity.setBrandId(brandId);
        relationEntity.setBrandName(name);
        baseMapper.update(relationEntity,new UpdateWrapper<CategoryBrandRelationEntity>()
                .eq("brand_id",brandId));
    }

    @Override
    public List<BrandEntity> getBrandsList(Long catId) {
        return baseMapper.selectList(new LambdaQueryWrapper<CategoryBrandRelationEntity>()
                .eq(CategoryBrandRelationEntity::getCatelogId, catId))
                .stream().map(categoryBrandRelationEntity ->
                        brandService.getById(categoryBrandRelationEntity.getBrandId()))
                .collect(Collectors.toList());
    }
}