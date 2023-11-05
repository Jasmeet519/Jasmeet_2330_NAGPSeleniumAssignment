package com.nagarro.testclient;


import com.nagarro.base.Driver;
import com.nagarro.utils.Helper;
import com.nagarro.listeners.ScreenshotListener;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.ThreadContext;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.testng.annotations.*;

import java.util.Properties;

@Listeners(ScreenshotListener.class)
public class TestClient {

    private WebDriver driver;
    private static Logger logger = LogManager.getLogger(TestClient.class);
    private Helper helper = new Helper();
    private String browser;
    private boolean headlessBrowser;
    private static Properties config;

    @BeforeClass(alwaysRun = true)
    public void initialize() {
        // Comment added for log4J2.xml
        ThreadContext.put("logFilename", Thread.currentThread().getName());

        if (driver == null) {
            setupDriver();
        }
    }

    @AfterClass(alwaysRun = true)
    public void reset() {
        Driver.quitDriver();
    }


    public void setupDriver() {
        config = helper.readConfigFile();
        browser = config.getProperty("browser").trim();
        logger.info("Value of 'browser' key in config file is: " + browser);

        headlessBrowser = Boolean.parseBoolean(config.getProperty("headlessBrowser"));
        logger.info("Value of 'headlessBrowser' key in config file is: " + headlessBrowser);

        switch (browser) {
            case "chrome":
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("start-maximized");
                chromeOptions.addArguments("--ignore-certificate-errors");
                chromeOptions.addArguments("--disable-popup-blocking");
                chromeOptions.addArguments("disable-infobars");
                chromeOptions.addArguments("--incognito");
                if (headlessBrowser)
                    chromeOptions.addArguments("--headless");

                driver = Driver.initializeChromeDriver(chromeOptions);
                break;

            case "firefox":
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headlessBrowser)
                    firefoxOptions.addArguments("--headless");

                driver = Driver.initializeFirefoxDriver(firefoxOptions);
                break;
            case "edge":
                driver = Driver.initializeEdgeDriver();
                driver.manage().window().maximize();
                break;
            default:
                logger.fatal("Value of 'browser' key in config file is incorrect. Please check config.properties file again!");
                break;

        }
        if (driver != null)
            driver.manage().deleteAllCookies();
    }
}
