package com.jinunn.mall.product.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jinunn.mall.product.entity.AttrAttrgroupRelationEntity;
import com.jinunn.mall.product.entity.AttrEntity;
import com.jinunn.mall.product.service.AttrAttrgroupRelationService;
import com.jinunn.mall.product.service.AttrService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jinunn.mall.product.entity.AttrGroupEntity;
import com.jinunn.mall.product.service.AttrGroupService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.R;



/**
 * 属性分组
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-02 23:45:52
 */
@RestController
@RequestMapping("product/attrgroup")
public class AttrGroupController {
    @Autowired
    private AttrGroupService attrGroupService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private AttrAttrgroupRelationService attrgroupRelationService;

    @PostMapping("attt/relation")
    public R addRelation(@RequestBody List<AttrAttrgroupRelationEntity> relationEntity){
        attrgroupRelationService.addRelation(relationEntity);
        return R.ok();
    }

    /**
     * 获取指定分组关联的所有属性
     */
    @GetMapping("{attrgroupId}/attr/relation")
    public R attRelation(@PathVariable("attrgroupId") Long attrgroupId){
        List<AttrEntity> data = attrService.attRelation(attrgroupId);
        return R.ok().put("data",data);
    }

    /**
     * 获取本分类 属性分组里面还没有关联的其他基本属性
     */
    @GetMapping("/{attrgroupId}/noattr/relation")
    public R attrNoRelation(@PathVariable Long attrgroupId,
                            @RequestParam Map<String, Object> params){
        PageUtils page =attrService.getattrNoRelation(params,attrgroupId);
        return R.ok().put("data",page);
    }

    /**
     * 列表
     */
    @RequestMapping("/list/{cateLogId}")
    public R list(@RequestParam Map<String, Object> params,
                  @PathVariable("cateLogId") Long cateLogId){
        PageUtils page = attrGroupService.queryPage(params,cateLogId);

        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @GetMapping("/info/{attrGroupId}")
    public R info(@PathVariable("attrGroupId") Long attrGroupId){
		AttrGroupEntity attrGroup = attrGroupService.getAttrGroup(attrGroupId);
        return R.ok().put("attrGroup", attrGroup);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.save(attrGroup);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrGroupEntity attrGroup){
		attrGroupService.updateById(attrGroup);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrGroupIds){
		attrGroupService.removeByIds(Arrays.asList(attrGroupIds));

        return R.ok();
    }

}
