package com.automationexercise.pages;

import com.automationexercise.testdata.TestDataGenerator.UserData;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * SignupPage
 * ----------
 * Page Object for the full Account Information / Registration form (/signup).
 * Reached after submitting the 'New User Signup' mini-form on /login.
 */
public class SignupPage extends BasePage {

    // ── Account information section ──────────────────────────────────────────
    @FindBy(id = "id_gender1")
    private WebElement titleMrRadio;

    @FindBy(css = "input[data-qa='password']")
    private WebElement passwordInput;

    @FindBy(css = "select[data-qa='days']")
    private WebElement dobDaySelect;

    @FindBy(css = "select[data-qa='months']")
    private WebElement dobMonthSelect;

    @FindBy(css = "select[data-qa='years']")
    private WebElement dobYearSelect;

    // ── Address details section ──────────────────────────────────────────────
    @FindBy(css = "input[data-qa='first_name']")
    private WebElement firstNameInput;

    @FindBy(css = "input[data-qa='last_name']")
    private WebElement lastNameInput;

    @FindBy(css = "input[data-qa='company']")
    private WebElement companyInput;

    @FindBy(css = "input[data-qa='address']")
    private WebElement addressInput;

    @FindBy(css = "select[data-qa='country']")
    private WebElement countrySelect;

    @FindBy(css = "input[data-qa='state']")
    private WebElement stateInput;

    @FindBy(css = "input[data-qa='city']")
    private WebElement cityInput;

    @FindBy(css = "input[data-qa='zipcode']")
    private WebElement zipcodeInput;

    @FindBy(css = "input[data-qa='mobile_number']")
    private WebElement mobileInput;

    @FindBy(css = "button[data-qa='create-account']")
    private WebElement createAccountBtn;

    @FindBy(css = "h2[data-qa='account-created']")
    private WebElement accountCreatedHeader;

    @FindBy(css = "a[data-qa='continue-button']")
    private WebElement continueButton;

    @FindBy(xpath = "//h2[contains(text(),'Enter Account Information')]")
    private WebElement signupFormHeader;

    // ── Constructor ──────────────────────────────────────────────────────────

    public SignupPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public SignupPage fillAccountInformation(String password) {
        click(titleMrRadio);
        type(passwordInput, password);
        selectByValue(dobDaySelect, "10");
        selectByValue(dobMonthSelect, "5");
        selectByValue(dobYearSelect, "1995");
        return this;
    }

    public SignupPage fillAddressDetails(UserData user) {
        type(firstNameInput, user.firstName());
        type(lastNameInput, user.lastName());
        type(companyInput, user.company());
        type(addressInput, user.address());
        selectByVisibleText(countrySelect, user.country());
        type(stateInput, user.state());
        type(cityInput, user.city());
        type(zipcodeInput, user.zipcode());
        type(mobileInput, user.mobile());
        return this;
    }

    public SignupPage submit() {
        click(createAccountBtn);
        return this;
    }

    public HomePage continueAfterCreation() {
        click(continueButton);
        return new HomePage(driver);
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    public SignupPage assertSignupFormVisible() {
        assertThat(isDisplayed(signupFormHeader))
                .as("'Enter Account Information' form should be visible")
                .isTrue();
        return this;
    }

    public SignupPage assertAccountCreated() {
        assertThat(isDisplayed(accountCreatedHeader))
                .as("'Account Created!' confirmation should be visible")
                .isTrue();
        assertThat(getText(accountCreatedHeader))
                .as("Account created heading text")
                .contains("Account Created!");
        return this;
    }
}
