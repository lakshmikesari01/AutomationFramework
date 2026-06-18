package com.automationexercise.pages;

import com.automationexercise.config.ConfigReader;
import com.automationexercise.utils.WaitHelper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;

/**
 * BasePage
 * --------
 * Parent of every Page Object class.
 * Provides shared navigation, interaction, and assertion helpers so that
 * child pages stay concise and focused on their own locators/actions.
 */
public abstract class BasePage {

    protected final WebDriver driver;
    protected final WaitHelper wait;
    protected final Logger log = LogManager.getLogger(getClass());
    private final String baseUrl;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WaitHelper(driver);
        this.baseUrl = ConfigReader.getInstance().getBaseUrl();
        PageFactory.initElements(driver, this);
    }

    // ── Navigation ──────────────────────────────────────────────────────────

    protected void navigateTo(String path) {
        String url = baseUrl + "/" + path.replaceFirst("^/", "");
        log.info("Navigating to: {}", url);
        driver.get(url);
    }

    public String getCurrentUrl() {
        return driver.getCurrentUrl();
    }

    public String getPageTitle() {
        return driver.getTitle();
    }

    // ── Interactions ─────────────────────────────────────────────────────────

    protected void click(WebElement element) {
        wait.waitForClickable(element).click();
    }

    protected void click(By locator) {
        wait.waitForClickable(locator).click();
    }

    protected void type(WebElement element, String text) {
        wait.waitForVisible(element).clear();
        element.sendKeys(text);
    }

    protected void selectByValue(WebElement element, String value) {
        new Select(wait.waitForVisible(element)).selectByValue(value);
    }

    protected void selectByVisibleText(WebElement element, String text) {
        new Select(wait.waitForVisible(element)).selectByVisibleText(text);
    }

    protected void scrollIntoView(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    protected void acceptAlert() {
        wait.waitForAlertAndAccept(driver);
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    protected boolean isDisplayed(WebElement element) {
        try {
            return element.isDisplayed();
        } catch (Exception e) {
            return false;
        }
    }

    protected boolean isDisplayed(By locator) {
        return wait.isElementVisible(locator);
    }

    protected String getText(WebElement element) {
        return wait.waitForVisible(element).getText().trim();
    }
}
