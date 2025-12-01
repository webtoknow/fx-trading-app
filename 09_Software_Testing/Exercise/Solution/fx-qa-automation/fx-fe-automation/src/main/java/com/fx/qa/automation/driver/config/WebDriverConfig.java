package com.fx.qa.automation.driver.config;

import com.fx.qa.automation.driver.annotations.LazyConfiguration;
import com.fx.qa.automation.driver.annotations.ThreadScopeBean;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;

@LazyConfiguration
public class WebDriverConfig {

    @ThreadScopeBean
    @ConditionalOnProperty(name = "application.browser", havingValue = "chrome")
    public WebDriver chromeDriver() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--guest");
        options.addArguments("--disable-search-engine-choice-screen");
        return new ChromeDriver(options);
    }

    @ThreadScopeBean
    @ConditionalOnProperty(name = "application.browser", havingValue = "edge")
    public WebDriver edgeDriver() {
        WebDriverManager.edgedriver().setup();
        EdgeOptions options = new EdgeOptions();
        options.addArguments("--start-maximized");
        options.addArguments("--guest");
        return new EdgeDriver(options);
    }

    @ThreadScopeBean
    @ConditionalOnProperty(name = "application.browser", havingValue = "firefox")
    public WebDriver firefoxDriver() {
        WebDriverManager.firefoxdriver().setup();
        FirefoxOptions options = new FirefoxOptions();
        options.addArguments("--start-maximized");
        return new FirefoxDriver(options);
    }

    @ThreadScopeBean
    @ConditionalOnProperty(name = "application.browser", havingValue = "safari")
    public WebDriver safariDriver() {
        SafariOptions options = new SafariOptions();
        return new SafariDriver(options);
    }
}
