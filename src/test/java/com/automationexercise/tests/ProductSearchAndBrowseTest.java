package com.automationexercise.tests;

import com.automationexercise.pages.HomePage;
import com.automationexercise.pages.ProductsPage;
import com.automationexercise.testdata.Constants;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * ProductSearchAndBrowseTest
 * --------------------------
 * Journey 2: Product Search & Browse
 *
 * WHY THIS JOURNEY WAS CHOSEN:
 *   The ability to find and view products is the core commercial function
 *   of any e-commerce site. A broken search or product listing directly
 *   translates to lost revenue. Entirely unauthenticated — zero pre-conditions.
 *
 * SCENARIOS:
 *   1. All Products page loads with visible items                  [smoke]
 *   2. Search returns relevant results for a valid term            [regression]
 *   3. Search with nonsense term returns zero results              [regression]
 *   4. Product detail page displays complete information           [smoke]
 *   5. Category filter navigates to the correct category page     [regression]
 */
public class ProductSearchAndBrowseTest extends BaseTest {

    // ── Test 1 ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "product"},
          description = "All Products page loads and shows at least one product card")
    public void testAllProductsPageLoads() {
        ProductsPage products = new ProductsPage(getDriver()).open();
        products.assertProductsPageVisible();

        int count = products.getProductCount();
        assertThat(count)
                .as("Product count on /products listing")
                .isGreaterThan(0);

        log.info("Product listing loaded: {} products found", count);
    }

    // ── Test 2 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "product"},
          description = "Searching for 'top' returns a non-empty Searched Products section")
    public void testSearchReturnsRelevantResults() {
        new ProductsPage(getDriver())
                .open()
                .searchFor(Constants.SEARCH_VALID)
                .assertSearchResultsVisible()
                .assertSearchResultsNotEmpty();
    }

    // ── Test 3 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "product"},
          description = "Searching for a nonsense term returns zero products without crashing the page")
    public void testSearchWithNoResults() {
        new ProductsPage(getDriver())
                .open()
                .searchFor(Constants.SEARCH_NO_RESULTS)
                .assertSearchResultsVisible()   // section renders
                .assertSearchResultsEmpty();    // but zero cards
    }

    // ── Test 4 ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "product"},
          description = "Product detail page shows product name and price")
    public void testProductDetailPageDisplaysFullInfo() {
        ProductsPage products = new ProductsPage(getDriver()).open();
        products.openProductDetail(0);
        products.assertProductDetailVisible();

        String name = products.getProductNameOnDetail();
        assertThat(name)
                .as("Product name on detail page should not be blank")
                .isNotBlank();

        log.info("Product detail page verified for: {}", name);
    }

    // ── Test 5 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "product"},
          description = "Clicking Women > Dress category navigates to correct category page")
    public void testCategoryFilterWomenDress() {
        new HomePage(getDriver()).open();

        // Click the Women > Dress category sidebar link
        getDriver().findElement(By.cssSelector("a[href='/category_products/1']")).click();

        // Wait for URL and heading
        boolean urlCorrect = new com.automationexercise.utils.WaitHelper(getDriver())
                .waitForUrlContains("category_products/1");
        assertThat(urlCorrect).as("URL should contain 'category_products/1'").isTrue();

        WebElement heading = getDriver().findElement(
                By.xpath("//h2[contains(@class,'title') and contains(.,'Women - Dress Products')]"));
        assertThat(heading.isDisplayed())
                .as("'Women - Dress Products' heading should be visible")
                .isTrue();
    }
}
