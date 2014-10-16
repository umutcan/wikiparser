wikiparser
==========

A tool for extracting data from Wikipedia XML Dumps. 


## Requirements

This project is written in Java 7. It requires [Elasticsearch Java Client](http://www.elasticsearch.org/guide/en/elasticsearch/client/java-api/current/index.html), 
[json.org JSON](http://www.json.org/java/index.html) library 
and [Apache Commons Lang](http://commons.apache.org/proper/commons-lang/) library. 
A pom.xml is included into this repository in order to manage dependencies with Maven2. You can check [Maven in 5 Minutes](http://maven.apache.org/guides/getting-started/maven-in-five-minutes.html) document for details and basic usage.


There is only one processor right now. It transform the article XML string to a JSON string and stores it to a local Elasticsearch server.
So, a local Elasticsearch is required. 
You can check the [Elasticsearch setup document](http://www.elasticsearch.org/guide/en/elasticsearch/reference/current/setup.html)


## Building Project

You can build this project easily by using Maven2:

    mvn clean package

This command create ``wikiparser-1.0-SNAPSHOT.jar`` under the ``target`` folder.


## Running Project

After building .jar file, you can run program with following command:

    java -cp /path/to/wikiparser-1.0-SNAPSHOT.jar Main <path>
