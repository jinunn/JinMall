package com.jinunn.mall.search;

import com.alibaba.fastjson.JSON;
import com.jinunn.mall.search.config.ElasticSearchConfig;
import lombok.Data;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.xcontent.XContentType;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.aggregations.metrics.Avg;
import org.elasticsearch.search.aggregations.metrics.AvgAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.Map;

@SpringBootTest
class MallSearchApplicationTests {

    @Autowired
    private RestHighLevelClient restHighLevelClient;

    @Test
    void contextLoads() {
        System.out.println(restHighLevelClient);
    }

    @Data
    static class User{
        private String userName;
        private Integer age;
    }

    @Test
    public void indexData() throws IOException {
        User user = new User();
        user.setUserName("jinunn");
        user.setAge(22);
        //新建索引
        IndexRequest request = new IndexRequest("users");
        //要保存的内容，id和文档,   XContentType.JSON:指定数据和类型
        request.id("1").source(JSON.toJSONString(user), XContentType.JSON);

        IndexResponse index = restHighLevelClient.index(request, ElasticSearchConfig.COMMON_OPTIONS);
        System.out.println(index);
    }

    @Test
    public void find() throws IOException {
        //检索的索引，可以有多个索引, 也可以：searchRequest.indices("bank");
        SearchRequest searchRequest = new SearchRequest("bank");
        SearchSourceBuilder sourceBuilder = new SearchSourceBuilder();

        //构造索引条件
        //sourceBuilder.query();
        //sourceBuilder.from();
        //sourceBuilder.size();
        //sourceBuilder.aggregation();
        sourceBuilder.query(QueryBuilders.matchQuery("address","MILL"));

        // 构建第一个聚合条件:按照年龄的值分布 terms:查看聚合后值的分配情况，如38岁的有2人，28岁的有1人
        TermsAggregationBuilder ageAgg = AggregationBuilders.terms("ageAgg")
                .field("age").size(10);
        sourceBuilder.aggregation(ageAgg);

        //构建第二个聚合条件:平均薪资
        AvgAggregationBuilder balanceAvg = AggregationBuilders.avg("balanceAvg").field("balance");
        sourceBuilder.aggregation(balanceAvg);

        searchRequest.source(sourceBuilder);

        //执行检索
        SearchResponse search = restHighLevelClient.search(searchRequest, ElasticSearchConfig.COMMON_OPTIONS);
        //分析响应结果
        //System.out.println(search.toString());


        //获取所有查询到的记录
        SearchHits hits = search.getHits();
        //真正命中的所有记录
        SearchHit[] searchHits = hits.getHits();
        for (SearchHit hit : searchHits) {
            //hit.getId();
            //hit.getType();
            Map<String, Object> sourceAsMap = hit.getSourceAsMap();
            System.out.println(sourceAsMap);
        }

        //获取分析信息
        Aggregations aggregations = search.getAggregations();
        Terms aggTerms = aggregations.get("ageAgg");
        //获取Buckets信息
        for (Terms.Bucket bucket : aggTerms.getBuckets()) {
            System.out.println("年龄"+bucket.getKey()+" ===> " +bucket.getDocCount());
        }

        Avg balanceAvg1 = aggregations.get("balanceAvg");
        System.out.println("平均薪资"+balanceAvg1.getValue());
    }

}
