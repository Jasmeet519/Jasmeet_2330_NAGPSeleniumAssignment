package com.nagarro.utils;

import com.nagarro.listeners.ScreenshotListener;
import com.nagarro.base.Driver;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;

public class Screenshot {
    static Logger logger = LogManager.getLogger(ScreenshotListener.class);
    private Helper helper = new Helper();

    /**
     * Helps to take screenshot
     * @param iTestResult describes the result of a test
     * @return path of Screenshot
     */
    public String addScreenshot(ITestResult iTestResult) {
        logger.error("onTestFailure - TEST_FAILURE: " + iTestResult.getThrowable());
        String date = helper.getCurrentDateTime().replace(" ", "_").replace(":", "");
        String errorMsg = iTestResult.getThrowable().getMessage().replaceAll(" ", "_").replaceAll("[^\\\\.A-Za-z0-9_]", "").replaceAll("\\.", "").replaceAll("\\\\", "");

        String screenshotName = "Screenshot" + "_" + date + "_" + iTestResult.getName() + "_" + errorMsg;
        String filePath = System.getProperty("user.dir") + File.separator + "Screenshots" + File.separator + screenshotName + ".PNG";

        TakesScreenshot scrShot = ((TakesScreenshot) Driver.getDriverInstance());
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File DestFile = new File(filePath);

        try {
            //Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);
            logger.info("Screenshot saved successfully - " + screenshotName);
            logger.info("PATH: " + DestFile);
        } catch (IOException e) {
            logger.error("Failed to save screenshot...");
        }
        return DestFile.getAbsolutePath();
    }

    /**
     * Helps to take screenshot
     * @param iTestResult describes the result of a test
     * @param filePath is the path where screenshot needs to be saved
     * @return path of Screenshot
     */
    public static String addScreenshot(ITestResult iTestResult, String filePath) {
        logger.error("onTestFailure - TEST_FAILURE: " + iTestResult.getThrowable());
        TakesScreenshot scrShot = ((TakesScreenshot) Driver.getDriverInstance());
        File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);

        //Move image file to new destination
        File DestFile = new File(filePath);

        try {
            //Copy file at destination
            FileUtils.copyFile(SrcFile, DestFile);
            logger.info("Screenshot saved successfully at path- " + filePath);
            logger.info("PATH: " + filePath);
        } catch (IOException e) {
            logger.error("Failed to save screenshot...");
        }
        return filePath;
    }
}
