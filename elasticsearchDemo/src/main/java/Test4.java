import org.apache.http.HttpHost;
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

/**
 * @ClassName Test1
 * Description TODO
 **/
public class Test4 {
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
          "query":{
            "bool":{
              "filter": [
                {
                  "match":
                  {
                    "brandName":"小米"
                  }
                }
              ]
            }
          }
        }*/
        SearchRequest searchRequest = new SearchRequest("sku");
        searchRequest.types("doc");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        BoolQueryBuilder boolQueryBuilder = QueryBuilders.boolQuery();
        /*构建match条件*/
        TermQueryBuilder termQueryBuilder = QueryBuilders.termQuery("brandName", "小米");
        /*bool的filter吸收match,可以吸收多个*/
        boolQueryBuilder.filter(termQueryBuilder);
        /*query吸收bool*/
        searchSourceBuilder.query(boolQueryBuilder);
        /*_search吸收query*/
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /*然而hits有两层,因此继续寻找*/
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
