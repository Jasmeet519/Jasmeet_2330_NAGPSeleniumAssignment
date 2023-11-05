package com.nagarro.utils;

import com.nagarro.base.BasePageObject;
import com.nagarro.base.Driver;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Properties;

public class TestDataReader {
    private static ThreadLocal<String> testDataFileName = new ThreadLocal<>();
    private static Logger logger = LogManager.getLogger(TestDataReader.class);
    private static XMLParser xmlParser = new XMLParser();

    public static Helper helper = new Helper();
    public static Properties configFile = helper.readConfigFile();

    public static void init() {
        testDataFileName.set(configFile.getProperty("testDataFileName"));
    }

    /**
     * Helps to read xml and store data in map
     * @param tagXpath is the node name for which map needs to be generated
     * @return result Map having (nodeName,nodeValue) of all child nodes present in tagXpath
     */
    public static HashMap<String, String> getDataMap(String tagXpath) {

        HashMap<String, String> resultMap = new HashMap<>();
        if (tagXpath.trim().isEmpty() || tagXpath.trim() == null) {
            throw new IllegalArgumentException("Xpath(Parameter) for data cannot be empty or null");
        } else {
            String[] mydata = null;

            // Parse nodes by using forward slash (preferred)
            if (tagXpath.contains("/")) {
                mydata = tagXpath.split("/");
            } else {
                // Parse by using back slashes
                mydata = tagXpath.split("'\'");
            }

            if (testDataFileName == null)
                testDataFileName.set(configFile.getProperty("testDataFileName"));

            logger.debug(
                    "Attempting to build hashmap based on file '"
                            + testDataFileName
                            + "' and Node = "
                            + mydata[0]);

            HashMap<String, Object> test = (HashMap<String, Object>) xmlParser.parseXML(testDataFileName.get(), mydata[0]);
            if (test.isEmpty()) {
                throw new IllegalArgumentException(
                        "Failed to build the hashmap based on input XML path. Please check the configuration.");
            } else if (mydata.length > 1) {
                for (int i = 1; i < mydata.length; i++) {
                    test = (HashMap<String, Object>) test.get(mydata[i - 1]);
                    if (i == mydata.length - 1) {
                        if (test.get(mydata[i]) == null || test.get(mydata[i]).toString().isEmpty()) {
                            throw new IllegalArgumentException("No Test Data Node found for the given XPath !");
                        } else {
                            resultMap = (HashMap<String, String>) test.get(mydata[i]);
                        }
                    }
                }
            } else {
                resultMap = (HashMap<String, String>) test.get(mydata[0]);
            }
        }
        return resultMap;
    }

    /**
     * Helps to read xml and store data in map
     * @param fileName name of xml file to read
     * @param tagXpath is the node name for which map needs to be generated
     * @return result Map having (nodeName,nodeValue) of all child nodes present in tagXpath in fileName.xml
     */
    public static HashMap<String, String> getDataMap(String fileName, String tagXpath) {

        HashMap<String, String> resultMap = new HashMap<>();
        if (tagXpath.trim().isEmpty() || tagXpath.trim() == null) {
            throw new IllegalArgumentException("Xpath(Parameter) for data cannot be empty or null");
        } else {
            String[] mydata = null;

            // Parse nodes by using forward slash (preferred)
            if (tagXpath.contains("/")) {
                mydata = tagXpath.split("/");
            } else { // Parse by using back slashes
                mydata = tagXpath.split("'\'");
            }

            if (testDataFileName == null)
                testDataFileName.set(fileName);

            logger.debug(
                    "Attempting to build hashmap based on file '"
                            + testDataFileName
                            + "' and Node = "
                            + mydata[0]);

            HashMap<String, Object> test = (HashMap<String, Object>) xmlParser.parseXML(testDataFileName.get(), mydata[0]);
            if (test.isEmpty()) {
                throw new IllegalArgumentException(
                        "Failed to build the hashmap based on input XML path. Please check the configuration.");
            } else if (mydata.length > 1) {
                for (int i = 1; i < mydata.length; i++) {
                    test = (HashMap<String, Object>) test.get(mydata[i - 1]);
                    if (i == mydata.length - 1) {
                        if (test.get(mydata[i]) == null || test.get(mydata[i]).toString().isEmpty()) {
                            throw new IllegalArgumentException("No Test Data Node found for the given XPath !");
                        } else {
                            resultMap = (HashMap<String, String>) test.get(mydata[i]);
                        }
                    }
                }
            } else {
                resultMap = (HashMap<String, String>) test.get(mydata[0]);
            }
        }
        return resultMap;
    }
}
