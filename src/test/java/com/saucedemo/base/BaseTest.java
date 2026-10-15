package com.saucedemo.base;

import com.microsoft.playwright.*;
import com.saucedemo.factory.PlaywrightFactory;
import com.saucedemo.config.ConfigReader;
import org.testng.ITestResult;
import org.testng.annotations.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.qameta.allure.Allure;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import io.qameta.allure.AttachmentOptions;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.testng.annotations.Parameters;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.AfterTest;


public class BaseTest {
    private static final Logger logger =
            LoggerFactory.getLogger(BaseTest.class);
    protected BrowserContext context;
    protected Page page;

    @BeforeTest
    @Parameters("browser")
    public void startBrowser(String browserName) {

        logger.info("Starting browser: {}", browserName);

        PlaywrightFactory.initializeBrowser(browserName);

        logger.info("Browser started: {}", browserName);
    }


    @BeforeMethod
    public void createPage() {

        logger.info("Creating new browser context");

        context =
                PlaywrightFactory
                        .getBrowser()
                        .newContext(
                                new Browser.NewContextOptions()
                                        .setViewportSize(1440, 900)
                        );

        logger.info("Starting Playwright trace");

        context.tracing().start(
                new Tracing.StartOptions()
                        .setScreenshots(true)
                        .setSnapshots(true)
                        .setSources(true)
        );

        page = context.newPage();

        page.setDefaultTimeout(
                ConfigReader.getTimeout()
        );

        logger.info("Browser context and page created");
    }
    private void saveTrace(String testName) {

        try {
            Path traceDirectory =
                    Paths.get("test-output/traces");

            Files.createDirectories(traceDirectory);

            Path tracePath =
                    traceDirectory.resolve(
                            testName + "-trace.zip"
                    );

            context.tracing().stop(
                    new Tracing.StopOptions()
                            .setPath(tracePath)
            );

            try (InputStream traceStream =
                         Files.newInputStream(tracePath)) {

                Allure.attachment(
                        "Playwright Trace - " + testName,
                        "application/zip",
                        traceStream,
                        AttachmentOptions.withFileExtension(".zip")
                );
            }

            logger.info(
                    "Playwright trace saved and attached to Allure: {}",
                    tracePath.toAbsolutePath()
            );

        } catch (Exception e) {
            logger.error("Unable to save or attach Playwright trace", e);
        }
    }
    @AfterMethod
    public void closeContext(ITestResult result) {

        String testName =
                PlaywrightFactory.getBrowserName()
                        + "-"
                        + result.getMethod().getMethodName()
                        + "-"
                        + System.currentTimeMillis();

        if (result.getStatus() == ITestResult.FAILURE) {

            takeScreenshot(testName);

            saveTrace(testName);
        } else {

            // Stop trace without saving it for passing tests
            if (context != null) {
                context.tracing().stop();
            }
        }

        if (context != null) {
            context.close();
            context = null;
        }
    }

    @AfterTest
    public void stopBrowser() {

        logger.info("Closing browser");

        PlaywrightFactory.closeBrowser();

        logger.info("Browser closed");
    }
    private void takeScreenshot(String testName) {

        if (page == null) {
            logger.warn("Cannot capture screenshot: page is null");
            return;
        }

        try {
            Path screenshotDirectory =
                    Paths.get("test-output", "screenshots");

            Files.createDirectories(screenshotDirectory);

            Path screenshotPath =
                    screenshotDirectory.resolve(testName + ".png");

            byte[] screenshotBytes =
                    page.screenshot(
                            new Page.ScreenshotOptions()
                                    .setFullPage(true)
                    );

            Files.write(screenshotPath, screenshotBytes);

            // Attach screenshot to the current Allure test
            Allure.attachment(
                    "Failure Screenshot - " + testName,
                    "image/png",
                    new ByteArrayInputStream(screenshotBytes),
                    AttachmentOptions.withFileExtension(".png")
            );

            logger.info(
                    "Screenshot saved: {}",
                    screenshotPath.toAbsolutePath()
            );

        } catch (Exception e) {
            logger.error("Unable to capture or attach screenshot", e);
        }
    }
}
