package com.jinunn.mall.search.config;

import org.apache.http.HttpHost;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author : JinDun
 * @date : 2021/7/12 0:45
 *
 * 编写配置，给容器中注入一个RestHighLevelClient
 */
@Configuration
public class ElasticSearchConfig {

    public static final RequestOptions COMMON_OPTIONS;

    //es添加了安全访问规则，访问es需要添加一个安全头，就可以通过requestOptions设置,官方建议把requestOptions创建成单实例
    static {
        RequestOptions.Builder builder = RequestOptions.DEFAULT.toBuilder();

        COMMON_OPTIONS = builder.build();
    }

    @Bean
    public RestHighLevelClient restHighLevelClient(){
        //可以指定多个host集群。
        RestClientBuilder builder = RestClient.builder(new HttpHost("47.110.138.50", 9200, "http"));
        return new RestHighLevelClient(builder);
    }
}
