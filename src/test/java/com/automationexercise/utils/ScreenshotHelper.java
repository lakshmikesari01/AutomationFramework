package com.automationexercise.utils;

import com.automationexercise.config.ConfigReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * ScreenshotHelper
 * ----------------
 * Captures a PNG screenshot and saves it to reports/screenshots/.
 * Called automatically by the BaseTest listener on failure.
 */
public class ScreenshotHelper {

    private static final Logger log = LogManager.getLogger(ScreenshotHelper.class);
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss_SSS");

    private ScreenshotHelper() {}

    /**
     * Takes a full-page screenshot and returns the absolute file path,
     * or null if capture fails.
     */
    public static String capture(WebDriver driver, String testName) {
        String dir = ConfigReader.getInstance().getScreenshotsDir();
        try {
            Files.createDirectories(Paths.get(dir));
            String timestamp = LocalDateTime.now().format(FORMATTER);
            String fileName = sanitise(testName) + "_" + timestamp + ".png";
            Path dest = Paths.get(dir, fileName);

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), dest);

            log.info("Screenshot saved: {}", dest.toAbsolutePath());
            return dest.toAbsolutePath().toString();
        } catch (IOException e) {
            log.warn("Could not save screenshot: {}", e.getMessage());
            return null;
        }
    }

    private static String sanitise(String name) {
        return name.replaceAll("[^a-zA-Z0-9_\\-]", "_");
    }
}
