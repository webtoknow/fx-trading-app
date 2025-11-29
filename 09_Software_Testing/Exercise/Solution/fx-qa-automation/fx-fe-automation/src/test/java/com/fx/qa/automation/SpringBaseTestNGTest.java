package com.fx.qa.automation;

import com.fx.qa.automation.driver.annotations.LazyAutowired;
import com.fx.qa.automation.listeners.TestNGCustomListener;
import com.fx.qa.automation.page.dashboard.DashboardPage;
import com.fx.qa.automation.page.login.LoginPage;
import com.fx.qa.automation.page.register.RegisterPage;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;

@Slf4j
@EnableAutoConfiguration
@SpringBootTest
@ComponentScan(basePackages = {"com.fx.qa.automation"})
public class SpringBaseTestNGTest extends AbstractTestNGSpringContextTests {

    @LazyAutowired
    protected LoginPage loginPage;

    @LazyAutowired
    protected RegisterPage registerPage;

    @LazyAutowired
    protected DashboardPage dashboardPage;

    @Autowired
    private TestNGCustomListener listener;

    @Autowired
    protected ApplicationContext applicationContext;

    @BeforeClass
    public void setUpClass() {
        loginPage.initialize();
    }

    @AfterMethod
    public void after(ITestResult result) { this.applicationContext.getBean(WebDriver.class).quit();}
}
