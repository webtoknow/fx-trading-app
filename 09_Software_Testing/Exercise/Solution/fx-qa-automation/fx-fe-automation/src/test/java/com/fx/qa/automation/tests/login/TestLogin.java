package com.fx.qa.automation.tests.login;

import com.fx.qa.automation.SpringBaseTestNGTest;
import com.fx.qa.automation.listeners.RetryAnalyzer;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

@Slf4j
public class TestLogin extends SpringBaseTestNGTest {
    public static final String USERNAME = "astronaut";
    public static final String PASSWORD = "astronauc";

    @Test(description = "This scenario the log in function is tested", retryAnalyzer = RetryAnalyzer.class)
    public void login() {
        loginPage.getLoginFormFragment().login(USERNAME, PASSWORD);
        Assert.assertTrue(dashboardPage.getDashBoardFormFragment().dashboardVisibleCheck(), "Test failed as The dashboard page did not load");
        log.info("The Login test case ended successfully!");
    }
}
