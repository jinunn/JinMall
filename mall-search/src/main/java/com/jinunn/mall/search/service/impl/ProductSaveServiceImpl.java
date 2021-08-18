package com.jinunn.mall.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.jinunn.common.to.es.SkuEsModel;
import com.jinunn.mall.search.config.ElasticSearchConfig;
import com.jinunn.mall.search.constant.EsConstant;
import com.jinunn.mall.search.service.ProductSaveService;
import lombok.extern.slf4j.Slf4j;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

/**
 * @author : JinDun
 * @date : 2021/8/18 0:09
 */
@Slf4j
@Service
public class ProductSaveServiceImpl implements ProductSaveService {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Override
    public boolean productStatusUp(List<SkuEsModel> skuEsModels) {
        //给es建立索引,product,建立好映射关系
        BulkRequest bulkRequest = new BulkRequest();
        skuEsModels.forEach(skuEsModel -> {
            //设置索引
            IndexRequest indexRequest = new IndexRequest(EsConstant.PRODUCT_INDEX);
            //设置索引id
            indexRequest.id(skuEsModel.getSkuId().toString())
                    .source(JSON.toJSONString(skuEsModel), XContentType.JSON);
            bulkRequest.add(indexRequest);
        });
        BulkResponse bulk;
        try {
            // bulk批量保存
            bulk = restHighLevelClient.bulk(bulkRequest, ElasticSearchConfig.COMMON_OPTIONS);
        } catch (IOException e) {
            log.error(e.getMessage(),e);
            e.printStackTrace();
            return false;
        }
        //判断是否出现错误,false没有出现错误，true又有错误
        return bulk.hasFailures();
    }
}
