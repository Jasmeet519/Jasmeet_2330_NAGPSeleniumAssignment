package com.nagarro.listeners;

import com.aventstack.extentreports.Status;
import com.nagarro.utils.Helper;
import com.nagarro.utils.Reporter;
import com.nagarro.utils.Screenshot;
import org.apache.logging.log4j.ThreadContext;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

import java.io.File;

public class ReportListener implements ITestListener {
    String reportPath;

    @Override
    public void onTestStart(ITestResult iTestResult) {
        System.out.println("onTestStart");
        Reporter.test.set(Reporter.extentReports.createTest(iTestResult.getMethod().getMethodName()));
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        System.out.println("onTestSuccess");
        Reporter.test.get().log(Status.PASS, "This test is Passed");
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        System.out.println("onTestFailure");
        Helper helper = new Helper();

        String date = helper.getCurrentDateTime().replace(" ", "_").replace(":", "");
        //String errorMsg = iTestResult.getThrowable().getMessage();

        String screenshotName = "Screenshot" + "_" + date + "_" + iTestResult.getMethod().getMethodName();
        String filePath = reportPath + File.separator + screenshotName + ".png";

        Reporter.test.get().log(Status.FAIL, "This test is Failed: " + iTestResult.getThrowable()+Reporter.test.get().addScreenCaptureFromPath(Screenshot.addScreenshot(iTestResult, filePath)));
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        System.out.println("onTestSkipped");
        Helper helper = new Helper();

        String date = helper.getCurrentDateTime().replace(" ", "_").replace(":", "");
        //String errorMsg = iTestResult.getThrowable().getMessage();

        String screenshotName = "Screenshot" + "_" + date + "_" + iTestResult.getMethod().getMethodName();
        String filePath = reportPath + File.separator + screenshotName + ".png";

        Reporter.test.get().log(Status.SKIP, "This test is Failed: " + iTestResult.getThrowable()+Reporter.test.get().addScreenCaptureFromPath(Screenshot.addScreenshot(iTestResult, filePath)));
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {
        ThreadContext.put("logFilename", Thread.currentThread().getName());
        System.out.println("onStart");
        reportPath = Reporter.intializeReport();
    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        System.out.println("onFinish");
        Reporter.finalizeReport();
    }
}
