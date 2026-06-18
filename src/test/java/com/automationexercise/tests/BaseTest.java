package com.automationexercise.tests;

import com.automationexercise.utils.DriverManager;
import com.automationexercise.utils.ExtentReportManager;
import com.automationexercise.utils.ScreenshotHelper;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import java.lang.reflect.Method;

/**
 * BaseTest
 * --------
 * Parent of all test classes.
 * Handles:
 *   - WebDriver lifecycle (init before / quit after each test)
 *   - ExtentReports test node creation and status logging
 *   - Automatic screenshot capture on test failure
 */
public abstract class BaseTest {

    protected final Logger log = LogManager.getLogger(getClass());

    // ── Suite hooks ──────────────────────────────────────────────────────────

    @BeforeSuite(alwaysRun = true)
    public void initReport() {
        ExtentReportManager.getInstance();
        log.info("ExtentReports initialised for the suite");
    }

    @AfterSuite(alwaysRun = true)
    public void flushReport() {
        ExtentReportManager.flush();
        log.info("ExtentReports flushed — report written to disk");
    }

    // ── Test method hooks ────────────────────────────────────────────────────

    @BeforeMethod(alwaysRun = true)
    public void setUp(Method method) {
        DriverManager.initDriver();
        log.info("▶ Starting test: {}", method.getName());

        ExtentTest test = ExtentReportManager.getInstance()
                .createTest(method.getName());
        ExtentReportManager.setTest(test);
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown(ITestResult result) {
        ExtentTest test = ExtentReportManager.getTest();

        if (result.getStatus() == ITestResult.FAILURE) {
            log.error("✖ FAILED: {}", result.getName());
            if (test != null) {
                String screenshotPath = ScreenshotHelper.capture(
                        DriverManager.getDriver(), result.getName());
                test.log(Status.FAIL, result.getThrowable());
                if (screenshotPath != null) {
                    test.addScreenCaptureFromPath(screenshotPath, "Failure Screenshot");
                }
            }
        } else if (result.getStatus() == ITestResult.SKIP) {
            log.warn("⚠ SKIPPED: {}", result.getName());
            if (test != null) test.log(Status.SKIP, "Test skipped");
        } else {
            log.info("✔ PASSED: {}", result.getName());
            if (test != null) test.log(Status.PASS, "Test passed");
        }

        DriverManager.quitDriver();
    }

    // ── Helper accessor ──────────────────────────────────────────────────────

    protected WebDriver getDriver() {
        return DriverManager.getDriver();
    }
}
