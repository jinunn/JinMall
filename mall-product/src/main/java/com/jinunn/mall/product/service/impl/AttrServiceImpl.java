package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.constant.ProductConstant;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;
import com.jinunn.mall.product.dao.AttrDao;
import com.jinunn.mall.product.entity.AttrAttrgroupRelationEntity;
import com.jinunn.mall.product.entity.AttrEntity;
import com.jinunn.mall.product.entity.AttrGroupEntity;
import com.jinunn.mall.product.entity.CategoryEntity;
import com.jinunn.mall.product.service.AttrAttrgroupRelationService;
import com.jinunn.mall.product.service.AttrService;
import com.jinunn.mall.product.service.CategoryService;
import com.jinunn.mall.product.vo.AttrRespVo;
import com.jinunn.mall.product.vo.AttrVo;
import lombok.extern.log4j.Log4j2;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author JINDUN
 */
@Log4j2
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
        if (attrEntity.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode()) && attr.getAttrGroupId()!=null){
            //2、保存关联关系
            AttrAttrgroupRelationEntity relationEntity = new AttrAttrgroupRelationEntity();
            relationEntity.setAttrId(attrEntity.getAttrId());
            relationEntity.setAttrGroupId(attr.getAttrGroupId());
            attrgroupRelationService.save(relationEntity);
        }
    }

    @Override
    public PageUtils queryBasePage(Map<String, Object> params, Long catelogId, Integer code) {
        QueryWrapper<AttrEntity> wrapper = new QueryWrapper<AttrEntity>()
                .eq("attr_type", code);

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

            //这里是因为类型是基本属性才需要查询分组信息
            if (attrEntity.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())){
                //根据当前的商品属性id 到关联表中查询对应的属性分组的id
                Long attrGroupId = attrgroupRelationService.getOne(new QueryWrapper<AttrAttrgroupRelationEntity>()
                        .eq("attr_id", attrEntity.getAttrId())).getAttrGroupId();
                if (attrGroupId != null) {
                    AttrGroupEntity groupEntity = attrGroupService.getById(attrGroupId);
                    attrRespVo.setAttrGroupName(groupEntity.getAttrGroupName());
                }
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

        //类型是基本属性才需要查询分组信息
        if (attrEntity.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())){
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

        if (attrEntity.getAttrType().equals(ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode())){
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

    /**
     * 获取指定分组id查询关联的所有基本属性
     * @param attrgroupId 分组id
     * @return 关联的所有基本属性
     */
    @Override
    public List<AttrEntity> attRelation(Long attrgroupId) {
        //1、根据属性分组id到关联表中查询所有属性分组
        LambdaQueryWrapper<AttrAttrgroupRelationEntity> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(AttrAttrgroupRelationEntity::getAttrGroupId,attrgroupId);

        //2、得到所有属性分组，然后遍历属性分组获取到属性id去商品信息表中查信息。
        List<Long> attrids = attrgroupRelationService.list(wrapper)
                .stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        if (attrids.size() == 0){
            return null;
        }

        //3、得到的属性id去商品信息表中查询
        return this.listByIds(attrids);
    }

    @Override
    public PageUtils getattrNoRelation(Map<String, Object> params, Long attrgroupId) {
        String key = (String) params.get("key");
        //1、当前属性分组只能关联自己所属分类的所有属性，得到自己的三级分类的id
        AttrEntity attrEntity = baseMapper.selectById(attrgroupId);
        Long catelogId = attrEntity.getCatelogId();

        //2、当前属性分组只能关联别的分组没有引用的属性
        //2.1 根据分类id查询出当前分类下的所有属性分组的id。
        List<Long> groupIds = attrGroupService
                .list(new LambdaQueryWrapper<AttrGroupEntity>()
                        .eq(AttrGroupEntity::getCatelogId, catelogId))
                .stream().map(AttrGroupEntity::getAttrGroupId).collect(Collectors.toList());

        //2.2 拿到【2.1步骤】查到的属性分组id去【关联表】中查询已经被这些属性分组关联的属性id。
        List<Long> attrIds = attrgroupRelationService
                .list(new LambdaQueryWrapper<AttrAttrgroupRelationEntity>()
                        .in(AttrAttrgroupRelationEntity::getAttrGroupId, groupIds))
                .stream().map(AttrAttrgroupRelationEntity::getAttrId).collect(Collectors.toList());

        //2.3 查询本分类下，不包含【2.2步骤】已经关联分组的属性id,得到的就是属于当前分类下并且没有被其他属性分组关联的属性。
        LambdaQueryWrapper<AttrEntity> wrapper = new LambdaQueryWrapper<AttrEntity>()
                .eq(AttrEntity::getCatelogId, catelogId)
                //只需要查询基本属性
                .eq(AttrEntity::getAttrType,ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode());
        if (attrIds.size() > 0) {
            wrapper.notIn(AttrEntity::getAttrId, attrIds);
        }
        if (!StringUtils.isEmpty(key)) {
            wrapper.like(AttrEntity::getAttrName, key);
        }
        IPage<AttrEntity> page = this.page(new Query<AttrEntity>().getPage(params), wrapper);
        return new PageUtils(page);
    }

    @Override
    public List<Long> selectSearchAttrs(List<Long> attrIds) {
        return baseMapper.selectSearchAttrs(attrIds);
    }
}