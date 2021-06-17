package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;
import com.jinunn.mall.product.dao.AttrGroupDao;
import com.jinunn.mall.product.entity.AttrEntity;
import com.jinunn.mall.product.entity.AttrGroupEntity;
import com.jinunn.mall.product.service.AttrGroupService;
import com.jinunn.mall.product.service.AttrService;
import com.jinunn.mall.product.service.CategoryService;
import com.jinunn.mall.product.vo.AttrGroupWithAttrsVo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author jindun
 */
@Service("attrGroupService")
public class AttrGroupServiceImpl extends ServiceImpl<AttrGroupDao, AttrGroupEntity> implements AttrGroupService {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private AttrService attrService;


    @Override
    public PageUtils queryPage(Map<String, Object> params, Long cateLogId) {
        //模糊查询
        String key = (String) params.get("key");
        QueryWrapper<AttrGroupEntity> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotBlank(key)){
            wrapper.like("attr_group_name",key);
        }

        //等于0 查询全部
        if (cateLogId==0){
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params), wrapper);
            return new PageUtils(page);
        }else {
            wrapper.eq("catelog_id", cateLogId);
            IPage<AttrGroupEntity> page = this.page(new Query<AttrGroupEntity>().getPage(params),
                    wrapper);
            return new PageUtils(page);
        }
    }

    @Override
    public AttrGroupEntity getAttrGroup(Long attrGroupId) {
        AttrGroupEntity groupEntity = this.getById(attrGroupId);
        Long[] path = categoryService.getCateLogPath(groupEntity.getCatelogId());
        groupEntity.setCatelogPath(path);
        return groupEntity;
    }

    @Override
    public List<AttrGroupWithAttrsVo> getAttrGroupWithAttrBycatelogId(Long catelogId) {
        //查询分组信息
        List<AttrGroupEntity> attrGroupEntityList = baseMapper.selectList(new LambdaQueryWrapper<AttrGroupEntity>()
                .eq(AttrGroupEntity::getAttrGroupId, catelogId));
        //查询这些属性分组下的所有属性
        return attrGroupEntityList.stream().map(attrGroupEntity -> {
            AttrGroupWithAttrsVo groupWithAttrsVo = new AttrGroupWithAttrsVo();
            BeanUtils.copyProperties(attrGroupEntity,groupWithAttrsVo);
            List<AttrEntity> attrEntities = attrService.attRelation(attrGroupEntity.getAttrGroupId());
            groupWithAttrsVo.setAttrs(attrEntities);
            return groupWithAttrsVo;
        }).collect(Collectors.toList());
    }
}