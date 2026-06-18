package com.automationexercise.tests;

import com.automationexercise.pages.ContactPage;
import com.automationexercise.pages.HomePage;
import com.automationexercise.testdata.Constants;
import org.testng.annotations.Test;

/**
 * ContactUsTest
 * -------------
 * Journey 4: Contact Us Form Submission
 *
 * WHY THIS JOURNEY WAS CHOSEN:
 *   The Contact Us form is a key customer-support touchpoint. It is:
 *   - Stateless (no login required) → zero pre-conditions, ideal smoke gate
 *   - Fast to execute (~3 s per test)
 *   - Demonstrates browser Alert/Dialog handling in Selenium
 *   - Validates an often-overlooked but business-critical support channel
 *
 * SCENARIOS:
 *   1. Successful submission shows success alert                [smoke]
 *   2. Home button after submission navigates to landing page   [regression]
 *   3. Contact Us is reachable from nav link                    [regression]
 *   4. Minimal valid input still triggers success               [regression]
 */
public class ContactUsTest extends BaseTest {

    // ── Test 1 ───────────────────────────────────────────────────────────────

    @Test(groups = {"smoke", "contact"},
          description = "Happy path: filling all fields and submitting shows the success alert")
    public void testSuccessfulContactFormSubmission() {
        new ContactPage(getDriver())
                .open()
                .assertContactPageVisible()
                .fillContactForm(
                        Constants.CONTACT_NAME,
                        Constants.CONTACT_EMAIL,
                        Constants.CONTACT_SUBJECT,
                        Constants.CONTACT_MESSAGE)
                .submitForm()
                .assertSuccessMessageVisible();
    }

    // ── Test 2 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "contact"},
          description = "Clicking 'Home' after a successful submission navigates to the home page")
    public void testHomeButtonNavigatesAfterSubmission() {
        ContactPage contact = new ContactPage(getDriver())
                .open()
                .fillContactForm(
                        "Navigate Test User",
                        "nav.test@example.com",
                        "Navigation check",
                        "Testing the Home button navigation post-form-submission.")
                .submitForm();

        contact.assertSuccessMessageVisible();

        HomePage home = contact.clickHome();
        home.assertHomePageVisible();
    }

    // ── Test 3 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "contact"},
          description = "Contact Us page is accessible via the top-navigation link")
    public void testContactPageAccessibleFromNav() {
        new HomePage(getDriver()).open();

        getDriver().findElement(
                org.openqa.selenium.By.cssSelector("a[href='/contact_us']")).click();

        new ContactPage(getDriver()).assertContactPageVisible();
    }

    // ── Test 4 ───────────────────────────────────────────────────────────────

    @Test(groups = {"regression", "contact"},
          description = "Edge case: minimum-length valid values in every field still submit successfully")
    public void testContactFormWithMinimalValidInput() {
        new ContactPage(getDriver())
                .open()
                .fillContactForm("A", "a@b.com", "X", "Y")
                .submitForm()
                .assertSuccessMessageVisible();
    }
}
