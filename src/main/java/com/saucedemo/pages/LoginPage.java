package com.saucedemo.pages;

import com.microsoft.playwright.Locator;
import com.microsoft.playwright.Page;
import com.saucedemo.config.ConfigReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LoginPage {
    private static final Logger logger =
            LoggerFactory.getLogger(LoginPage.class);
    private final Page page;

    private final Locator emailInput;
    private final Locator passwordInput;
    private final Locator loginButton;
    private final Locator forgotPasswordLink;

    public LoginPage(Page page) {

        this.page = page;

        emailInput =
                page.getByLabel("Email Address");

        passwordInput =
                page.getByLabel("Password");
        loginButton =
                page.locator(
                        "form[action='/account/login'] input[type='submit']"
                );

        forgotPasswordLink =
                page.getByText("Forgot your password?");
    }

    public void open() {

        String loginUrl =
                ConfigReader.getBaseUrl()
                        + "/account/login";

        logger.info("Opening login page: {}", loginUrl);

        page.navigate(loginUrl);

        logger.info("Login page opened successfully");
    }

    public boolean isLoginPageDisplayed() {

        return page.getByText("Customer Login")
                .isVisible();
    }
    public boolean isOnLoginPage() {

        return page.url()
                .contains("/account/login");
    }

    public boolean isEmailFieldDisplayed() {

        return emailInput.isVisible();
    }

    public boolean isPasswordFieldDisplayed() {

        return passwordInput.isVisible();
    }

    public void enterEmail(String email) {

        logger.info("Entering email address");

        emailInput.fill(email);
    }

    public void enterPassword(String password) {

        logger.info("Entering password");

        passwordInput.fill(password);
    }

    public void clickLogin() {

        logger.info("Clicking Login button");

        loginButton.click();
    }

    public void login(
            String email,
            String password
    ) {

        logger.info("Starting login operation");

        enterEmail(email);
        enterPassword(password);
        clickLogin();

        logger.info("Login operation completed");
    }

    public void clickForgotPassword() {

        logger.info("Clicking Forgot Password link");

        forgotPasswordLink.click();

        logger.info("Forgot Password page/section opened");
    }

    public boolean isResetPasswordDisplayed() {

        return page.getByText("Reset Password")
                .isVisible();
    }

    public String getPasswordInputType() {

        return passwordInput
                .getAttribute("type");
    }

    public String getCurrentUrl() {

        return page.url();
    }
}