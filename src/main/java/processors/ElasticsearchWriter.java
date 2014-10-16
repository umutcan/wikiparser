package processors;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.bulk.BulkItemResponse;
import org.elasticsearch.action.bulk.BulkRequestBuilder;
import org.elasticsearch.action.bulk.BulkResponse;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONException;

/**
 * Created by umutcan on 14.10.2014.
 */
public class ElasticsearchWriter extends BufferProcessor {

    private Client es;
    private int counter = 0;
    private String index;
    private String type;
    private BulkRequestBuilder bulkRequest;
    private static final int BULK_LIMIT = 1000;

    public ElasticsearchWriter(String host){
        this.es = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(host, 9300));
        this.index = "wiki";
        this.type = "pages";
        this.bulkRequest = es.prepareBulk();
    }

    @Override
    public void process(StringBuffer buffer) {
        try {
            this.bulkRequest.add(this.es.prepareIndex(this.index, this.type)
                            .setSource(this.makeJSONStr(buffer.toString()))
            );
            if(counter % BULK_LIMIT == 0 && counter > 0){
                BulkResponse bulkResponse = bulkRequest.execute().actionGet();
                if(bulkResponse.hasFailures()){
                    System.out.println("Has failures");
                }
                System.out.println(counter + " documents saved." );
                this.bulkRequest = this.es.prepareBulk();
            }
            counter++;
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        } catch (ElasticsearchException e){
            System.out.println(e.getMessage());
        }
    }

}
