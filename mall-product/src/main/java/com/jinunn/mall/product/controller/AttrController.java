package com.jinunn.mall.product.controller;

import java.util.Arrays;
import java.util.Map;

import com.jinunn.common.constant.ProductConstant;
import com.jinunn.mall.product.vo.AttrRespVo;
import com.jinunn.mall.product.vo.AttrVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.jinunn.mall.product.entity.AttrEntity;
import com.jinunn.mall.product.service.AttrService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.R;



/**
 * 商品属性
 *
 * email 372138750@qq.com
 * @date 2021-06-02 23:45:52
 * @author jinunn
 */
@RestController
@RequestMapping("product/attr")
public class AttrController {
    @Autowired
    private AttrService attrService;

    /**
     * 根据商品分类id,获取商品【基本属性】列表
     */
    @RequestMapping("base/list/{catelogId}")
    public R getBaseList(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        Integer baseCode = ProductConstant.AttrEnum.ATTR_TYPE_BASE.getCode();
        PageUtils page = attrService.queryBasePage(params,catelogId,baseCode);
        return R.ok().put("page", page);
    }

    /**
     * 根据商品分类id,获取商品【销售属性】列表
     */
    @RequestMapping("sale/list/{catelogId}")
    public R getSaleList(@RequestParam Map<String, Object> params,
                  @PathVariable("catelogId") Long catelogId){
        Integer saleCode = ProductConstant.AttrEnum.ATTR_TYPE_SALE.getCode();
        PageUtils page = attrService.queryBasePage(params,catelogId,saleCode);
        return R.ok().put("page", page);
    }


    /**
     * 获取详情
     */
    @RequestMapping("/info/{attrId}")
    public R info(@PathVariable("attrId") Long attrId){
		AttrRespVo respVo = attrService.getAttrInfo(attrId);
        return R.ok().put("attr", respVo);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody AttrVo attr){
		attrService.saveAttr(attr);
        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody AttrRespVo attr){
		attrService.updateAttr(attr);
        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] attrIds){
		attrService.removeByIds(Arrays.asList(attrIds));

        return R.ok();
    }

}
