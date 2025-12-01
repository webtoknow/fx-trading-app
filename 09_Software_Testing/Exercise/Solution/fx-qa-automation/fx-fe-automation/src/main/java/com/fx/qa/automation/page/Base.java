package com.fx.qa.automation.page;

import com.fx.qa.automation.driver.annotations.LazyAutowired;
import com.fx.qa.automation.driver.utils.Utils;
import jakarta.annotation.PostConstruct;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component
public abstract class Base {

    @Autowired
    protected WebDriverWait wait;

    @LazyAutowired
    protected FluentWait<WebDriver> fluentWait;

    @Autowired
    protected ApplicationContext ctx;

    @LazyAutowired
    protected Utils utils;

    @Value("${application.url}")
    private String applicationUrl;

    protected String getApplicationUrl() {
        return applicationUrl;
    }

    protected WebDriver getDriver() {
        return ctx.getBean(WebDriver.class);
    }

    @PostConstruct
    private void init() {
        PageFactory.initElements(getDriver(), this);
    }

    protected Actions getActions() {
        return new Actions(getDriver());
    }
}
