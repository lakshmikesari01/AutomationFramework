package com.automationexercise.pages;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CheckoutPage
 * ------------
 * Page Object for the Checkout review page and Payment page.
 */
public class CheckoutPage extends BasePage {

    // ── Checkout review ──────────────────────────────────────────────────────
    @FindBy(id = "address_delivery")
    private WebElement deliveryAddress;

    @FindBy(css = "textarea[name='message']")
    private WebElement orderCommentArea;

    @FindBy(css = "a.btn.btn-default.check_out")
    private WebElement placeOrderBtn;

    // ── Payment form ─────────────────────────────────────────────────────────
    @FindBy(css = "input[data-qa='name-on-card']")
    private WebElement cardNameInput;

    @FindBy(css = "input[data-qa='card-number']")
    private WebElement cardNumberInput;

    @FindBy(css = "input[data-qa='cvc']")
    private WebElement cardCvcInput;

    @FindBy(css = "input[data-qa='expiry-month']")
    private WebElement cardExpiryMonthInput;

    @FindBy(css = "input[data-qa='expiry-year']")
    private WebElement cardExpiryYearInput;

    @FindBy(css = "button[data-qa='pay-button']")
    private WebElement payAndConfirmBtn;

    // ── Order confirmation ───────────────────────────────────────────────────
    @FindBy(css = "h2[data-qa='order-placed']")
    private WebElement orderPlacedHeader;

    // ── Guest checkout prompt ─────────────────────────────────────────────────
    @FindBy(xpath = "//a[contains(text(),'Register / Login')]")
    private WebElement registerLoginLink;

    // ── Constructor ──────────────────────────────────────────────────────────

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public CheckoutPage addCommentAndPlaceOrder(String comment) {
        type(orderCommentArea, comment);
        click(placeOrderBtn);
        return this;
    }

    public CheckoutPage fillPaymentDetails(
            String name, String cardNumber, String cvc,
            String expiryMonth, String expiryYear) {
        type(cardNameInput, name);
        type(cardNumberInput, cardNumber);
        type(cardCvcInput, cvc);
        type(cardExpiryMonthInput, expiryMonth);
        type(cardExpiryYearInput, expiryYear);
        click(payAndConfirmBtn);
        return this;
    }

    public LoginPage loginFromCheckout() {
        click(registerLoginLink);
        return new LoginPage(driver);
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    public CheckoutPage assertDeliveryAddressVisible() {
        assertThat(isDisplayed(deliveryAddress))
                .as("Delivery address block should be visible on checkout")
                .isTrue();
        return this;
    }

    public CheckoutPage assertOrderPlacedSuccessfully() {
        assertThat(isDisplayed(orderPlacedHeader))
                .as("'Order Placed!' confirmation header should be visible")
                .isTrue();
        assertThat(getText(orderPlacedHeader))
                .as("Order placed header text")
                .contains("Order Placed!");
        return this;
    }
}
