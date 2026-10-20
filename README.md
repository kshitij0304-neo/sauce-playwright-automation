# Java Playwright Automation Framework

[![Java Playwright CI](https://github.com/kshitij0304-neo/sauce-playwright-automation/actions/workflows/playwright-tests.yml/badge.svg)](https://github.com/kshitij0304-neo/sauce-playwright-automation/actions/workflows/playwright-tests.yml)

A Java-based web UI automation framework built with **Playwright, TestNG, Maven, Page Object Model (POM), Logback, Allure, and GitHub Actions**. The project demonstrates reusable browser setup, data-driven tests, cross-browser execution, parallel browser runs, and CI test reporting.

> **Project scope:** The current test implementation targets the login and password-reset entry points of the Sauce Demo Shopify demo site. The framework components can be reused, but Page Objects, test data, and assertions must be adapted for a different application's UI and business workflows.

## Contents

- [Highlights](#highlights)
- [Technology stack](#technology-stack)
- [Latest verified run](#latest-verified-run)
- [Test coverage](#test-coverage)
- [Framework architecture](#framework-architecture)
- [Project structure](#project-structure)
- [Prerequisites](#prerequisites)
- [Setup on Windows](#setup-on-windows)
- [Run tests](#run-tests)
- [Allure report in GitHub Actions](#allure-report-in-github-actions)
- [Configuration](#configuration)
- [Troubleshooting](#troubleshooting)
- [AI-assisted development](#ai-assisted-development)
- [Limitations and next improvements](#limitations-and-next-improvements)

## Highlights

- **Page Object Model:** Locators and page actions are encapsulated in a dedicated `LoginPage` class.
- **Centralized configuration:** `ConfigReader` loads the application URL, default browser, headless mode, and timeout settings.
- **Browser factory:** `PlaywrightFactory` supports Chromium, Firefox, and WebKit, with thread-local browser ownership for parallel browser suites.
- **Test isolation:** Each test creates its own `BrowserContext` and `Page`.
- **Data-driven testing:** A TestNG `@DataProvider` supplies multiple invalid-credential scenarios.
- **Logging:** SLF4J with Logback provides console and file logs.
- **Failure diagnostics:** Screenshots and Playwright traces are configured to be saved and attached to Allure when tests fail.
- **Reporting:** Allure results are generated during test execution and an HTML report is generated in GitHub Actions.
- **CI automation:** GitHub Actions runs the cross-browser suite on pushes and pull requests targeting `main`, and supports manual runs.

## Technology stack

| Technology | Purpose |
|---|---|
| Java 25 | Programming language and runtime used by the current project and CI workflow |
| Playwright for Java | Browser automation API |
| TestNG 7.11.0 | Test execution, lifecycle annotations, parameters, groups, and DataProviders |
| Maven | Dependency management and build/test execution |
| Maven Surefire | Runs the TestNG suite |
| SLF4J + Logback | Structured test and framework logging |
| Allure | Test result visualization and attachments |
| Git + GitHub | Source control and repository hosting |
| GitHub Actions | CI pipeline for cross-browser tests and report artifacts |

Dependency versions are maintained in `pom.xml`. Review that file for the exact versions and plugin configuration used by this repository.

## Latest verified run

The most recently verified GitHub Actions run during development reported:

| Result | Count |
|---|---:|
| Test executions | 33 |
| Passed | 33 |
| Failed | 0 |
| Errors | 0 |
| Skipped | 0 |

The 33 executions represent 11 test invocations per browser across Chromium, Firefox, and WebKit. This is a recorded run result, not a guarantee that every future execution will pass.

## Test coverage

The current `LoginTest` suite covers:

- Login page is displayed.
- Email field is displayed.
- Password field is displayed.
- Password field uses password masking.
- Login URL contains `/account/login`.
- Invalid credential combinations, supplied by a TestNG DataProvider.
- Empty-credential submission behavior.
- Forgot-password navigation to the reset-password UI.
- Login page behavior after a refresh.

The invalid-credential DataProvider currently supplies three combinations. With the other test methods, this produces 11 test invocations per browser.

**Testing note:** These are UI automation scenarios for a demo application. Positive authentication with valid credentials is not included because a verified valid test account has not been configured. Assertions should be revisited if the application changes its UI or behavior.

## Framework architecture

```text
TestNG suite (testng.xml)
        |
        +-- Chromium tests
        +-- Firefox tests
        +-- WebKit tests
                 |
                 v
              BaseTest
       browser/context/page lifecycle
       trace + screenshot on failure
                 |
                 v
              LoginTest
                 |
            +----+----+
            |         |
            v         v
        LoginPage  LoginTestData
            |
            v
        Playwright API
            |
            v
          Browser

Supporting components:
  ConfigReader       -> configuration values
  PlaywrightFactory  -> thread-local browser lifecycle
  Logback            -> console/file logging
  Allure             -> test results and failure attachments
  GitHub Actions     -> CI execution and downloadable artifacts
```

## Project structure

```text
sauce-playwright-automation/
├── .github/
│   └── workflows/
│       └── playwright-tests.yml
├── .gitignore
├── pom.xml
├── testng.xml
├── qodana.yaml
└── src/
    ├── main/
    │   ├── java/com/saucedemo/
    │   │   ├── config/ConfigReader.java
    │   │   ├── factory/PlaywrightFactory.java
    │   │   └── pages/LoginPage.java
    │   └── resources/logback-test.xml
    └── test/
        ├── java/com/saucedemo/
        │   ├── base/BaseTest.java
        │   ├── testdata/LoginTestData.java
        │   └── tests/LoginTest.java
        └── resources/
            ├── allure.properties
            └── config.properties
```

Generated build output, logs, screenshots, traces, and reports are intended to remain outside version control through `.gitignore`.

## Prerequisites

Install the following on a development machine:

1. JDK 25 (or another JDK supported by the project after updating and verifying the Maven configuration).
2. Apache Maven, unless using a Maven Wrapper that you add to the repository.
3. Git.
4. IntelliJ IDEA or another Java IDE.
5. Node.js/npm only if you want to use the Allure 3 CLI locally to open the generated report.

## Setup on Windows

### 1. Clone the repository

```powershell
git clone https://github.com/kshitij0304-neo/sauce-playwright-automation.git
cd sauce-playwright-automation
```

### 2. Verify Java and Maven

```powershell
java -version
javac -version
mvn -version
```

### 3. Install Playwright browsers

From the project root, run the Playwright CLI through Maven:

```powershell
mvn exec:java -e -D "exec.mainClass=com.microsoft.playwright.CLI" -D "exec.args=install"
```

This installs the browsers supported by the installed Playwright package. On GitHub Actions, the workflow installs the browsers and required Linux dependencies.

### 4. Open in IntelliJ

Open the project folder or `pom.xml` in IntelliJ and let Maven import the dependencies.

## Run tests

Run the complete TestNG suite, including all configured browsers:

```powershell
mvn clean test
```

Run the login test class:

```powershell
mvn -Dtest=LoginTest test
```

Run the invalid-credential DataProvider test method:

```powershell
mvn -Dtest=LoginTest#verifyInvalidCredentials test
```

The `testng.xml` suite configures three browser blocks with `parallel="tests"` and `thread-count="3"`. This runs the browser suites concurrently. The exact test total changes when scenarios or DataProvider rows change.

## Allure report in GitHub Actions

The GitHub Actions workflow:

1. Sets up Java and Node.js.
2. Installs Playwright browsers and Linux dependencies.
3. Runs `mvn clean test`.
4. Generates an Allure HTML report when Allure result files exist.
5. Uploads Surefire results, Allure results/report, and any available logs, screenshots, and traces as a workflow artifact.

To view the report from a CI run:

1. Open the repository's [Actions page](https://github.com/kshitij0304-neo/sauce-playwright-automation/actions).
2. Select a completed workflow run.
3. Download the artifact named similar to `playwright-ci-artifacts-<run-number>`.
4. Extract the ZIP file.
5. From a terminal in the extracted artifact's parent directory, use Node.js/npm to serve the report:

```powershell
npx.cmd --yes allure@3.20.1 open .\allure-report
```

The `.cmd` form avoids PowerShell's `npx.ps1` execution-policy restriction on Windows. If the artifact layout differs, adjust the path to the extracted `allure-report` folder. Keep the terminal open while viewing the report.

The repository currently stores the HTML report as a downloadable artifact; permanent GitHub Pages hosting is not configured.

## Configuration

The default `src/test/resources/config.properties` contains non-secret execution settings:

```properties
baseUrl=https://sauce-demo.myshopify.com
browser=chromium
headless=false
timeout=30000
```

- `baseUrl` identifies the environment under test.
- `browser` is the default browser setting.
- `headless` controls whether the browser UI is shown locally.
- `timeout` is the default Playwright timeout in milliseconds.

The `HEADLESS` environment variable or `-Dheadless=true/false` system property can override the configured headless value. The CI workflow sets `HEADLESS=true` for headless execution.

Do not put real passwords, tokens, cookies, or other secrets in configuration files committed to Git. Use environment variables or an appropriate secret store if authenticated test scenarios are added.

## Troubleshooting

### `mvn` is not recognized

Install Apache Maven, add its `bin` directory to your user/system `PATH`, reopen the terminal, and check `mvn -version`.

### Playwright browser executable is missing

Run the Playwright browser installation command from the project root. Ensure the installed browser binaries match the Playwright Java dependency version.

### A locator times out

Inspect the actual DOM and accessible role/name, and check whether navigation completed or the application changed. Do not weaken an assertion or substitute a guessed selector just to make the test pass.

### Allure report opens to a spinner or does not load assets

Extract the artifact ZIP completely, then serve the report through the Allure CLI instead of opening the HTML file directly from a ZIP archive.

### CI tests fail but local tests pass

Inspect the failed GitHub Actions step, Surefire results, logs, screenshot and trace artifacts. Check browser version/dependencies, headless behavior, timing, and environment differences before changing test logic.

## AI-assisted development

This framework was developed through an AI-assisted, iterative engineering workflow. AI was used as a coding and troubleshooting partner for framework design, Java/Playwright implementation guidance, Maven and PowerShell errors, DataProvider setup, tracing, Allure reporting, and GitHub Actions configuration. Each major stage was checked against actual compiler output, local test runs, trace viewing, or GitHub Actions results.





## Repository

[View source code and GitHub Actions runs](https://github.com/kshitij0304-neo/sauce-playwright-automation)
