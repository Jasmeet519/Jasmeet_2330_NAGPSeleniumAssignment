package com.nagarro.base;

import com.nagarro.utils.Helper;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;

import java.util.Properties;

public class Driver {

    public static ThreadLocal<WebDriver> driver = new ThreadLocal<>();
    public static Logger logger = LogManager.getLogger(Driver.class);
    public Helper helper = new Helper();
    public Properties configFile = helper.readConfigFile();


    /**
     * Helps to Initialize Chrome Driver
     * @param options are ChromeOptions
     * @return ChromeDriver object
     */
    public static WebDriver initializeChromeDriver(ChromeOptions options) {
        logger.info("Initializing Chrome Driver...");
        WebDriverManager.chromedriver().setup();
        if (options != null)
            driver.set(new ChromeDriver(options));
        else driver.set(new ChromeDriver());

        return getDriverInstance();
    }

    /**
     *  Helps to Initialize Firefox Driver
     * @param options are FirefoxOptions
     * @return FirefoxDriver object
     */
    public static WebDriver initializeFirefoxDriver(FirefoxOptions options) {
        logger.info("Initializing Firefox Driver...");
        WebDriverManager.firefoxdriver().setup();
        if (options != null)
            driver.set(new FirefoxDriver(options));
        else driver.set(new FirefoxDriver());
        return getDriverInstance();
    }

    /**
     * Helps to Initialize Edge Driver
     *
     * @return EdgeDriver object
     */
    public static WebDriver initializeEdgeDriver() {
        logger.info("Initializing IE Edge Driver...");
        WebDriverManager.edgedriver().setup();
        driver.set(new EdgeDriver());
        return getDriverInstance();
    }

    /**
     * Helps to get Driver instance
     * @return return WebDriver
     */
    public static WebDriver getDriverInstance() {
        return driver.get();
    }

    /**
     * Helps to Close Driver
     */
    public static void closeDriver(){
        logger.info("Closing driver instance");
        getDriverInstance().close();
    }

    /**
     * Helps to quit driver
     */
    public static void quitDriver(){
        logger.info("Closing all window tabs");
        getDriverInstance().quit();
    }


}
