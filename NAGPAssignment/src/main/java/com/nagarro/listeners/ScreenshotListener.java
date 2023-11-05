package com.nagarro.listeners;

import com.nagarro.utils.Screenshot;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ScreenshotListener implements ITestListener {

    @Override
    public void onTestStart(ITestResult iTestResult) {

    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {

    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        Screenshot screenshot = new Screenshot();
        screenshot.addScreenshot(iTestResult);

    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        Screenshot screenshot = new Screenshot();
        screenshot.addScreenshot(iTestResult);
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {

    }



}
