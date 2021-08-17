package com.jinunn.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jinunn.mall.ware.vo.SkuHasStockVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jinunn.mall.ware.entity.WareSkuEntity;
import com.jinunn.mall.ware.service.WareSkuService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.R;



/**
 * 商品库存
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:43:13
 */
@RestController
@RequestMapping("ware/waresku")
public class WareSkuController {
    @Autowired
    private WareSkuService wareSkuService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = wareSkuService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		WareSkuEntity wareSku = wareSkuService.getById(id);

        return R.ok().put("wareSku", wareSku);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody WareSkuEntity wareSku){
		wareSkuService.save(wareSku);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody WareSkuEntity wareSku){
		wareSkuService.updateById(wareSku);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		wareSkuService.removeByIds(Arrays.asList(ids));

        return R.ok();
    }

    /**
     * 查询商品是否有库存
     */
    @PostMapping("hasStock")
    public List<SkuHasStockVo> getSkuHasStock(@RequestBody List<Long> skuIds){
        return wareSkuService.getSkuHasStock(skuIds);
    }
}
