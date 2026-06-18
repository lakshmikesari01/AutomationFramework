package com.automationexercise.testdata;

/**
 * Constants
 * ---------
 * Centralised static values used across the test suite.
 */
public final class Constants {

    private Constants() {}

    // ── URLs ─────────────────────────────────────────────────────────────────
    public static final String BASE_URL            = "https://automationexercise.com";
    public static final String PRODUCTS_URL        = BASE_URL + "/products";
    public static final String LOGIN_URL           = BASE_URL + "/login";
    public static final String CART_URL            = BASE_URL + "/view_cart";
    public static final String CONTACT_URL         = BASE_URL + "/contact_us";
    public static final String CATEGORY_WOMEN_DRESS = BASE_URL + "/category_products/1";

    // ── Search terms ──────────────────────────────────────────────────────────
    public static final String SEARCH_VALID        = "top";
    public static final String SEARCH_NO_RESULTS   = "xyzzy_no_match_999";
    public static final String SEARCH_PARTIAL      = "dress";

    // ── Test card data (sandbox values) ──────────────────────────────────────
    public static final String CARD_NAME           = "Test Cardholder";
    public static final String CARD_NUMBER         = "4111111111111111";
    public static final String CARD_CVC            = "123";
    public static final String CARD_EXPIRY_MONTH   = "12";
    public static final String CARD_EXPIRY_YEAR    = "2027";

    // ── Contact form data ─────────────────────────────────────────────────────
    public static final String CONTACT_NAME        = "QA Automation Bot";
    public static final String CONTACT_EMAIL       = "qa.bot@automationtest.com";
    public static final String CONTACT_SUBJECT     = "Automated Regression Test Submission";
    public static final String CONTACT_MESSAGE     =
            "This is an automated message from the Selenium Java regression suite. Please ignore.";
}
