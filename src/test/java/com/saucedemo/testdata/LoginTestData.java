package com.saucedemo.testdata;

import org.testng.annotations.DataProvider;

public class LoginTestData {

    private LoginTestData() {
        // Utility class
    }

    @DataProvider(name = "invalidLoginData")
    public static Object[][] invalidLoginData() {

        return new Object[][]{

                {
                        "Invalid email and invalid password",
                        "invalid1@example.com",
                        "WrongPassword123"
                },

                {
                        "Non-existent email and invalid password",
                        "invalid2@example.com",
                        "WrongPassword456"
                },

                {
                        "Another invalid credential combination",
                        "invalid3@example.com",
                        "WrongPassword789"
                }
        };
    }
}
