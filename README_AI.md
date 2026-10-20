# AI-Assisted Development: Java Playwright Automation Framework

This document explains how AI was used as a development partner while building this Java Playwright automation project, how suggestions were validated, and which engineering decisions remained subject to human review.

**Repository:** [sauce-playwright-automation](https://github.com/kshitij0304-neo/sauce-playwright-automation)

## 1. Project objective

The objective was to build a maintainable UI automation framework using Java, Playwright, TestNG, Maven, Page Object Model, logging, Allure reporting, cross-browser execution, and GitHub Actions CI.

The current automated scenarios cover a demo web application's login and password-reset entry points. The reusable framework infrastructure can support other web applications, but their Page Objects, test data, assertions, and workflows must be designed for each application's actual UI and behavior.

## 2. How AI was used

AI was used as an engineering copilot across several stages of the work.

### Framework design

- Discussed how to organize a Java Maven automation project into configuration, factory, page, base-test, test-data, and test packages.
- Used separation of concerns and Page Object Model to keep locators/actions separate from test assertions.
- Built the project in small, verifiable phases instead of generating a large untested code dump.

### Implementation guidance

- Developed the structure for `ConfigReader`, `PlaywrightFactory`, `BaseTest`, `LoginPage`, `LoginTest`, and `LoginTestData`.
- Added TestNG DataProvider-based credential variations.
- Added SLF4J/Logback logging, failure screenshots, Playwright tracing, and Allure reporting.
- Configured Chromium, Firefox, and WebKit execution, then introduced parallel browser suites after updating browser lifecycle ownership.
- Prepared a GitHub Actions workflow to install browsers, run the TestNG suite, generate the Allure report, and upload artifacts.

### Troubleshooting and debugging

AI was used to interpret actual errors and propose focused fixes. Examples encountered during development included:

- Maven not being found in the Windows `PATH`.
- PowerShell argument parsing when invoking the Playwright CLI through Maven.
- A Java public-class filename/package-structure mismatch.
- A Playwright locator timeout because the initial assumed button role/name did not match the actual login submit control.
- An invalid-login assertion that assumed error wording not present in the observed page content.
- A checked `IOException` in a temporary tracing test.
- A version-specific mismatch between an Allure attachment example and the installed Allure API.
- Workflow/report generation and locating the downloaded Allure report artifact.

The approach was to inspect the error output, make a narrow correction, rerun the affected test or command, and only then continue to the next framework stage.

## 3. Human-in-the-loop verification

AI suggestions were not treated as proof that the code worked. The project was validated through actual tool output and manual inspection.

Examples of verification performed during development:

- Confirmed Java and Maven versions in the local terminal.
- Ran Maven/TestNG tests after structural or locator changes.
- Ran a targeted invalid-credentials test to inspect the page URL and visible page text rather than assuming an authentication error message.
- Opened a Playwright trace to inspect recorded navigation, form filling, screenshots and snapshots.
- Generated and opened an Allure HTML report locally.
- Pushed the project to GitHub and inspected GitHub Actions logs.
- Verified a cross-browser CI run reporting 33 test executions, 33 passes, zero failures, zero errors and zero skipped tests.
- Verified that GitHub Actions uploaded the generated report and test artifacts.

The 33-pass result is evidence for that specific run, not a promise that future runs will always pass.

## 4. Engineering principles followed

### Inspect before asserting

Locators and expected behavior should be based on the application's actual DOM and observed behavior, not guesses based only on visual appearance.

### Preserve meaningful failures

A test should not be changed just to make the build green. When an assertion failed, the next step was to gather evidence and adjust the assertion only when the observed application behavior justified it.

### Make changes incrementally

New layers were introduced in sequence: project foundation, Page Objects, tests, DataProvider, logging/diagnostics, reporting, cross-browser execution, parallel execution, and CI.

### Protect secrets

Credentials should not be invented or hardcoded as valid. Passwords, tokens, cookies, and other secrets should not be printed in logs or committed to source control.

### Keep the framework separate from application-specific behavior

The browser lifecycle, configuration, logging, and reporting are reusable infrastructure. Page Objects, test cases, selectors, and expected business behavior must be adapted when the application changes.

## 5. Example AI-assisted prompting approach

The useful pattern was to provide the AI with the current code or complete error output, request a small diagnosis, apply one correction, and validate it before expanding scope.

Examples of prompt patterns:

- “Analyze this Maven compilation error. Identify the exact class/package mismatch and suggest the smallest fix.”
- “Here is the Playwright timeout and locator call log. Explain what is known, what is only a hypothesis, and how to inspect the real DOM before changing the locator.”
- “Review this TestNG DataProvider and estimate the expected number of test invocations across the configured browser suite.”
- “Inspect this GitHub Actions log. Determine whether the Maven test step passed, whether the Allure report was generated, and which artifacts were uploaded.”
- “Modify only the required method and preserve the existing thread-safe browser lifecycle and failure diagnostics.”

These examples demonstrate a repeatable AI-assisted workflow rather than treating the model as an unquestioned source of truth.

## 6. What this project does—and does not—claim

### It demonstrates

- Practical use of AI assistance during automation framework design and implementation.
- AI-supported debugging based on compiler output, test logs, and browser evidence.
- Human verification of AI-suggested changes.
- A deterministic Java Playwright framework with TestNG, reporting, and CI integration.
- The ability to use AI as part of an engineering workflow while retaining responsibility for correctness.

### It does not currently claim

- That the framework itself is an autonomous AI testing agent.
- LLM-driven autonomous test generation at runtime.
- AI-based self-healing locators.
- Model-based exploration or autonomous root-cause analysis built into the framework.
- That AI independently executed all tests or deployed the project without human interaction.

Those are possible future experiments, not features of the current implementation.

## 7. Future AI-assisted improvements

Potential next steps include:

- Experiment with an AI agent that proposes test scenarios from explicit requirements and inspected page structure.
- Use AI to summarize failed CI runs from Surefire output, logs, screenshots, and trace metadata.
- Generate a draft test matrix for a new application, then require human review before implementing assertions.
- Add API testing and validate API/UI behavior together where the target application exposes appropriate endpoints.
- Explore accessibility checks and risk-based regression prioritization.

Any generated test or suggested fix should be reviewed and executed before being treated as reliable.

## 8. Summary

This project demonstrates **AI-assisted automation engineering**, not blind reliance on generated code. AI supported planning, implementation guidance, troubleshooting, and documentation; the framework was developed iteratively, and its behavior was checked against actual local runs, Playwright traces, Allure output, and GitHub Actions results.
