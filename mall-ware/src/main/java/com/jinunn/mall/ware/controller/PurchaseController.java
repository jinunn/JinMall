package com.jinunn.mall.ware.controller;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import com.jinunn.mall.ware.vo.MergeVo;
import com.jinunn.mall.ware.vo.PurchaseFinishVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.jinunn.mall.ware.entity.PurchaseEntity;
import com.jinunn.mall.ware.service.PurchaseService;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.R;



/**
 * 采购信息
 *
 * @author jinunn
 * @email 372138750@qq.com
 * @date 2021-06-03 00:43:13
 */
@RestController
@RequestMapping("ware/purchase")
public class PurchaseController {
    @Autowired
    private PurchaseService purchaseService;

    /**
     * 列表
     */
    @RequestMapping("/list")
    public R list(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPage(params);
        return R.ok().put("page", page);
    }


    /**
     * 信息
     */
    @RequestMapping("/info/{id}")
    public R info(@PathVariable("id") Long id){
		PurchaseEntity purchase = purchaseService.getById(id);

        return R.ok().put("purchase", purchase);
    }

    /**
     * 保存
     */
    @RequestMapping("/save")
    public R save(@RequestBody PurchaseEntity purchase){
		purchaseService.save(purchase);

        return R.ok();
    }

    /**
     * 修改
     */
    @RequestMapping("/update")
    public R update(@RequestBody PurchaseEntity purchase){
		purchaseService.updateById(purchase);

        return R.ok();
    }

    /**
     * 删除
     */
    @RequestMapping("/delete")
    public R delete(@RequestBody Long[] ids){
		purchaseService.removeByIds(Arrays.asList(ids));
        return R.ok();
    }

    /**
     * 查询未领取的采购单
     */
    @RequestMapping("unaccalimed/list")
    public R unaccalimedList(@RequestParam Map<String, Object> params){
        PageUtils page = purchaseService.queryPageUnaccalimed(params);
        return R.ok().put("page", page);
    }

    /**
     * 合并采购单
     */
    @PostMapping("merge")
    public R merge(@RequestBody MergeVo mergeVo){
        purchaseService.mergePurchase(mergeVo);
        return R.ok();
    }

    /**
     * 领取采购单
     */
    @PostMapping("/received")
    public R received(@RequestBody List<Long> ids){
        purchaseService.received(ids);
        return R.ok();
    }

    /**
     * 完成采购单
     */
    @PostMapping("/done")
    public R finish(@RequestBody PurchaseFinishVo purchaseFinishVo){
        purchaseService.done(purchaseFinishVo);
        return R.ok();
    }
}
