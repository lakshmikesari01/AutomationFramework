# AutomationExercise — Selenium Java Framework

A production-ready **Selenium 4 + TestNG + Java 17** automation framework for  
[https://automationexercise.com](https://automationexercise.com), built from scratch using the Page Object Model.

---

## Table of Contents

1. [Six Most Important User Journeys](#1-six-most-important-user-journeys)
2. [Four Journeys Selected for Scripting — and Why](#2-four-journeys-selected-for-scripting--and-why)
3. [Framework & Language Choice — and Why](#3-framework--language-choice--and-why)
4. [Project Structure](#4-project-structure)
5. [Prerequisites](#5-prerequisites)

---

## 1. Six Most Important User Journeys

| # | Journey | Why It Matters |
|---|---------|----------------|
| **1** | **User Registration** | Onboards new customers; blocks all authenticated features if broken |
| **2** | **Product Search & Browse** | Core discovery path; broken search = no conversions |
| **3** | **Add Product to Cart** | Bridge between browsing and buying; direct revenue impact |
| **4** | **Contact Us Form** | Primary support channel; trust risk if unavailable |
| **5** | **Login / Logout** | Gating mechanism for checkout and account management |
| **6** | **Checkout & Payment** | Final revenue-generating step; highest value per test |

---

## 2. Four Journeys Selected for Scripting — and Why

### Journey 1 — User Registration
**File:** `src/test/java/com/automationexercise/tests/UserRegistrationTest.java`

Registration is the highest-priority regression candidate because it gates all personalised  
features. This suite exercises form validation, dynamic unique data (via JavaFaker),  
duplicate-email error states, and post-registration login — covering both happy and  
negative paths with a single setup.

### Journey 2 — Product Search & Browse
**File:** `src/test/java/com/automationexercise/tests/ProductSearchAndBrowseTest.java`

Every conversion starts with discovery. This journey validates product listings, keyword  
search (valid term / no-results), product detail pages, and category-based navigation.  
It is entirely unauthenticated with zero pre-conditions — the safest regression suite to  
run in any environment.

### Journey 3 — Add Product to Cart
**File:** `src/test/java/com/automationexercise/tests/AddToCartTest.java`

The cart is where intent-to-purchase forms. Regressions here are immediately  
customer-facing and revenue-blocking. The suite covers: add from detail page, cart  
confirmation modal, product name verification in cart, quantity selection, item removal,  
and multi-product sessions.

### Journey 4 — Contact Us Form
**File:** `src/test/java/com/automationexercise/tests/ContactUsTest.java`

Selected over Login/Checkout for the initial suite because it: requires no authentication;  
executes in ~3 seconds; demonstrates browser **Alert/Dialog** handling in Selenium; and  
validates a support channel that is often overlooked in regression plans.

> **Login** is partially covered in `UserRegistrationTest` (register → logout → login).  
> **Checkout** is scoped as Phase 2 once the base suite is stable.

---

## 3. Framework & Language Choice — and Why

### Language: Java 17
- Strongly typed — compile-time safety catches selector or method signature errors before runtime
- First-class Selenium support via the official Java client
- `record` types (Java 16+) used for clean, immutable test data models
- Familiar to the majority of enterprise QA engineering teams

### Test Runner: TestNG
- Fine-grained group/suite control (`groups = {"smoke", "cart"}`)
- Data-driven support via `@DataProvider`
- Parallel execution natively via `parallel="tests"` in `testng.xml`
- Rich `ITestResult` hooks used for screenshot-on-failure and report logging

### Browser Automation: Selenium 4
- Industry standard with the largest community and documentation base
- **WebDriverManager** eliminates manual browser driver management
- Selenium 4 adds relative locators, improved `Actions` API, and Chrome DevTools Protocol access
- Native Page Object Model support via `PageFactory` / `@FindBy`

### Reporting: ExtentReports 5
- Rich, self-contained HTML report with pass/fail/skip stats
- Automatic screenshot embedding on failure
- System-info panel showing browser, URL, and framework version

### Test Data: JavaFaker
- Generates realistic unique emails, names, and addresses per test run
- Prevents "Email Already Registered" collisions between test executions

---

## 4. Project Structure

```
selenium-java-framework/
│
├── pom.xml                                     Maven project descriptor
│
└── src/test/
    ├── java/com/automationexercise/
    │   ├── config/
    │   │   └── ConfigReader.java               Reads config.properties; System prop override
    │   ├── pages/                              Page Object Model classes
    │   │   ├── BasePage.java                   Shared helpers (click, type, wait, assert)
    │   │   ├── HomePage.java
    │   │   ├── LoginPage.java
    │   │   ├── SignupPage.java
    │   │   ├── ProductsPage.java
    │   │   ├── CartPage.java
    │   │   ├── CheckoutPage.java
    │   │   └── ContactPage.java
    │   ├── tests/
    │   │   ├── BaseTest.java                   Driver lifecycle + reporting hooks
    │   │   ├── UserRegistrationTest.java        Journey 1
    │   │   ├── ProductSearchAndBrowseTest.java  Journey 2
    │   │   ├── AddToCartTest.java               Journey 3
    │   │   └── ContactUsTest.java               Journey 4
    │   ├── testdata/
    │   │   ├── TestDataGenerator.java           JavaFaker unique user factory
    │   │   └── Constants.java                  Static URLs, search terms, card data
    │   └── utils/
    │       ├── DriverManager.java              Thread-local WebDriver factory
    │       ├── WaitHelper.java                 Explicit wait utilities
    │       ├── ScreenshotHelper.java           Failure screenshot capture
    │       └── ExtentReportManager.java        Report singleton + thread-local test nodes
    │
    └── resources/
        ├── config.properties                   Environment configuration
        ├── testng.xml                          Full regression suite (parallel)
        ├── smoke-suite.xml                     Smoke-only suite
        └── log4j2.xml                          Logging configuration
```

---

## 5. Prerequisites

| Tool | Minimum Version | Download |
|---|---|---|
| Java JDK | 17 | https://adoptium.net |
| Apache Maven | 3.8+ | https://maven.apache.org |
| Google Chrome | Latest | https://www.google.com/chrome |

> **ChromeDriver is managed automatically** by WebDriverManager — no manual download needed.


*Framework built with Selenium 4 · TestNG · Java 17 · ExtentReports · JavaFaker · WebDriverManager*
