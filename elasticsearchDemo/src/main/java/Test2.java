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
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
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
public class Test2 {
    public static void main(String[] args) throws IOException {
        /*连接rest接口*/
        /*获得需要连接的地址*/
        HttpHost http = new HttpHost("localhost", 9200, "http");
        /*创建rest客户端构建器*/
        RestClientBuilder restClientBuilder = RestClient.builder(http);
        /*创建高级客户端rest*/
        RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);

        /*封装请求对象*/
        /*GET sku/doc/_search
        {
          "query":
          {
            "match":{
              "name":{
                "query":"电视"
              }

            }
          }
        }*/
        /*查询请求,get sku*/
        SearchRequest searchRequest = new SearchRequest("sku");
        /*get sku/doc,不写默认查询sku的全部索引*/
        searchRequest.types("doc");
        /*json信息,query,查询源构建器"query":*/
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        /*json,match属性"match":*/
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("name", "电视");
        /*match在query内*/
        searchSourceBuilder.query(matchQueryBuilder);
        /*query在打括号内,作为一个完整的请求体,模拟手写json的功能*/
        searchRequest.source(searchSourceBuilder);
        /*使用高级客户端获取查询结果*/
        /*开头的命令/_search,将大括号里面的json装载进入*/
        /*RequestOptions.DEFAULT,只有一个选项,并没有特殊的意义,只是为了以后的扩展*/
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        /*查询结果都在hits里面*/
        /*{
          "took" : 42,
          "timed_out" : false,
          "_shards" : {
            "total" : 5,
            "successful" : 5,
            "skipped" : 0,
            "failed" : 0
          },
          "hits" : {
            "total" : 1,
            "max_score" : 0.5753642,
            "hits" : [
              {
                "_index" : "sku",
                "_type" : "doc",
                "_id" : "1",
                "_score" : 0.5753642,
                "_source" : {
                  "name" : "小米电视",
                  "price" : 1000,
                  "spuId" : 1010101011,
                  "createTime" : "2019‐03‐01",
                  "categoryName" : "电视",
                  "brandName" : "小米",
                  "saleNum" : 10102,
                  "commentNum" : 1331,
                  "spec" : {
                    "网络制式" : "移动4G",
                    "屏幕尺寸" : "4.5"
                  }
                }
              }
            ]
          }
        }*/
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
