import org.apache.http.HttpHost;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.Terms;
import org.elasticsearch.search.aggregations.bucket.terms.TermsAggregationBuilder;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * @ClassName Test1
 * Description TODO
 **/
public class Test5 {
    public static void main(String[] args) throws IOException {
        /*连接rest接口*/
        /*获得需要连接的地址*/
        HttpHost http = new HttpHost("localhost", 9200, "http");
        /*创建rest客户端构建器*/
        RestClientBuilder restClientBuilder = RestClient.builder(http);
        /*创建高级客户端rest*/
        RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);

        /*封装请求对象*/
        /*GET sku/_search
        {
          "size": 0,
          "aggs": {
            "sku_category": {
              "terms": {
                "field": "categoryName"
              }
            },
            "sku_brand": {
              "terms": {
                "field": "brandName"
              }
            }
          }
        }*/
        SearchRequest searchRequest = new SearchRequest("sku");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        TermsAggregationBuilder termsAggregationBuilder =
                AggregationBuilders.terms("sku_category").field("categoryName");
        searchSourceBuilder.aggregation(termsAggregationBuilder);
        searchSourceBuilder.size(0);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /*然而hits有两层,因此继续寻找*/
        Aggregations aggregations = searchResponse.getAggregations();
        Map<String, Aggregation> asMap = aggregations.getAsMap();
        Terms terms = (Terms) asMap.get("sku_category");
        List<? extends Terms.Bucket> buckets = terms.getBuckets();
        for (Terms.Bucket bucket : buckets) {
            System.out.println(bucket.getKeyAsString()+":"+bucket.getDocCount());
        }
        /*客户端有始有终*/
        client.close();
    }
}
