package com.automationexercise.tests;

import com.automationexercise.pages.CartPage;
import com.automationexercise.pages.ProductsPage;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * AddToCartTest
 * -------------
 * Journey 3: Add Product to Cart
 *
 * WHY THIS JOURNEY WAS CHOSEN:
 *   The cart is where browsing converts to intent-to-purchase. Regressions
 *   here are immediately customer-facing and revenue-blocking.
 *   Covers: add from detail, custom quantity, remove item, multiple products.
 *
 * SCENARIOS:
 *   1. Add a product from the detail page — cart modal appears     [smoke]
 *   2. Cart contains the correct product after adding              [smoke]
 *   3. Remove product from cart reduces the item count             [regression]
 *   4. Custom quantity is reflected in the cart                    [regression]
 *   5. Multiple distinct products can be in the cart simultaneously [regression]
 */
public class AddToCartTest extends BaseTest {

    // ── Test 1 ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "cart"},
          description = "Happy path: add a product from detail page — success modal appears")
    public void testAddProductToCartFromDetailPage() {
        new ProductsPage(getDriver())
                .open()
                .openProductDetail(0)
                .assertProductDetailVisible()
                .addToCartFromDetail()
                .assertCartModalVisible();
    }

    // ── Test 2 ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "cart"},
          description = "After adding a product, the cart should list that product by name")
    public void testCartContainsAddedProduct() {
        ProductsPage products = new ProductsPage(getDriver()).open();
        products.openProductDetail(0);
        products.assertProductDetailVisible();

        String productName = products.getProductNameOnDetail();
        log.info("Adding product to cart: {}", productName);

        CartPage cart = products
                .addToCartFromDetail()
                .goToCartFromModal();

        cart.assertCartHasItems()
            .assertProductInCart(productName);
    }

    // ── Test 3 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "cart"},
          description = "Removing a product from the cart should decrease the item count by 1")
    public void testRemoveProductFromCart() {
        ProductsPage products = new ProductsPage(getDriver()).open();
        CartPage cart = products
                .openProductDetail(0)
                .addToCartFromDetail()
                .goToCartFromModal();

        int before = cart.getItemCount();
        assertThat(before).as("Cart should have at least 1 item before removal").isGreaterThan(0);

        cart.removeFirstItem();

        int after = cart.getItemCount();
        assertThat(after)
                .as("Item count should decrease by 1 after removal")
                .isEqualTo(before - 1);
    }

    // ── Test 4 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "cart"},
          description = "Setting quantity to 3 on the detail page should show quantity 3 in the cart")
    public void testAddProductWithCustomQuantity() {
        CartPage cart = new ProductsPage(getDriver())
                .open()
                .openProductDetail(0)
                .setQuantity(3)
                .addToCartFromDetail()
                .goToCartFromModal();

        cart.assertCartHasItems()
            .assertFirstItemQuantity("3");
    }

    // ── Test 5 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "cart"},
          description = "Two distinct products can be added to the cart in a single session")
    public void testMultipleProductsCanBeAddedToCart() {
        ProductsPage products = new ProductsPage(getDriver()).open();

        // Add first product
        products.openProductDetail(0)
                .addToCartFromDetail()
                .continueShopping();

        // Add second product
        products.open()
                .openProductDetail(1)
                .addToCartFromDetail();

        CartPage cart = products.goToCartFromModal();
        int count = cart.getItemCount();
        assertThat(count)
                .as("Cart should contain at least 2 distinct products")
                .isGreaterThanOrEqualTo(2);
    }
}
