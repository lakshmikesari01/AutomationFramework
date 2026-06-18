package com.automationexercise.config;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * ConfigReader
 * ------------
 * Singleton that loads config.properties once and exposes typed getters.
 * System properties override file values, enabling CI/CD parameterisation:
 *   mvn test -Dbrowser=firefox -Dheadless=true
 */
public class ConfigReader {

    private static final Logger log = LogManager.getLogger(ConfigReader.class);
    private static ConfigReader instance;
    private final Properties props = new Properties();

    private ConfigReader() {
        try (InputStream in = getClass().getClassLoader()
                .getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new RuntimeException("config.properties not found on classpath");
            }
            props.load(in);
            log.info("config.properties loaded successfully");
        } catch (IOException e) {
            throw new RuntimeException("Failed to load config.properties", e);
        }
    }

    public static ConfigReader getInstance() {
        if (instance == null) {
            instance = new ConfigReader();
        }
        return instance;
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    /** Returns a property, with System property taking precedence. */
    public String get(String key) {
        return System.getProperty(key, props.getProperty(key));
    }

    public String getBaseUrl() {
        return get("base.url");
    }

    public String getBrowser() {
        return get("browser");
    }

    public boolean isHeadless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public int getImplicitWait() {
        return Integer.parseInt(get("implicit.wait"));
    }

    public int getExplicitWait() {
        return Integer.parseInt(get("explicit.wait"));
    }

    public int getPageLoadTimeout() {
        return Integer.parseInt(get("page.load.timeout"));
    }

    public String getReportsDir() {
        return get("reports.dir");
    }

    public String getScreenshotsDir() {
        return get("screenshots.dir");
    }
}
