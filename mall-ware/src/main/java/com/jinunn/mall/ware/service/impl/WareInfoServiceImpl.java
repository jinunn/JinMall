package com.jinunn.mall.ware.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import java.util.Map;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jinunn.common.utils.PageUtils;
import com.jinunn.common.utils.Query;

import com.jinunn.mall.ware.dao.WareInfoDao;
import com.jinunn.mall.ware.entity.WareInfoEntity;
import com.jinunn.mall.ware.service.WareInfoService;


@Service("wareInfoService")
public class WareInfoServiceImpl extends ServiceImpl<WareInfoDao, WareInfoEntity> implements WareInfoService {

    @Override
    public PageUtils queryPage(Map<String, Object> params) {
        LambdaQueryWrapper<WareInfoEntity> wrapper = new QueryWrapper<WareInfoEntity>().lambda();

        String key = (String) params.get("key");
        if (StringUtils.isNotBlank(key)){
            wrapper.eq(WareInfoEntity::getId,key)
                    .or().like(WareInfoEntity::getName,key)
                    .or().like(WareInfoEntity::getAddress,key)
                    .or().eq(WareInfoEntity::getAreacode,key);
        }
        IPage<WareInfoEntity> page = this.page(
                new Query<WareInfoEntity>().getPage(params),wrapper
        );

        return new PageUtils(page);
    }

}