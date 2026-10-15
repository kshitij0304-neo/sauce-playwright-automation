package com.saucedemo.factory;

import com.microsoft.playwright.*;
import com.saucedemo.config.ConfigReader;

public class PlaywrightFactory {

    private static final ThreadLocal<Playwright> PLAYWRIGHT =
            new ThreadLocal<>();

    private static final ThreadLocal<Browser> BROWSER =
            new ThreadLocal<>();

    private static final ThreadLocal<String> BROWSER_NAME =
            new ThreadLocal<>();

    private PlaywrightFactory() {
        // Prevent object creation
    }

    public static void initializeBrowser(String browserName) {

        if (PLAYWRIGHT.get() != null) {
            closeBrowser();
        }

        Playwright playwright = Playwright.create();

        try {
            BrowserType.LaunchOptions options =
                    new BrowserType.LaunchOptions()
                            .setHeadless(ConfigReader.isHeadless());

            Browser browser;

            switch (browserName.toLowerCase()) {

                case "chromium":
                    browser = playwright.chromium().launch(options);
                    break;

                case "firefox":
                    browser = playwright.firefox().launch(options);
                    break;

                case "webkit":
                    browser = playwright.webkit().launch(options);
                    break;

                default:
                    throw new IllegalArgumentException(
                            "Unsupported browser: " + browserName
                    );
            }

            PLAYWRIGHT.set(playwright);
            BROWSER.set(browser);
            BROWSER_NAME.set(browserName.toLowerCase());

        } catch (RuntimeException e) {
            playwright.close();
            throw e;
        }
    }

    public static Browser getBrowser() {

        Browser browser = BROWSER.get();

        if (browser == null) {
            throw new IllegalStateException(
                    "Browser has not been initialized on this thread"
            );
        }

        return browser;
    }

    public static String getBrowserName() {

        String browserName = BROWSER_NAME.get();

        return browserName == null ? "unknown" : browserName;
    }

    public static void closeBrowser() {

        Browser browser = BROWSER.get();
        Playwright playwright = PLAYWRIGHT.get();

        try {
            if (browser != null) {
                browser.close();
            }
        } finally {
            BROWSER.remove();

            try {
                if (playwright != null) {
                    playwright.close();
                }
            } finally {
                PLAYWRIGHT.remove();
                BROWSER_NAME.remove();
            }
        }
    }
}