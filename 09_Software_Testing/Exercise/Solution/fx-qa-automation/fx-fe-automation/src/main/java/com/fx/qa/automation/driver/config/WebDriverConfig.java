package com.fx.qa.automation.driver.config;

import com.fx.qa.automation.driver.annotations.LazyConfiguration;
import com.fx.qa.automation.driver.annotations.ThreadScopeBean;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
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
}
