package com.nagarro.utils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Helper {
    public static Logger logger = LogManager.getLogger(Helper.class);

    /**
     * Helps to read config.properties file
     * @return Properties object of config.properties file
     */
    public Properties readConfigFile() {
        final String RESOURCE_NAME = "/config.properties";
        Properties properties = new Properties();

        try {
            InputStream inputStream = getClass().getResourceAsStream(RESOURCE_NAME);
            properties.load(inputStream);
        } catch (IOException ioException) {
            logger.error(ioException);
            throw new RuntimeException("Problem reading config file");
        }
        return properties;
    }

    /**
     * Helps to read properties file
     * @param fileName name of properties file
     * @return Properties object of fileName.properties file
     */
    public Properties readConfigFile(String fileName) {
        final String RESOURCE_NAME = "/" + fileName + ".properties";
        logger.info("Reading '" + RESOURCE_NAME + "' file...");
        Properties properties = new Properties();

        try {
            InputStream inputStream = getClass().getResourceAsStream(RESOURCE_NAME);
            properties.load(inputStream);
        } catch (IOException ioException) {
            logger.error(ioException);
            throw new RuntimeException("Problem reading config file");
        }
        return properties;
    }

    /**
     * Helps to read locator file 'fileName'
     * @param fileName name of locator file
     * @return Properties object of fileName.locators file
     */
    public Properties readLocatorFile(String fileName) {
        final String RESOURCE_NAME = "/" + fileName + ".locators";
        logger.info("Reading '" + RESOURCE_NAME + "' file...");
        Properties properties = new Properties();

        try {
            InputStream inputStream = getClass().getResourceAsStream(RESOURCE_NAME);
            properties.load(inputStream);
        } catch (IOException ioException) {
            logger.error(ioException);
            throw new RuntimeException("Problem reading locator file");
        }
        return properties;
    }

    /**
     * Help reads csv file and store each data row in List of Map
     * @param filePath path of csv file
     * @return list of Map wher Key is the csv header and value is the data
     */
    public List<Map<String, String>> readCSVFile(String filePath) {
        logger.info("Reading '" + filePath + "' file...");
        List<Map<String, String>> csvData = new ArrayList<>();
        try {
            Stream<String> lines = Files.lines(Paths.get(filePath));
            List<String[]> csvRows = lines.map(line -> line.split(",")).collect(Collectors.toList());

            String[] csvHeader = csvRows.get(0);

            for (int i = 1; i < csvRows.size(); i++) {
                Map<String, String> map = new LinkedHashMap<>();

                for (int j = 0; j < csvRows.get(0).length; j++) {
                    map.put(csvHeader[j], csvRows.get(i)[j]);
                }
                csvData.add(map);
            }
            lines.close();
            logger.info("CSV Data: " + csvData);
            return csvData;
        } catch (IOException e) {
            logger.error("Failed to read csv file: " + e);
            throw new RuntimeException(e);
        }

    }

    /**
     * Helps to get Current DateTime
     *
     * @return current datetime in 'yyyyMMdd HH:mm:ss' format
     */
    public String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }
}
