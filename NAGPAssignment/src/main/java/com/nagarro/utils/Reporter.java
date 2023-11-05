package com.nagarro.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Reporter {
    public static ThreadLocal<ExtentSparkReporter> sparkReporterThread = new ThreadLocal<>();
    public static ThreadLocal<ExtentReports> extentReportsThread = new ThreadLocal<>();
    public static ThreadLocal<ExtentTest> test = new ThreadLocal<>();

    public static ExtentSparkReporter sparkReporter;
    public static ExtentReports extentReports;

    /**
     * Initialize Extent Report and Move any existing Report to Archive Folder
     * @return
     */
    public static String intializeReport() {

        // Path where current report generates
        String path = System.getProperty("user.dir") +
                File.separator + "htmlReports" +
                File.separator + "reports" +
                File.separator + "Current Test Report";

        // Path where old reports are archived
        String archive_path = System.getProperty("user.dir") +
                File.separator + "htmlReports" +
                File.separator + "reports" +
                File.separator + "Archived Test Reports";

        // Moving the Existing Reports to Archived Folder
        File src_directory = new File(path);
        File target_directory = new File(archive_path);
        try {
            if (src_directory.listFiles().length != 0) {
                FileUtils.copyDirectory(src_directory, target_directory);
                FileUtils.cleanDirectory(src_directory);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        String dirPath = path + File.separator + getCurrentDateTime().replace(" ", "_").replace(":", "");
        String filePath = dirPath + File.separator + "TestReport.html";

        // Initialize Spark Reporter
        sparkReporterThread.set(new ExtentSparkReporter(filePath));
        sparkReporter = sparkReporterThread.get();

        // Initialize Extent Report and attach to Spark Reporter
        extentReportsThread.set(new ExtentReports());
        extentReports = extentReportsThread.get();
        extentReports.attachReporter(sparkReporter);

        extentReports.setSystemInfo("Author", "Jasmeet Chadha");
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        return dirPath;
    }

    /**
     * Helps to Finalize Report
     */
    public static void finalizeReport() {
        extentReports.flush();
    }

    /**
     * Helps to get Current DateTime
     *
     * @return current datetime in 'yyyyMMdd HH:mm:ss' format
     */
    public static String getCurrentDateTime() {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd HH:mm:ss");
        LocalDateTime now = LocalDateTime.now();
        return dtf.format(now);
    }

}
