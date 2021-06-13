package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.jinunn.mall.product.entity.AttrAttrgroupRelationEntity;
import com.jinunn.mall.product.entity.AttrGroupEntity;
import com.jinunn.mall.product.entity.CategoryEntity;
import com.jinunn.mall.product.service.AttrAttrgroupRelationService;
import com.jinunn.mall.product.service.CategoryService;
import com.jinunn.mall.product.vo.AttrRespVo;
import com.jinunn.mall.product.vo.AttrVo;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.product.dao.AttrDao;
import com.jinunn.mall.product.entity.AttrEntity;
import com.jinunn.mall.product.service.AttrService;
import org.springframework.transaction.annotation.Transactional;


@Service("attrService")
public class AttrServiceImpl extends ServiceImpl<AttrDao, AttrEntity> implements AttrService {

    @Autowired
    private AttrAttrgroupRelationService attrgroupRelationService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrGroupServiceImpl attrGroupService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveAttr(AttrVo attr) {
        //1、保存基本属性
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        baseMapper.insert(attrEntity);
        //2、保存关联关系
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrId(attrEntity.getAttrId());
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        attrgroupRelationService.save(relationEntity);
    }

    @Override
    public PageUtils queryBasePage(Map<String, Object> params, Long catelogId) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<>();
        if (catelogId!=0){
            wrapper.eq("catelog_id",catelogId);
        }
        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)){
            wrapper.like("attr_name",key);
        }
        IPage<AttrEntity> page = this.page(
                new Query<AttrEntity>().getPage(params), wrapper
        );
        PageUtils pageUtils = new PageUtils(page);
        List<AttrRespVo> attrRespVoList = page.getRecords().stream().map(attrEntity -> {
            AttrRespVo attrRespVo = new AttrRespVo();
            BeanUtils.copyProperties(attrEntity, attrRespVo);

            //根据当前的商品属性id 到关联表中查询对应的属性分组的id
            Long attrGroupId = attrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                    .eq("attr_id", attrEntity.getAttrId())).getAttrGroupId();
            if (attrGroupId != null) {
                AttrGroupEntity groupEntity = attrGroupService.getById(attrGroupId);
                attrRespVo.setAttrGroupName(groupEntity.getAttrGroupName());
            }
            //根据当前的所属分类id(catelog_id) 查分类的名字
            CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
            if (categoryEntity != null) {
                attrRespVo.setCatelogName(categoryEntity.getName());
            }
            return attrRespVo;
        }).collect(Collectors.toList());
        pageUtils.setList(attrRespVoList);
        return pageUtils;
    }

    @Override
    public AttrRespVo getAttrInfo(Long attrId) {
        AttrRespVo attrRespVo = new AttrRespVo();
        AttrEntity attrEntity = baseMapper.selectById(attrId);
        BeanUtils.copyProperties(attrEntity,attrRespVo);

        //设置分组信息
        AttrAttrgroupRelationEntity relationEntity = attrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                .eq("attr_id", attrId));
        if (relationEntity!=null){
            attrRespVo.setAttrGroupId(relationEntity.getAttrGroupId());
            AttrGroupEntity groupEntity = attrGroupService.getById(relationEntity.getAttrGroupId());
            if (groupEntity!=null){
                attrRespVo.setAttrGroupName(groupEntity.getAttrGroupName());
            }
        }
        //设置分类路径
        Long[] cateLogPath = categoryService.getCateLogPath(attrEntity.getCatelogId());
        attrRespVo.setCatelogPath(cateLogPath);
        CategoryEntity categoryEntity = categoryService.getById(attrEntity.getCatelogId());
        if (categoryEntity!=null){
            attrRespVo.setCatelogName(categoryEntity.getName());
        }
        return attrRespVo;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void updateAttr(AttrRespVo attr) {
        AttrEntity attrEntity = new AttrEntity();
        BeanUtils.copyProperties(attr,attrEntity);
        baseMapper.updateById(attrEntity);

        //修改分组关联信息
        AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
        relationEntity.setAttrGroupId(attr.getAttrGroupId());
        relationEntity.setAttrId(attr.getAttrId());
        int count = attrgroupRelationService
                .count(new QueryWrapper<AttrAttrgroupRelationEntity>().eq("attr_id", attr.getAttrId()));
        if (count>0){
            attrgroupRelationService.update(relationEntity,
                    new UpdateWrapper<AttrAttrgroupRelationEntity>().eq("attr_id",attr.getAttrId()));
        }else {
            attrgroupRelationService.save(relationEntity);
        }
    }
}