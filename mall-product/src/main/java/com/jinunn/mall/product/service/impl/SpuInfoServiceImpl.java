package com.jinunn.mall.product.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.constant.ProductConstant;
import com.jinunn.common.to.SkuReductionTo;
import com.jinunn.common.to.SpuBoundTo;
import com.jinunn.common.to.es.AttrList;
import com.jinunn.common.to.es.SkuEsModel;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;
import com.jinunn.common.utils.R;
import com.jinunn.mall.product.dao.SpuInfoDao;
import com.jinunn.mall.product.entity.*;
import com.jinunn.mall.product.feign.CouponFeignService;
import com.jinunn.mall.product.feign.SearchFeignService;
import com.jinunn.mall.product.feign.WareFeignService;
import com.jinunn.mall.product.service.*;
import com.jinunn.mall.product.vo.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    @Autowired
    private BrandService brandService;

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private WareFeignService wareFeignService;

    @Autowired
    private SearchFeignService searchFeignService;

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

    @Override
    public PageUtils queryPageByCondition(Map<String, Object> params) {
        LambdaQueryWrapper<SpuInfoEntity> wrapper = new QueryWrapper<SpuInfoEntity>().lambda();

        String key = (String) params.get("key");
        String status = (String) params.get("status");
        String brandId = (String) params.get("brandId");
        String catelogId = (String) params.get("catelogId");

        if (StringUtils.isNotBlank(key)){
            wrapper.and(wrappe->
                    wrappe.eq(SpuInfoEntity::getId,key).or().like(SpuInfoEntity::getSpuName,key));
        }

        if (StringUtils.isNotBlank(status)){
            wrapper.eq(SpuInfoEntity::getPublishStatus,status);
        }

        if (StringUtils.isNotBlank(brandId)){
            wrapper.eq(SpuInfoEntity::getBrandId,brandId);
        }

        if (StringUtils.isNotBlank(catelogId)){
            wrapper.eq(SpuInfoEntity::getCatalogId,catelogId);
        }

        IPage<SpuInfoEntity> page = this.page(
                new Query<SpuInfoEntity>().getPage(params),wrapper
        );
        return new PageUtils(page);
    }

    @Override
    public void up(Long spuId) {
        //查询当前esMode对应的所有sku信息，品牌的名称
       List<SkuInfoEntity> skus =  skuInfoService.getSkuByspuId(spuId);

        //查询当前sku的所有可以被检索的规格属性
        List<ProductAttrValueEntity> attrAll = productAttrValueService.listforspu(spuId);
        List<Long> attrIds = attrAll.stream()
                .map(ProductAttrValueEntity::getAttrId)
                .collect(Collectors.toList());
        //查询可用于检索的商品属性
        List<Long>  searchAttrs = attrService.selectSearchAttrs(attrIds);
        //过滤掉不可以被检索的属性
        List<AttrList> attrLists = attrAll.stream()
                .filter(productAttrValueEntity -> searchAttrs.contains(productAttrValueEntity.getAttrId()))
                .map(productAttrValueEntity -> {
                    AttrList attrList = new AttrList();
                    BeanUtils.copyProperties(productAttrValueEntity, attrList);
                    return attrList;
                }).collect(Collectors.toList());

        //发送远程调用，库存系统查询是否为空
        Map<Long, Boolean> stockMap = null;
        try {
            List<Long> skuids = skus.stream().map(SkuInfoEntity::getSkuId).collect(Collectors.toList());
            List<SkuHasStockVo> skuHasStockVos = wareFeignService.getSkuHasStock(skuids);
            stockMap = skuHasStockVos.stream().collect(Collectors.toMap(SkuHasStockVo::getSkuId, SkuHasStockVo::getHasStock));
        } catch (Exception e) {
            log.error("远程调用库存服务失败,原因{}",e);
            e.printStackTrace();
        }


        Map<Long, Boolean> finalStockMap = stockMap;
        List<SkuEsModel> skuEsModelList = skus.stream().map(skuInfoEntity -> {
            //组装需要的数据
            SkuEsModel skuEsModel = new SkuEsModel();
            BeanUtils.copyProperties(skuInfoEntity, skuEsModel);
            skuEsModel.setSkuPrice(skuInfoEntity.getPrice());
            skuEsModel.setSkuImg(skuInfoEntity.getSkuDefaultImg());
            //是否有库存
            if (finalStockMap == null) {
                skuEsModel.setHasStock(true);
            } else {
                skuEsModel.setHasStock(finalStockMap.get(skuInfoEntity.getSkuId()));
            }
            //热度
            skuEsModel.setHotScore(0L);
            //品牌名称
            BrandEntity brandEntity = brandService.getById(skuInfoEntity.getBrandId());
            skuEsModel.setBrandName(brandEntity.getName());
            skuEsModel.setBrandImg(brandEntity.getLogo());
            //分类名称
            CategoryEntity categoryEntity = categoryService.getById(skuInfoEntity.getCatalogId());
            skuEsModel.setCatalogName(categoryEntity.getName());
            //设置检索属性
            skuEsModel.setAttrs(attrLists);
            return skuEsModel;
        }).collect(Collectors.toList());

        //todo 将数据发送给es进行保存
        R result = searchFeignService.productStatusUp(skuEsModelList);
        if (result.getCode()==0){
            //成功，修改当前状态
            baseMapper.upSpuStatus(spuId, ProductConstant.ProductStatusEnum.SPU_UP.getCode());
        }else {
            log.error("调用接口失败");
            //TODO 调用远程接口失败，接口幂等性？重试机制
        }
    }

}