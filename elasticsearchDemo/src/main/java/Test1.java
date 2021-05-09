import org.apache.http.HttpHost;
import org.elasticsearch.action.bulk.BulkRequest;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName Test1
 * Description TODO
 **/
public class Test1 {
    public static void main(String[] args) throws IOException {
        /*连接rest接口*/
        /*获得需要连接的地址*/
        HttpHost http = new HttpHost("localhost", 9200, "http");
        /*创建rest客户端构建器*/
        RestClientBuilder restClientBuilder = RestClient.builder(http);
        /*创建高级客户端rest*/
        RestHighLevelClient client = new RestHighLevelClient(restClientBuilder);

        /*封装请求对象*/
        /*索引,文档名,id*/
        BulkRequest bulkRequest=new BulkRequest();
        IndexRequest indexRequest = new IndexRequest("sku", "doc", "100");
        Map skuMap=new HashMap();
        skuMap.put("name","华为mete20 pro");
        skuMap.put("brandName","华为");
        skuMap.put("categoryName","手机");
        skuMap.put("price",1010221);
        skuMap.put("createTime","2019-05-01");
        skuMap.put("saleNum",101021);
        skuMap.put("commentNum",10102321);
        Map spec=new HashMap();
        spec.put("网络制式","移动4G");
        spec.put("屏幕尺寸","5");
        skuMap.put("spec",spec);
        indexRequest.source(skuMap);
        /*获得执行结果*/
        /*有一个默认参数,暂时没用,只是为了以后扩展*/
        bulkRequest.add(indexRequest);
        BulkResponse bulkResponse = client.bulk(bulkRequest, RequestOptions.DEFAULT);
        int status = bulkResponse.status().getStatus();
        System.out.println(status);
        /*客户端有始有终*/
        client.close();
    }
}
