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

    /**
     * When processing large files in a loop,
     * String.concat()(+ operator) causes performance overhead
     * due to immutable nature of String objects in Java.
     * So a StringBuffer buffer is used instead of a String buffer.
     */
    private StringBuffer buf;
    private Boolean pageStarted = false;
    private Boolean isNsTag = false;
    private BufferProcessor processor;

    public PageExtractHandler(BufferProcessor processor){

        this.processor = processor;
        this.buf = new StringBuffer();
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
        } else if(qName.equalsIgnoreCase("ns")){
            if(this.pageStarted)
                this.isNsTag = true;
                buf.append("<" + qName + ">");
        } else {
            /**
             * start the tag if element is a child of page element
             */
            if (this.pageStarted) {
                buf.append( "<" + qName + ">");
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
            // empty the bufffer
            this.buf = new StringBuffer();
        } else if(qName.equalsIgnoreCase("ns")){
            if(this.pageStarted)
                this.isNsTag = false;
            buf.append("</" + qName + ">");
        } else {
            /**
             * close the tag if it is a child of page
             */
            if (this.pageStarted) {
                buf.append("</" + qName + ">");
            }
        }
    }

    /**
     * To take specific actions for each chunk of character data (such as
     * adding the data to a node or buffer, or printing it to a file).
     */

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
            this.buf.append(StringEscapeUtils.escapeXml11(new String(ch, start, length)));
        }

    }
}
