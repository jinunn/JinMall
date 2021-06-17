package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.product.dao.AttrAttrgroupRelationDao;
import com.jinunn.mall.product.entity.AttrAttrgroupRelationEntity;
import com.jinunn.mall.product.service.AttrAttrgroupRelationService;


/**
 * @author jindun
 */
@Service("attrAttrgroupRelationService")
public class AttrAttrgroupRelationServiceImpl extends ServiceImpl<AttrAttrgroupRelationDao, AttrAttrgroupRelationEntity> implements AttrAttrgroupRelationService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrAttrgroupRelationEntity> page = this.page(
                new Query<AttrAttrgroupRelationEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public void deleteRelation(List<AttrAttrgroupRelationEntity> attrAttrgroupRelationEntities) {
        attrAttrgroupRelationEntities.forEach(attrAttrgroupRelationEntity -> {
            LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(AttrAttrgroupRelationEntity::getAttrId,attrAttrgroupRelationEntity.getAttrId())
            .eq(AttrAttrgroupRelationEntity::getAttrGroupId,attrAttrgroupRelationEntity.getAttrGroupId());
            baseMapper.delete(wrapper);
        });
    }

    @Override
    public void addRelation(List<AttrAttrgroupRelationEntity> relationEntity) {
        this.saveBatch(relationEntity);
    }

}