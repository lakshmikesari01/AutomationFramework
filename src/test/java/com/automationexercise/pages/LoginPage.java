package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * LoginPage
 * ---------
 * Page Object for /login — contains both the Login and New User Signup forms.
 */
public class LoginPage extends BasePage {

    // ── Login form ───────────────────────────────────────────────────────────
    @FindBy(css = "input[data-qa='login-email']")
    private WebElement loginEmailInput;

    @FindBy(css = "input[data-qa='login-password']")
    private WebElement loginPasswordInput;

    @FindBy(css = "button[data-qa='login-button']")
    private WebElement loginButton;

    @FindBy(xpath = "//p[contains(text(),'Your email or password is incorrect!')]")
    private WebElement loginErrorMsg;

    @FindBy(xpath = "//h2[contains(text(),'Login to your account')]")
    private WebElement loginHeader;

    // ── Signup form ──────────────────────────────────────────────────────────
    @FindBy(css = "input[data-qa='signup-name']")
    private WebElement signupNameInput;

    @FindBy(css = "input[data-qa='signup-email']")
    private WebElement signupEmailInput;

    @FindBy(css = "button[data-qa='signup-button']")
    private WebElement signupButton;

    @FindBy(xpath = "//p[contains(text(),'Email Address already exist!')]")
    private WebElement signupEmailExistsError;

    // ── Constructor ──────────────────────────────────────────────────────────

    public LoginPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public LoginPage open() {
        navigateTo("/login");
        return this;
    }

    public HomePage login(String email, String password) {
        type(loginEmailInput, email);
        type(loginPasswordInput, password);
        click(loginButton);
        return new HomePage(driver);
    }

    public SignupPage startSignup(String name, String email) {
        type(signupNameInput, name);
        type(signupEmailInput, email);
        click(signupButton);
        return new SignupPage(driver);
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    public LoginPage assertLoginPageVisible() {
        assertThat(isDisplayed(loginHeader))
                .as("Login page header should be visible")
                .isTrue();
        return this;
    }

    public LoginPage assertLoginError() {
        assertThat(isDisplayed(loginErrorMsg))
                .as("Login error message should be displayed for wrong credentials")
                .isTrue();
        return this;
    }

    public LoginPage assertSignupEmailExistsError() {
        assertThat(isDisplayed(signupEmailExistsError))
                .as("'Email Address already exist!' error should be shown")
                .isTrue();
        return this;
    }
}
