package com.fx.qa.automation.runner;

import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.annotations.DataProvider;

@CucumberOptions(features = "src/test/resources/features",
            plugin = {
                    "pretty",
                    "html:target/cucumber-html-report.html",
                    "json:target/cucumber.json"
            },
            glue = {"com.fx.qa.automation.definitions", "com.fx.qa.automation.cucumber.configuration"})
public class CucumberTestRunner extends AbstractTestNGCucumberTests {
    @Override
    @DataProvider(parallel = false)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
