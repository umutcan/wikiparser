package extractors;

import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import processors.BufferProcessor;

/**
 * Created by umutcan on 13.10.2014.
 * Extracts Page data to a buffer.
 * In order to proccess, buffer a BufferProcessor is used.
 */
public class PageExtractHandler extends DefaultHandler {

    String buf = "";
    Boolean pageStarted = false;
    Boolean isNsTag = false;
    int counter = 0;
    BufferProcessor processor;

    public PageExtractHandler(BufferProcessor processor){
        this.processor = processor;
    }

    @Override
    public void startDocument() throws SAXException {
        /**
         * There is no need to implement this
         * because this class only handles page element.
         */
    }

    @Override
    public void endDocument() throws SAXException {
        /**
         * There is no need to implement this
         * because this class only handles page element.
         */
    }

    @Override
    public void startElement(String uri, String localName, String qName,
                             Attributes attributes) throws SAXException {
        /**
         * Extract page data between <page> tags
         */
        if (qName.equalsIgnoreCase("page")) {
            this.pageStarted = true;
            this.counter++;
        } else if(qName.equalsIgnoreCase("ns")){
            if(this.pageStarted)
                this.isNsTag = true;
                buf += "<" + qName + ">";
        } else {
            /**
             * start the tag if element is a child of page element
             */
            if (this.pageStarted) {
                buf += "<" + qName + ">";
            }
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName)
            throws SAXException {
        /**
         * when page tag is closed process the buffer
         */
        if (qName.equalsIgnoreCase("page")) {
            if(this.pageStarted){
                this.pageStarted = false;
                processor.process(buf);

            }
//            if(this.counter == 1000)
//                throw new SAXException("done");
            // empty the bufffer
            this.buf = "";
        } else if(qName.equalsIgnoreCase("ns")){
            if(this.pageStarted)
                this.isNsTag = false;
            buf += "</" + qName + ">";
        } else {
            /**
             * close the tag if it is a child of page
             */
            if (this.pageStarted) {
                buf += "</" + qName + ">";
            }
        }
    }

    // To take specific actions for each chunk of character data (such as
    // adding the data to a node or buffer, or printing it to a file).
    @Override
    public void characters(char ch[], int start, int length)
            throws SAXException {
        if (this.pageStarted){
            if(this.isNsTag){
                String ns = new String(ch, start, length).replaceAll("\\s","");

                if(!ns.equalsIgnoreCase("0")){
                    this.pageStarted = false;
                    this.isNsTag = false;
                }
            }
            /**
             * adding char array to buffer only if page tag is open
             */
            this.buf += StringEscapeUtils.escapeXml11(new String(ch, start, length));
        }

    }
}
