package com.jinunn.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Configuration;

/**
 * @author : JinDun
 * @date : 2021/7/12 0:45
 */
@Configuration
public class ElasticSearchConfig {

    public RestHighLevelClient restHighLevelClient(){
        //可以指定多个host集群。
        RestClientBuilder builder = RestClient.builder(new HttpHost("47.110.138.50", 9200, "http"));
        return new RestHighLevelClient(builder);
    }
}
