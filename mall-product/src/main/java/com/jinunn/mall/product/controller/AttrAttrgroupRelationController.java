package com.jinunn.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jinunn.mall.product.entity.AttrGroupEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jinunn.mall.product.entity.AttrAttrgroupRelationEntity;
import com.jinunn.mall.product.service.AttrAttrgroupRelationService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.R;



/**
 * 属性&属性分组关联
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:45:52
 */
@RestController
@RequestMapping("product/attrattrgrouprelation")
public class AttrAttrgroupRelationController {
    @Autowired
    private AttrAttrgroupRelationService attrAttrgroupRelationService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = attrAttrgroupRelationService.queryPage(params);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		AttrAttrgroupRelationEntity attrAttrgroupRelation = attrAttrgroupRelationService.getById(id);

        return R.ok().put("attrAttrgroupRelation", attrAttrgroupRelation);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrAttrgroupRelationEntity attrAttrgroupRelation){
		attrAttrgroupRelationService.save(attrAttrgroupRelation);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrAttrgroupRelationEntity attrAttrgroupRelation){
		attrAttrgroupRelationService.updateById(attrAttrgroupRelation);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		attrAttrgroupRelationService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }


    /**
     * 根据分类id和分组id 删除关联表数据
     */
    @GetMapping("/attr/relation/delete")
    public R deleteRelation(@RequestBody List<AttrAttrgroupRelationEntity> attrGroupEntities ){
        attrAttrgroupRelationService.deleteRelation(attrGroupEntities);
        return R.ok();
    }
}
