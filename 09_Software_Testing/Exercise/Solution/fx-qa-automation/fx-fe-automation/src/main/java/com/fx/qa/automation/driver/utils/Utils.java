package com.fx.qa.automation.driver.utils;

import com.fx.qa.automation.page.Base;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class Utils extends Base {

    public void switchDefaultContext() {
        ctx.getBean(WebDriver.class).switchTo().defaultContent();
    }


    public void waitForElementToBeVisible(WebElement element) {
        this.wait.until((d) -> element.isDisplayed());
    }

    public void hoverThenClickElement(WebElement element) {
        getActions().moveToElement(element).click().perform();
    }
}
