package com.jinunn.mall.product.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.to.SkuReductionTo;
import com.jinunn.common.to.SpuBoundTo;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;
import com.jinunn.mall.product.dao.SpuInfoDao;
import com.jinunn.mall.product.entity.*;
import com.jinunn.mall.product.feign.CouponFeignService;
import com.jinunn.mall.product.service.*;
import com.jinunn.mall.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @author jindun
 */
@Service("spuInfoService")
public class SpuInfoServiceImpl extends ServiceImpl<SpuInfoDao, SpuInfoEntity> implements SpuInfoService {

    @Autowired
    private SpuInfoDescService spuInfoDescService;

    @Autowired
    private SpuImagesService spuImagesService;

    @Autowired
    private AttrService attrService;

    @Autowired
    private ProductAttrValueService productAttrValueService;

    @Autowired
    private SkuInfoService skuInfoService;

    @Autowired
    private SkuImagesService skuImagesService;

    @Autowired
    private SkuSaleAttrValueService skuSaleAttrValueService;

    @Autowired
    private CouponFeignService couponFeignService;

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),
                new QueryWrapper<>()
        );

        return new PageUtils(page);
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public void saveSpuInfo(SpuSaveVo saveVo) {
        //1、保存spu基本信息 pms_spu_info(spu信息)
        SpuInfoEntity spuInfoEntity = new SpuInfoEntity();
        spuInfoEntity.setSpuName(saveVo.getSpuName());
        spuInfoEntity.setSpuDescription(saveVo.getSpuDescription());
        spuInfoEntity.setCatalogId(saveVo.getCatalogId());
        spuInfoEntity.setBrandId(saveVo.getBrandId());
        spuInfoEntity.setWeight(saveVo.getWeight());
        spuInfoEntity.setPublishStatus(saveVo.getPublishStatus());
        this.saveBaseSupInfo(spuInfoEntity);

        //2、保存spu的秒速图片 pms_spu_info_desc(spu信息介绍大图)
        SpuInfoDescEntity spuInfoDescEntity = new SpuInfoDescEntity();
        List<String> decript = saveVo.getDecript();
        spuInfoDescEntity.setSpuId(spuInfoEntity.getId());
        spuInfoDescEntity.setDecript(String.join(",",decript));
        spuInfoDescService.save(spuInfoDescEntity);

        //3、保存spu的图片集 pms_sku_images(spu图片)
        List<String> images = saveVo.getImages();
        spuImagesService.saveImages(spuInfoEntity.getId(),images);

        //4、保存spu的规格参数 pms_product_attr_value(spu属性值)
        List<BaseAttrsVo> baseAttrsVoList = saveVo.getBaseAttrs();
        List<ProductAttrValueEntity> productAttrValueEntityList = baseAttrsVoList.stream().map(baseAttrsVo -> {
            ProductAttrValueEntity productAttrValueEntity = new ProductAttrValueEntity();
            productAttrValueEntity.setAttrId(baseAttrsVo.getAttrId());
            String attrName = attrService.getById(baseAttrsVo.getAttrId()).getAttrName();
            productAttrValueEntity.setAttrName(attrName);
            productAttrValueEntity.setAttrValue(baseAttrsVo.getAttrValues());
            productAttrValueEntity.setQuickShow(baseAttrsVo.getShowDesc());
            productAttrValueEntity.setSpuId(spuInfoEntity.getId());
            return productAttrValueEntity;
        }).collect(Collectors.toList());
        productAttrValueService.saveBatch(productAttrValueEntityList);

        //5、保存spu的积分信息 mall-sms  ---> sms_spu_bounds 商品spu积分设置
        BoundsVo bounds = saveVo.getBounds();
        SpuBoundTo spuBoundTo = new SpuBoundTo();
        spuBoundTo.setSpuId(spuInfoEntity.getId());
        spuBoundTo.setBuyBounds(bounds.getBuyBounds());
        spuBoundTo.setGrowBounds(bounds.getGrowBounds());
        couponFeignService.saveSpuBounds(spuBoundTo);


        //5、保存当前spu的sku信息
           //5.1 sku的基本信息 pms_sku_info(sku信息)
        List<SkusVo> skus = saveVo.getSkus();
        if (!skus.isEmpty()){
            skus.forEach(skusVo -> {
                String defaultImg="";
                for (Images image : skusVo.getImages()) {
                    if (image.getDefaultImg()==1){
                        defaultImg=image.getImgUrl();
                    }
                }
                SkuInfoEntity skuInfoEntity = new SkuInfoEntity();
                skuInfoEntity.setSkuName(skusVo.getSkuName());
                skuInfoEntity.setPrice(skusVo.getPrice());
                skuInfoEntity.setSkuTitle(skusVo.getSkuTitle());
                skuInfoEntity.setSkuSubtitle(skusVo.getSkuSubtitle());
                skuInfoEntity.setBrandId(spuInfoEntity.getBrandId());
                skuInfoEntity.setCatalogId(spuInfoEntity.getCatalogId());
                skuInfoEntity.setSaleCount(0L);
                skuInfoEntity.setSpuId(spuInfoEntity.getId());
                skuInfoEntity.setSkuDefaultImg(defaultImg);
                skuInfoService.save(skuInfoEntity);

                Long skuId = skuInfoEntity.getSkuId();

                //5.2 sku的图片信息 pms_sku_images（sku图片)
                List<SkuImagesEntity> skuImagesEntityList = skusVo.getImages()
                        .stream()
                        .filter(image -> StringUtils.isNotBlank(image.getImgUrl()))
                        .map(image -> {
                    SkuImagesEntity skuImagesEntity = new SkuImagesEntity();
                    skuImagesEntity.setSkuId(skuId);
                    skuImagesEntity.setImgUrl(image.getImgUrl());
                    skuImagesEntity.setDefaultImg(image.getDefaultImg());
                    return skuImagesEntity;
                }).collect(Collectors.toList());
                skuImagesService.saveBatch(skuImagesEntityList);

                //5.3 sku的销售属性信息：pms_sku_sale_attr_value(sku销售属性&值)
                List<SkuSaleAttrValueEntity> skuSaleAttrValueEntityList = skusVo.getAttr()
                        .stream().map(attr -> {
                            SkuSaleAttrValueEntity skuSaleAttrValueEntity = new SkuSaleAttrValueEntity();
                            skuSaleAttrValueEntity.setAttrId(attr.getAttrId());
                            skuSaleAttrValueEntity.setAttrName(attr.getAttrName());
                            skuSaleAttrValueEntity.setAttrValue(attr.getAttrValue());
                            skuSaleAttrValueEntity.setSkuId(skuId);
                            return skuSaleAttrValueEntity;
                        }).collect(Collectors.toList());
                skuSaleAttrValueService.saveBatch(skuSaleAttrValueEntityList);

                //5.4 sku的优惠、满减等信息 mall-sms库--->sms_sku_ladder/sms_sku_full_reduction/sms_member_price
                SkuReductionTo skuReductionTo = new SkuReductionTo();
                skuReductionTo.setSkuId(skuId);
                skuReductionTo.setDiscount(skusVo.getDiscount());
                skuReductionTo.setCountStatus(skusVo.getCountStatus());
                skuReductionTo.setFullCount(skusVo.getFullCount());
                skuReductionTo.setFullPrice(skusVo.getFullPrice());
                //skuReductionTo.setMemberPrice(skusVo.getMemberPrice());
                couponFeignService.saveReduction(skuReductionTo);
            });
        }
    }

    @Override
    public void saveBaseSupInfo(SpuInfoEntity spuInfoEntity) {
        baseMapper.insert(spuInfoEntity);
    }

}