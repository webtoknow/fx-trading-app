package com.fx.qa.automation.tests.createNewUser;

import com.fx.qa.automation.SpringBaseTestNGTest;
import com.fx.qa.automation.utils.AppUtils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TestCreateNewUser extends SpringBaseTestNGTest {
    public static final String USERNAME = "Test" + AppUtils.getRandomCharacterString(7);
    public static final String PASSWORD = "abc123";
    private static final String EMAIL = "abc123999@ab.com";


    @Test(groups = "userCreation")
    public void createNewUser(){
        loginPage.getLoginFormFragment().registerNewAccount();
        registerPage.getRegisterFormFragment().registerAccount(USERNAME,PASSWORD,EMAIL);
        Assert.assertTrue(loginPage.getLoginFormFragment().loginPageReady(), "The login page was not loaded after registering the account");
    }
}
