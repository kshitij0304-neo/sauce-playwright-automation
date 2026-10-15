package com.saucedemo.config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream =
                     ConfigReader.class
                             .getClassLoader()
                             .getResourceAsStream("config.properties")) {

            if (inputStream == null) {
                throw new RuntimeException(
                        "config.properties file not found"
                );
            }

            properties.load(inputStream);

        } catch (IOException e) {
            throw new RuntimeException(
                    "Unable to load config.properties",
                    e
            );
        }
    }

    private ConfigReader() {
        // Prevent object creation
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }

    public static String getBaseUrl() {
        return get("baseUrl");
    }

    public static String getBrowser() {
        return get("browser");
    }

    public static boolean isHeadless() {

        String override = System.getProperty("headless");

        if (override == null || override.isBlank()) {
            override = System.getenv("HEADLESS");
        }

        if (override != null && !override.isBlank()) {
            return Boolean.parseBoolean(override);
        }

        return Boolean.parseBoolean(get("headless"));
    }


    public static int getTimeout() {
        return Integer.parseInt(get("timeout"));
    }
}