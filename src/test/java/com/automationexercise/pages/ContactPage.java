package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ContactPage
 * -----------
 * Page Object for /contact_us
 */
public class ContactPage extends BasePage {

    @FindBy(xpath = "//h2[contains(@class,'title') and contains(text(),'Contact us')]")
    private WebElement contactPageHeader;

    @FindBy(css = "input[data-qa='name']")
    private WebElement nameInput;

    @FindBy(css = "input[data-qa='email']")
    private WebElement emailInput;

    @FindBy(css = "input[data-qa='subject']")
    private WebElement subjectInput;

    @FindBy(css = "textarea[data-qa='message']")
    private WebElement messageTextarea;

    @FindBy(css = "input[data-qa='submit-button']")
    private WebElement submitBtn;

    @FindBy(css = "div.status.alert.alert-success")
    private WebElement successAlert;

    @FindBy(css = "a.btn.btn-success")
    private WebElement homeBtn;

    // ── Constructor ──────────────────────────────────────────────────────────

    public ContactPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public ContactPage open() {
        navigateTo("/contact_us");
        return this;
    }

    public ContactPage fillContactForm(String name, String email,
                                       String subject, String message) {
        type(nameInput, name);
        type(emailInput, email);
        type(subjectInput, subject);
        type(messageTextarea, message);
        return this;
    }

    public ContactPage submitForm() {
        // The site raises a native browser confirm dialog on submit — accept it
        click(submitBtn);
        acceptAlert();
        return this;
    }

    public HomePage clickHome() {
        click(homeBtn);
        return new HomePage(driver);
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    public ContactPage assertContactPageVisible() {
        assertThat(isDisplayed(contactPageHeader))
                .as("Contact Us page header should be visible")
                .isTrue();
        return this;
    }

    public ContactPage assertSuccessMessageVisible() {
        assertThat(isDisplayed(successAlert))
                .as("Success alert should be visible after form submission")
                .isTrue();
        assertThat(getText(successAlert))
                .as("Success alert text")
                .contains("Success! Your details have been submitted successfully.");
        return this;
    }
}
