import extractors.PageExtractHandler;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import processors.BufferProcessor;
import processors.ElasticsearchWriter;
import processors.RandomWriter;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        try {
            /**
             * SAX (Simple API for XML) is used to parse Wikipedia XML Dump.
             * Its event based API is more suitable to parse large XML file
             * and extract data from the file due to its low memory requirements.
             */
            SAXParser parser = SAXParserFactory.newInstance().newSAXParser();

            /**
             * A processor object is used for processing buffer
             * that is created in custom event handler class for SAXParser.
             * It creates an JSON String from buffer and writes it to Elasticsearch.
             */
            BufferProcessor processor = new ElasticsearchWriter();

            /**
             * A custom event handler class for SAXParser.
             * It extracts only page data and save it to the buffer as a XML string.
             */
            DefaultHandler handler = new PageExtractHandler(processor);
            System.out.println("Starting to parse XML dump ");
            parser.parse("/home/umutcan/Downloads/enwiki-20140903-pages-articles.xml", handler);
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        }

    }
}

