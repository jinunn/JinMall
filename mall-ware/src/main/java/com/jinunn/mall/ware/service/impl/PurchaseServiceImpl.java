package com.jinunn.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.constant.WareConstant;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;
import com.jinunn.mall.ware.dao.PurchaseDao;
import com.jinunn.mall.ware.entity.PurchaseDetailEntity;
import com.jinunn.mall.ware.entity.PurchaseEntity;
import com.jinunn.mall.ware.service.PurchaseDetailService;
import com.jinunn.mall.ware.service.PurchaseService;
import com.jinunn.mall.ware.service.WareSkuService;
import com.jinunn.mall.ware.vo.MergeVo;
import com.jinunn.mall.ware.vo.PurchaseFinishVo;
import com.jinunn.mall.ware.vo.PurchaseItemDoneVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author jinunn
 */
@Service("purchaseService")
public class PurchaseServiceImpl extends ServiceImpl<PurchaseDao, PurchaseEntity> implements PurchaseService {

    @Autowired
    private PurchaseDetailService purchaseDetailService;

    @Autowired
    private WareSkuService wareSkuService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Override
    public PageUtils queryPageUnaccalimed(Map<String, Object> params) {
        IPage<PurchaseEntity> page = this.page(
                new Query<PurchaseEntity>().getPage(params),
                new LambdaQueryWrapper<PurchaseEntity>()
                        .eq(PurchaseEntity::getStatus,0).or().eq(PurchaseEntity::getStatus,1)
        );
        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void mergePurchase(MergeVo mergeVo) {
        Long purchaseId = mergeVo.getPurchaseId();
        if (purchaseId==null){
            PurchaseEntity purchaseEntity = new PurchaseEntity();
            purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.CREATED.getCode());
            this.save(purchaseEntity);
            purchaseId=purchaseEntity.getId();
        }
        Long finalPurchaseId = purchaseId;
        List<PurchaseDetailEntity> purchaseDetailEntityList = mergeVo.getItems().stream().map(item -> {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            purchaseDetailEntity.setId(item);
            purchaseDetailEntity.setPurchaseId(finalPurchaseId);
            purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.ASSIGNED.getCode());
            return purchaseDetailEntity;
        }).collect(Collectors.toList());
        purchaseDetailService.updateBatchById(purchaseDetailEntityList);
    }

    @Override
    public void received(List<Long> ids) {
        //1、确认需要领取的当前采购单是新建或者已分配的，同时改变这些采购的状态为已领取。
        List<PurchaseEntity> purchaseEntityList = ids.stream()
                .map(this::getById)
                .filter(purchaseEntity -> purchaseEntity.getStatus().equals(WareConstant.PurchaseStatusEnum.CREATED.getCode()) ||
                        purchaseEntity.getStatus().equals(WareConstant.PurchaseStatusEnum.ASSIGNED.getCode()))
                .peek(purchaseEntity -> purchaseEntity.setStatus(WareConstant.PurchaseStatusEnum.RECEIVE.getCode()))
                .collect(Collectors.toList());
        //2、改变采购单的状态
        this.updateBatchById(purchaseEntityList);

        //3、改变采购项的状态
        purchaseEntityList.forEach(purchaseEntity -> {
            List<PurchaseDetailEntity> purchaseDetailEntityList =
                    purchaseDetailService.listDetailByPurchaseId(purchaseEntity.getId());
            List<PurchaseDetailEntity> purchaseDetailEntities = purchaseDetailEntityList.stream()
                    .peek(purchaseDetailEntity -> purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.PURCHASING.getCode()))
                    .collect(Collectors.toList());
            purchaseDetailService.updateBatchById(purchaseDetailEntities);
        });
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void done(PurchaseFinishVo purchaseFinishVo) {
        Long id = purchaseFinishVo.getId();

        // 1、改变采购项的状态
        boolean flag =true;
        List<PurchaseDetailEntity> updateList =new ArrayList<>();
        List<PurchaseItemDoneVo> items = purchaseFinishVo.getItems();
        for (PurchaseItemDoneVo item : items) {
            PurchaseDetailEntity purchaseDetailEntity = new PurchaseDetailEntity();
            if (item.getStatus().equals(WareConstant.PurchaseDetailStatusEnum.PURCHASINGFAILED.getCode())){
                flag=false;
                purchaseDetailEntity.setStatus(item.getStatus());
            }else {
                purchaseDetailEntity.setStatus(WareConstant.PurchaseDetailStatusEnum.FINISH.getCode());
                //采购成功的需要入库
                PurchaseDetailEntity detailEntity = purchaseDetailService.getById(item.getItemId());
                wareSkuService.addStock(detailEntity.getSkuNum(),detailEntity.getSkuId(),detailEntity.getWareId());
            }
            purchaseDetailEntity.setPurchaseId(item.getItemId());
            updateList.add(purchaseDetailEntity);
        }
        purchaseDetailService.updateBatchById(updateList);

        // 2、改变采购单状态（只有采购项所有的都成功，采购单的状态才能是成功的)
        PurchaseEntity purchaseEntity = new PurchaseEntity();
        purchaseEntity.setId(id);
        purchaseEntity.setStatus(flag ? WareConstant.PurchaseStatusEnum.FINISH.getCode() : WareConstant.PurchaseStatusEnum.ABNORMAL.getCode());
        this.updateById(purchaseEntity);

        // 3、将成功的采购进行入库操作
        if (purchaseEntity.getStatus().equals(WareConstant.PurchaseStatusEnum.FINISH.getCode())){
            updateList.forEach(purchaseDetailEntity -> {
                wareSkuService.addStock(purchaseDetailEntity.getSkuNum(),purchaseDetailEntity.getSkuId(),purchaseDetailEntity.getWareId());
            });
        }
    }
}