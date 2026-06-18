package com.automationexercise.utils;

import com.automationexercise.config.ConfigReader;
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

import java.time.Duration;

/**
 * DriverManager
 * -------------
 * Thread-local WebDriver factory.
 * Each test thread gets its own driver instance — safe for parallel execution.
 *
 * Usage:
 *   DriverManager.initDriver();
 *   WebDriver driver = DriverManager.getDriver();
 *   DriverManager.quitDriver();
 */
public class DriverManager {

    private static final Logger log = LogManager.getLogger(DriverManager.class);
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    private DriverManager() { /* static utility class */ }

    // ── Initialise ───────────────────────────────────────────────────────────

    public static void initDriver() {
        ConfigReader cfg = ConfigReader.getInstance();
        String browser = cfg.getBrowser().toLowerCase().trim();
        boolean headless = cfg.isHeadless();

        log.info("Initialising {} driver (headless={})", browser, headless);

        WebDriver driver = switch (browser) {
            case "firefox" -> createFirefoxDriver(headless);
            case "edge"    -> createEdgeDriver(headless);
            default        -> createChromeDriver(headless);   // chrome is the default
        };

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(cfg.getImplicitWait()));
        driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(cfg.getPageLoadTimeout()));
        driver.manage().window().maximize();

        driverThreadLocal.set(driver);
        log.info("Driver initialised: {}", driver.getClass().getSimpleName());
    }

    // ── Retrieve ─────────────────────────────────────────────────────────────

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException(
                "WebDriver not initialised for this thread. Call DriverManager.initDriver() first.");
        }
        return driver;
    }

    // ── Quit ─────────────────────────────────────────────────────────────────

    public static void quitDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver != null) {
            driver.quit();
            driverThreadLocal.remove();
            log.info("Driver quit and removed from thread-local");
        }
    }

    // ── Private factory methods ───────────────────────────────────────────────

    private static WebDriver createChromeDriver(boolean headless) {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-blink-features=AutomationControlled");
        options.addArguments("--disable-extensions");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        if (headless) {
            options.addArguments("--headless=new");
            options.addArguments("--window-size=1280,800");
        }
        return new ChromeDriver(options);
    }

    private static WebDriver createFirefoxDriver(boolean headless) {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        if (headless) options.addArguments("--headless");
        return new FirefoxDriver(options);
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        if (headless) options.addArguments("--headless=new");
        return new EdgeDriver(options);
    }
}
