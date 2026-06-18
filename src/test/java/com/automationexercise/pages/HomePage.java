package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * HomePage
 * --------
 * Page Object for https://automationexercise.com/
 */
public class HomePage extends BasePage {

    // ── Locators (PageFactory @FindBy) ───────────────────────────────────────
    @FindBy(css = "a[href='/products']")
    private WebElement navProducts;

    @FindBy(css = "a[href='/view_cart']")
    private WebElement navCart;

    @FindBy(css = "a[href='/login']")
    private WebElement navLogin;

    @FindBy(css = "a[href='/logout']")
    private WebElement navLogout;

    @FindBy(css = "a:has-text('Logged in as')")
    private WebElement navLoggedInAs;

    // Use a more reliable CSS approach for "Logged in as" text
    @FindBy(xpath = "//a[contains(., 'Logged in as')]")
    private WebElement navLoggedInAsXpath;

    @FindBy(id = "susbscribe_email")
    private WebElement subscriptionEmailInput;

    @FindBy(id = "subscribe")
    private WebElement subscriptionSubmitBtn;

    @FindBy(id = "success-subscribe")
    private WebElement subscriptionSuccessMsg;

    // ── Constructor ──────────────────────────────────────────────────────────

    public HomePage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public HomePage open() {
        navigateTo("/");
        return this;
    }

    public LoginPage goToLogin() {
        click(navLogin);
        return new LoginPage(driver);
    }

    public ProductsPage goToProducts() {
        click(navProducts);
        return new ProductsPage(driver);
    }

    public CartPage goToCart() {
        click(navCart);
        return new CartPage(driver);
    }

    public void logout() {
        click(navLogout);
    }

    public void subscribeWithEmail(String email) {
        scrollIntoView(subscriptionEmailInput);
        type(subscriptionEmailInput, email);
        click(subscriptionSubmitBtn);
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    public HomePage assertHomePageVisible() {
        assertThat(driver.getCurrentUrl())
                .as("Home page URL")
                .isEqualTo("https://automationexercise.com/");
        return this;
    }

    public HomePage assertLoggedInAs(String username) {
        assertThat(isDisplayed(navLoggedInAsXpath))
                .as("'Logged in as' nav item should be visible")
                .isTrue();
        assertThat(getText(navLoggedInAsXpath))
                .as("Logged-in username in nav")
                .contains(username);
        return this;
    }

    public HomePage assertLogoutVisible() {
        assertThat(isDisplayed(navLogout))
                .as("Logout link should be visible")
                .isTrue();
        return this;
    }

    public HomePage assertSubscriptionSuccess() {
        assertThat(isDisplayed(subscriptionSuccessMsg))
                .as("Subscription success message")
                .isTrue();
        return this;
    }
}
