import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.index.query.TermQueryBuilder;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.builder.SearchSourceBuilder;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Test1
 * Description TODO
 **/
public class Test3 {
    public static void main(String[] args) throws IOException {
        /*连接rest接口*/
        /*获得需要连接的地址*/
        HttpHost http = new HttpHost("localhost", 9200, "http");
        /*创建rest客户端构建器*/
        RestClientBuilder restClientBuilder = RestClient.builder(http);
        /*创建高级客户端rest*/
        RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);
        /*GET /sku/_search
        {
          "query": {
            "bool": {
              "must": [
                {
                  "match": {
                    "name": "电视"
                  }
                },
                {
                  "term": {
                    "brandName": "小米"
                  }
                }
              ]
            }
          }
        }*/
        /*封装请求对象*/
        SearchRequest searchRequest = new SearchRequest("sku");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*"bool": {*/
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*match": {
            "name": "电视"
          }*/
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "电视");
        /*"must": [
                {
                  "match": {
                    "name": "电视"
                  }
                },*/
        boolQueryBuilder.must(matchQueryBuilder);
        /*term查询,类似*/
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brandName", "小米");
        /*加入布尔,must*/
        boolQueryBuilder.must(termQueryBuilder);
        /*bool加入query*/
        searchSourceBuilder.query(boolQueryBuilder);
        /*query加入search*/
        searchRequest.source(searchSourceBuilder);
        /*获得请求对象*/
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);
        SearchHits searchHits = searchResponse.getHits();
        long totalHits = searchHits.getTotalHits();
        System.out.println("记录数" + totalHits);
        /*得到内容hits列*/
        SearchHit[] hits = searchHits.getHits();
        for (SearchHit hit : hits) {
            String source = hit.getSourceAsString();
            System.out.println(source);
        }
        /*客户端有始有终*/
        client.close();
    }
}
