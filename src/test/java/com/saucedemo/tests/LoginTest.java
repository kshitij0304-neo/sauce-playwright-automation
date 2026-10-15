package com.saucedemo.tests;

import com.saucedemo.base.BaseTest;
import com.saucedemo.pages.LoginPage;

import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import com.saucedemo.testdata.LoginTestData;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    private LoginPage loginPage;

    @BeforeMethod
    public void initializePage() {

        loginPage = new LoginPage(page);

        loginPage.open();
    }

    @Test(
            description = "Verify login page is displayed",
            groups = {"smoke", "login"}
    )
    public void verifyLoginPageDisplayed() {

        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Customer Login page should be displayed"
        );
    }

    @Test(
            description = "Verify email field is displayed",
            groups = {"smoke", "login"}
    )
    public void verifyEmailFieldDisplayed() {

        Assert.assertTrue(
                loginPage.isEmailFieldDisplayed(),
                "Email field should be displayed"
        );
    }

    @Test(
            description = "Verify password field is displayed",
            groups = {"smoke", "login"}
    )
    public void verifyPasswordFieldDisplayed() {

        Assert.assertTrue(
                loginPage.isPasswordFieldDisplayed(),
                "Password field should be displayed"
        );
    }

    @Test(
            description = "Verify password is masked",
            groups = {"security", "login"}
    )
    public void verifyPasswordIsMasked() {

        Assert.assertEquals(
                loginPage.getPasswordInputType(),
                "password",
                "Password should be masked"
        );
    }

    @Test(
            description = "Verify login page URL",
            groups = {"smoke"}
    )
    public void verifyLoginPageUrl() {

        Assert.assertTrue(
                loginPage.getCurrentUrl()
                        .contains("/account/login"),
                "URL should contain /account/login"
        );
    }

    @Test(
            dataProvider = "invalidLoginData",
            dataProviderClass = LoginTestData.class,
            description = "Verify login with invalid credentials",
            groups = {"negative", "login"}
    )
    public void verifyInvalidCredentials(
            String scenario,
            String email,
            String password
    ) {

        loginPage.login(email, password);

        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                scenario
                        + " - User should remain on the login page"
        );
    }

    @Test(
            description = "Verify empty credentials",
            groups = {"negative", "login"}
    )
    public void verifyEmptyCredentials() {

        loginPage.clickLogin();

        String pageText =
                page.locator("body")
                        .innerText()
                        .toLowerCase();

        Assert.assertTrue(
                pageText.contains("email")
                        || pageText.contains("password"),
                "Validation should be displayed"
        );
    }

    @Test(
            description = "Verify forgot password functionality",
            groups = {"password-reset", "login"}
    )
    public void verifyForgotPassword() {

        loginPage.clickForgotPassword();

        Assert.assertTrue(
                loginPage.isResetPasswordDisplayed(),
                "Reset Password section should be displayed"
        );
    }

    @Test(
            description = "Verify page after refresh",
            groups = {"navigation", "login"}
    )
    public void verifyPageAfterRefresh() {

        page.reload();

        Assert.assertTrue(
                loginPage.isLoginPageDisplayed(),
                "Login page should remain displayed"
        );
    }
}
