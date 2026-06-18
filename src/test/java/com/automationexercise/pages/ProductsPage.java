package com.automationexercise.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductsPage
 * ------------
 * Page Object for /products (listing) and /product_details/<id> (detail).
 */
public class ProductsPage extends BasePage {

    // ── Listing page ─────────────────────────────────────────────────────────
    @FindBy(id = "search_product")
    private WebElement searchInput;

    @FindBy(id = "submit_search")
    private WebElement searchButton;

    @FindBy(xpath = "//h2[contains(@class,'title') and contains(text(),'All Products')]")
    private WebElement allProductsHeader;

    @FindBy(xpath = "//h2[contains(@class,'title') and contains(text(),'Searched Products')]")
    private WebElement searchedProductsHeader;

    @FindBy(css = ".productinfo")
    private List<WebElement> productCards;

    @FindBy(css = ".choose a")
    private List<WebElement> viewProductLinks;

    // ── Detail page ──────────────────────────────────────────────────────────
    @FindBy(css = ".product-information h2")
    private WebElement productName;

    @FindBy(css = ".product-information span span")
    private WebElement productPrice;

    @FindBy(id = "quantity")
    private WebElement quantityInput;

    @FindBy(xpath = "//button[contains(text(),'Add to cart')]")
    private WebElement addToCartBtn;

    // ── Cart modal ───────────────────────────────────────────────────────────
    @FindBy(id = "cartModal")
    private WebElement cartModal;

    @FindBy(xpath = "//u[contains(text(),'View Cart')]")
    private WebElement viewCartLink;

    @FindBy(xpath = "//button[contains(text(),'Continue Shopping')]")
    private WebElement continueShoppingBtn;

    // ── Constructor ──────────────────────────────────────────────────────────

    public ProductsPage(WebDriver driver) {
        super(driver);
    }

    // ── Actions ─────────────────────────────────────────────────────────────

    public ProductsPage open() {
        navigateTo("/products");
        return this;
    }

    public ProductsPage searchFor(String term) {
        type(searchInput, term);
        click(searchButton);
        return this;
    }

    public ProductsPage openProductDetail(int index) {
        List<WebElement> links = driver.findElements(By.cssSelector(".choose a"));
        assertThat(links.size()).as("Product links count").isGreaterThan(index);
        click(links.get(index));
        return this;
    }

    public ProductsPage setQuantity(int quantity) {
        quantityInput.clear();
        quantityInput.sendKeys(String.valueOf(quantity));
        return this;
    }

    public ProductsPage addToCartFromDetail() {
        click(addToCartBtn);
        return this;
    }

    public ProductsPage continueShopping() {
        wait.waitForVisible(continueShoppingBtn);
        click(continueShoppingBtn);
        return this;
    }

    public CartPage goToCartFromModal() {
        wait.waitForVisible(viewCartLink);
        click(viewCartLink);
        return new CartPage(driver);
    }

    // ── Getters ──────────────────────────────────────────────────────────────

    public int getProductCount() {
        return driver.findElements(By.cssSelector(".productinfo")).size();
    }

    public String getProductNameOnDetail() {
        return getText(productName);
    }

    // ── Assertions ──────────────────────────────────────────────────────────

    public ProductsPage assertProductsPageVisible() {
        assertThat(isDisplayed(allProductsHeader))
                .as("'All Products' heading should be visible")
                .isTrue();
        return this;
    }

    public ProductsPage assertSearchResultsVisible() {
        assertThat(isDisplayed(searchedProductsHeader))
                .as("'Searched Products' heading should be visible")
                .isTrue();
        return this;
    }

    public ProductsPage assertSearchResultsNotEmpty() {
        assertThat(getProductCount())
                .as("Search should return at least one product")
                .isGreaterThan(0);
        return this;
    }

    public ProductsPage assertSearchResultsEmpty() {
        assertThat(getProductCount())
                .as("Search should return zero results for nonsense term")
                .isEqualTo(0);
        return this;
    }

    public ProductsPage assertProductDetailVisible() {
        assertThat(isDisplayed(productName))
                .as("Product name should be visible on detail page")
                .isTrue();
        assertThat(isDisplayed(productPrice))
                .as("Product price should be visible on detail page")
                .isTrue();
        return this;
    }

    public ProductsPage assertCartModalVisible() {
        assertThat(isDisplayed(cartModal))
                .as("Cart modal should appear after adding to cart")
                .isTrue();
        return this;
    }
}
