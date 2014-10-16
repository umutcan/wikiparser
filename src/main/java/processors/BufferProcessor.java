package processors;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;

/**
 * Created by umutcan on 13.10.2014.
 */
public abstract class BufferProcessor {

    public abstract void process(String buffer);

    protected String makeJSONStr(String xmlStr) throws JSONException {
        JSONObject xmlJSONObj = XML.toJSONObject(xmlStr);
        return xmlJSONObj.toString(4);
    }
}
