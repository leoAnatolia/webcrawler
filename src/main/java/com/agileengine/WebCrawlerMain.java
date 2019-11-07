package com.agileengine;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/*
    java -jar <your_bundled_app>.jar <input_origin_file_path> <input_other_sample_file_path>

        // Jsoup requires an absolute file path to resolve possible relative paths in HTML,
        // so providing InputStream through classpath resources is not a case
        //String originalHTMLFilePath = "./samples/startbootstrap-freelancer-gh-pages-cut.html";

Assumptions:
    - Some runtime exceptions(e.g. NullPointerException,etc...) are not handled while it is a test program.
 */
public class WebCrawlerMain {

    private static Logger LOGGER = LoggerFactory.getLogger(WebCrawlerMain.class);

    private static String CHARSET_NAME = "utf8";

    static String targetElementId = "make-everything-ok-button";


    public static void main(String[] args) {

        String originalHTMLFilePath = args[0], targetHTMLFilePath=args[1];

        Optional<Element> buttonOpt = findElementById(new File(originalHTMLFilePath), targetElementId);


        if(!buttonOpt.isPresent()){
            System.out.println("targetElementId: "+targetElementId+" is not found in file: "+originalHTMLFilePath);
            return;
        }


        Map<String,String> hashMappedAttributes = buttonOpt.get().attributes().asList()
                .stream()
                .collect(Collectors.toMap(attr -> attr.getKey(), attr -> attr.getValue()));

        //LOGGER.debug("hashMappedAttributes: "+hashMappedAttributes);
        //LOGGER.debug("title="+hashMappedAttributes.get("title"));

        String titleValue = hashMappedAttributes.get("title");

        Element e = getHittingElement(targetHTMLFilePath, hashMappedAttributes, titleValue);

        System.out.println(e);


    }

    private static Element getHittingElement(String targetHTMLFilePath, Map<String, String> hashMappedAttributes, String titleValue) {
        List<Element> hittingElements = findElementsByAttribute(new File(targetHTMLFilePath),"title",titleValue);

        if (hittingElements.size()>1){
            String classValue = hashMappedAttributes.get("class");

            for (Element e:hittingElements
                 ) {

                Map<String,String> attributes = e.attributes().asList()
                        .stream()
                        .collect(Collectors.toMap(attr -> attr.getKey(), attr -> attr.getValue()));

                if (classValue.equals(attributes.get("class"))){
                    return e;
                }
            }
        }
        return null;
    }

    private static Optional<Element> findElementById(File htmlFile, String targetElementId) {

            Document doc = parseHTMLFile(htmlFile);
            if(doc==null) return Optional.empty();

            Element e = doc.getElementById(targetElementId);
            if(e==null) return Optional.empty();

            return Optional.of(doc.getElementById(targetElementId));

    }


    private static Elements findElementsByAttribute(File htmlFile, String attrKey, String attrValue) {

            Document doc = parseHTMLFile(htmlFile);

            Elements elements = doc.getElementsByAttributeValue(attrKey,attrValue);

            return elements;

    }


    private static Document parseHTMLFile(File htmlFile){

        try {
            Document doc = Jsoup.parse(
                    htmlFile,
                    CHARSET_NAME,
                    htmlFile.getAbsolutePath());


            return doc;

        } catch (IOException e) {
            LOGGER.error("Error reading [{}] file", htmlFile.getAbsolutePath(), e);
            return null;
        }

    }


}