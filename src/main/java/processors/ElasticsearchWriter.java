package processors;

import org.elasticsearch.ElasticsearchException;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.Client;
import org.elasticsearch.client.transport.TransportClient;
import org.elasticsearch.common.transport.InetSocketTransportAddress;
import org.json.JSONException;

/**
 * Created by umutcan on 14.10.2014.
 */
public class ElasticsearchWriter extends BufferProcessor {

    Client es;
    int counter = 0;
    String index;
    String type;

    public ElasticsearchWriter(String host){
        this.es = new TransportClient()
                .addTransportAddress(new InetSocketTransportAddress(host, 9300));
        this.index = "index";
        this.type = "pages";
    }

    @Override
    public void process(String buffer) {
        if(counter % 1000 == 0 && counter > 0)
            System.out.println(counter + " documents saved." );

        try {
            IndexResponse response = this.es.prepareIndex(this.index, this.type)
                    .setSource(this.makeJSONStr(buffer))
                    .execute()
                    .actionGet();
        } catch (JSONException e) {
            System.out.println(e.getMessage());
        } catch (ElasticsearchException e){
            System.out.println(e.getMessage());
        }
    }

}
