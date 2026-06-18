package com.automationexercise.utils;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ExtentReportManager
 * -------------------
 * Thread-safe singleton that manages one ExtentReports instance per run
 * and one ExtentTest per thread (for parallel-safe logging).
 */
public class ExtentReportManager {

    private static ExtentReports extent;
    private static final ThreadLocal<ExtentTest> testThread = new ThreadLocal<>();

    private ExtentReportManager() {}

    public static synchronized ExtentReports getInstance() {
        if (extent == null) {
            String timestamp = LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String reportPath = "reports/ExtentReport_" + timestamp + ".html";

            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath);
            spark.config().setTheme(Theme.STANDARD);
            spark.config().setDocumentTitle("AutomationExercise Regression Report");
            spark.config().setReportName("Selenium Java Framework — Test Results");
            spark.config().setTimeStampFormat("dd MMM yyyy HH:mm:ss");

            extent = new ExtentReports();
            extent.attachReporter(spark);
            extent.setSystemInfo("Framework",  "Selenium 4 + TestNG");
            extent.setSystemInfo("Language",   "Java 17");
            extent.setSystemInfo("Browser",    ConfigReader.getInstance().getBrowser());
            extent.setSystemInfo("Target URL", ConfigReader.getInstance().getBaseUrl());
        }
        return extent;
    }

    public static ExtentTest getTest() {
        return testThread.get();
    }

    public static void setTest(ExtentTest test) {
        testThread.set(test);
    }

    public static void flush() {
        if (extent != null) extent.flush();
    }

    // Inner import to avoid circular dependency
    private static class ConfigReader {
        static com.automationexercise.config.ConfigReader getInstance() {
            return com.automationexercise.config.ConfigReader.getInstance();
        }
    }
}
