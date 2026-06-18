package com.automationexercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * CartPage
 * --------
 * Page Object for the Shopping Cart (/view_cart).
 */
public class CartPage extends BasePage {

    @FindBy(id = "cart_info_table")
    private WebElement cartTable;

    @FindBy(css = "tr.cart_blk")
    private List<WebElement> cartRows;

    @FindBy(css = ".cart_description h4 a")
    private List<WebElement> productNames;

    @FindBy(css = ".cart_quantity button")
    private List<WebElement> quantities;

    @FindBy(css = ".cart_quantity_delete")
    private List<WebElement> deleteButtons;

    @FindBy(css = "a.btn.btn-default.check_out")
    private WebElement proceedToCheckoutBtn;

    @FindBy(id = "empty_cart")
    private WebElement emptyCartMsg;

    // ── Constructor ──────────────────────────────────────────────────────────

    public CartPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public CartPage open() {
        navigateTo("/view_cart");
        return this;
    }

    public CartPage removeFirstItem() {
        assertThat(deleteButtons).as("Delete buttons").isNotEmpty();
        click(deleteButtons.get(0));
        // Brief pause for DOM update after delete
        try { Thread.sleep(800); } catch (InterruptedException ignored) {}
        return this;
    }

    public CheckoutPage proceedToCheckout() {
        click(proceedToCheckoutBtn);
        return new CheckoutPage(driver);
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getItemCount() {
        return driver.findElements(By.cssSelector("tr.cart_blk")).size();
    }

    public String getQuantityOfFirstItem() {
        return quantities.isEmpty() ? "0" : getText(quantities.get(0));
    }

    public List<String> getProductNamesInCart() {
        return productNames.stream()
                .map(e -> e.getText().trim())
                .collect(Collectors.toList());
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    public CartPage assertCartHasItems() {
        assertThat(isDisplayed(cartTable))
                .as("Cart table should be visible")
                .isTrue();
        assertThat(getItemCount())
                .as("Cart should contain at least one item")
                .isGreaterThan(0);
        return this;
    }

    public CartPage assertCartIsEmpty() {
        assertThat(isDisplayed(emptyCartMsg))
                .as("Empty cart message should be visible")
                .isTrue();
        return this;
    }

    public CartPage assertProductInCart(String productName) {
        List<String> names = getProductNamesInCart();
        assertThat(names)
                .as("Cart product names")
                .anyMatch(n -> n.toLowerCase().contains(productName.toLowerCase()));
        return this;
    }

    public CartPage assertItemCount(int expected) {
        assertThat(getItemCount())
                .as("Cart item count")
                .isEqualTo(expected);
        return this;
    }

    public CartPage assertFirstItemQuantity(String expected) {
        assertThat(getQuantityOfFirstItem())
                .as("First cart item quantity")
                .isEqualTo(expected);
        return this;
    }
}
