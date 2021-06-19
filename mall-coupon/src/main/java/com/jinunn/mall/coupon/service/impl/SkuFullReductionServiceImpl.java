package com.jinunn.mall.coupon.service.impl;

import com.jinunn.common.to.MemberPrice;
import com.jinunn.common.to.SkuReductionTo;
import com.jinunn.mall.coupon.entity.MemberPriceEntity;
import com.jinunn.mall.coupon.entity.SeckillSkuRelationEntity;
import com.jinunn.mall.coupon.entity.SkuLadderEntity;
import com.jinunn.mall.coupon.service.MemberPriceService;
import com.jinunn.mall.coupon.service.SkuLadderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.coupon.dao.SkuFullReductionDao;
import com.jinunn.mall.coupon.entity.SkuFullReductionEntity;
import com.jinunn.mall.coupon.service.SkuFullReductionService;
import org.springframework.transaction.annotation.Transactional;


@Service("skuFullReductionService")
public class SkuFullReductionServiceImpl extends ServiceImpl<SkuFullReductionDao, SkuFullReductionEntity> implements SkuFullReductionService {

    @Autowired
    private SkuLadderService skuLadderService;

    @Autowired
    private MemberPriceService memberPriceService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SkuFullReductionEntity> page = this.page(
                new Query<SkuFullReductionEntity>().getPage(params),
                new QueryWrapper<SkuFullReductionEntity>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveReduction(SkuReductionTo skuReductionTo) {
        //sku的优惠、满减等信息 mall-sms库--->sms_sku_ladder/sms_sku_full_reduction/sms_member_price
        //sms_sku_ladder
        SkuLadderEntity skuLadderEntity = new SkuLadderEntity();
        skuLadderEntity.setSkuId(skuReductionTo.getSkuId());
        skuLadderEntity.setFullCount(skuReductionTo.getFullCount());
        skuLadderEntity.setDiscount(skuReductionTo.getDiscount());
        skuLadderEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuLadderEntity.getFullCount()>0) {
            skuLadderService.save(skuLadderEntity);
        }
        //sms_sku_full_reduction 满减信息
        SkuFullReductionEntity skuFullReductionEntity = new SkuFullReductionEntity();
        skuFullReductionEntity.setSkuId(skuReductionTo.getSkuId());
        skuFullReductionEntity.setFullPrice(skuReductionTo.getFullPrice());
        skuFullReductionEntity.setReducePrice(skuReductionTo.getReducePrice());
        skuFullReductionEntity.setAddOther(skuReductionTo.getCountStatus());
        if (skuFullReductionEntity.getFullPrice().compareTo(new BigDecimal(0))>0) {
            this.save(skuFullReductionEntity);
        }
        //sms_member_price 会员信息
        List<MemberPrice> memberPriceList = skuReductionTo.getMemberPrice();
        if (!memberPriceList.isEmpty()){
            List<MemberPriceEntity> memberPriceEntityList = memberPriceList
                    .stream()
                    .filter(memberPrice -> memberPrice.getPrice().compareTo(new BigDecimal(0))>0)
                    .map(memberPrice -> {
                MemberPriceEntity memberPriceEntity = new MemberPriceEntity();
                memberPriceEntity.setSkuId(skuReductionTo.getSkuId());
                memberPriceEntity.setMemberPrice(memberPrice.getPrice());
                memberPriceEntity.setMemberLevelId(memberPrice.getId());
                memberPriceEntity.setMemberLevelName(memberPrice.getName());
                memberPriceEntity.setAddOther(1);
                return memberPriceEntity;
            }).collect(Collectors.toList());
            memberPriceService.saveBatch(memberPriceEntityList);
        }
    }


}