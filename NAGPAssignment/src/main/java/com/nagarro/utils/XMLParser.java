package com.nagarro.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class XMLParser {
    private Logger logger = LogManager.getLogger(getClass());
    private Document sourceDocument;
    private Document currentDocument;
    private final String appNode = "APP_localhost";

    /**
     * Method to parse XML data from a file based on a given xPath/tagname value.
     *
     * @param filePath The XML file to read data from
     * @param tagname  A tag name in the xml file
     * @return Map containing the node data
     */
    public Map parseXML(String filePath, String tagname) {
        HashMap result = new HashMap();

        // Open Input Stream
        InputStream fileStream = getClass().getResourceAsStream("/" + filePath);
        if (fileStream == null)
            logger.error("Problem reading the XML data file! Check to see if the file exists.");
        else {
            try {
                // Create a DocumentBuilder
                DocumentBuilderFactory sourceFactory = DocumentBuilderFactory.newInstance();
                try {
                    // Create a Document from 'fileStream' stream
                    DocumentBuilder sourceBuilder = sourceFactory.newDocumentBuilder();
                    sourceDocument = sourceBuilder.parse(fileStream);
                } catch (ParserConfigurationException e) {
                    logger.error("Problem reading XML data file!");
                    e.printStackTrace();
                }

                // Getting Document with Valid Node i.e. '<APP_localhost>'
                sourceDocument = getDocumentWithValidNode(sourceDocument);
                currentDocument = sourceDocument;
                //logger.info("Root Element: " + sourceDocument.getFirstChild().getNodeName());

                NodeList resultNodeList = sourceDocument.getElementsByTagName(tagname);
                XMLParser.MyNodeList tempNodeList = new MyNodeList();
                String emptyNodeName = null;
                String emptyNodeValue = null;


                for (int index = 0; index < resultNodeList.getLength(); index++) {
                    Node tempNode = resultNodeList.item(index);
                    if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                        tempNodeList.addNode(tempNode);
                    }
                    emptyNodeName = tempNode.getNodeName();
                    emptyNodeValue = tempNode.getNodeValue();
                }

                if (tempNodeList.getLength() == 0 && emptyNodeName != null && emptyNodeValue != null) {
                    result.put(emptyNodeName, emptyNodeValue);
                } else {
                    this.parseXmlNode(tempNodeList, result);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            } finally {
                try {
                    fileStream.close();
                } catch (IOException e) {
                    logger.error("Failed close Input stream object");
                }

            }
        }

        // TODO: UnComment below code when debugging the code to check the Map generated for tagname/XMLNode
        /*logger.info("Map generated for [FileName:  " + filePath + ", TagName: " + tagname + "] is: ");
        result.forEach((key,value)->{
            logger.info(key+"="+value);
        });*/

        return result;
    }

    /**
     * Parse list of ELEMENT_NODE in the XML file to get data as map.
     *
     * @param nodeList List of nodes that are ELEMENT_NODE
     * @param result   HashMap that stores the result of processing
     */
    private void parseXmlNode(NodeList nodeList, HashMap result) {
        // Iterate through each node and build the map for all the child nodes and sub-nodes

        for (int i = 0; i < nodeList.getLength(); i++) {
            // If Node has child nodes
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.hasChildNodes()
                    && node.getFirstChild() != null
                    && node.getFirstChild().getNextSibling() != null
                    || (node.getFirstChild() != null && node.getFirstChild().hasChildNodes())) {
                NodeList childNodes = node.getChildNodes();
                XMLParser.MyNodeList tempNodeList = new XMLParser.MyNodeList();
                for (int index = 0; index < childNodes.getLength(); index++) {
                    Node tempNode = childNodes.item(index);
                    if (tempNode.getNodeType() == Node.ELEMENT_NODE) {
                        tempNodeList.addNode(tempNode);
                    }
                }
                HashMap dataHashMap = new HashMap();
                result.put(node.getNodeName(), dataHashMap);

                // Recursive function is call
                this.parseXmlNode(tempNodeList, dataHashMap);
            } else if (node.getNodeType() == Node.ELEMENT_NODE
                    && node.hasChildNodes()
                    && node.getFirstChild() != null
                    && node.getFirstChild().getNextSibling() != null) {
                this.putValue(result, node);
            } else if (node.getNodeType() == Node.ELEMENT_NODE) {
                this.putValue(result, node);
            }
        } // end for loop
    }

    /**
     * Method to set a node value  *
     *
     * @param result will be the original value or else an evaluated value if the value is an xpath
     * @param node   Original node value to be checked.
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    private void putValue(HashMap result, Node node) {
        Object nodeValue;
        if (node.getFirstChild() != null) {
            nodeValue = node.getFirstChild().getNodeValue();
        } else {
            nodeValue = "";
        }
        Object putNode = nodeValue;
        result.put(node.getNodeName(), putNode);
    }

    public Document getDocumentWithValidNode(Document document) {
        NodeList validNodeList = document.getElementsByTagName(appNode);
        if (validNodeList != null & validNodeList.getLength() > 0) {
            Node rootNode = validNodeList.item(0);
            NodeList childNodeList = rootNode.getParentNode().getChildNodes();
            for (int i = 0; i < childNodeList.getLength(); i++) {
                Node childNode = childNodeList.item(i);
                String childNodeName = childNode.getNodeName();
                if (!childNodeName.equalsIgnoreCase(appNode) && !childNodeName.startsWith("#"))
                    childNode.getParentNode().removeChild(childNode);
            }
        } else logger.fatal("Failed to retrieve the node " + appNode + "from the XML.");
        return document;
    }


    static class MyNodeList implements NodeList {
        List<Node> nodes = new ArrayList<>();
        int length = 0;

        public void addNode(Node node) {
            nodes.add(node);
            length++;
        }

        @Override
        public Node item(int index) {
            try {
                return nodes.get(index);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        public int getLength() {
            return length;
        }
    }


}
