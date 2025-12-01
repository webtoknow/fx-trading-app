package com.fx.qa.automation.page.login.components;

import com.fx.qa.automation.driver.annotations.PageFragment;
import com.fx.qa.automation.page.Base;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@PageFragment
public class LoginFormFragment extends Base {
    // Login
    @FindBy(id="username")
    private static WebElement username;

    @FindBy(id="password")
    private static WebElement password;

    @FindBy(xpath="//a[@href='/register']")
    private static WebElement register;

    @FindBy(xpath="//button[@type='submit']")
    private static WebElement login;

    @FindBy(xpath="//h1[contains(text(), 'Login to your account')]")
    private static WebElement loginToYourAccount;

    @FindBy(xpath="//h4[text()='Register a new account']")
    private static WebElement registerNewAccount;
    //Methods for login page

    /**
     * This method logs in
     * @param name - name to be completed
     * @param pass - password to be completed
     */
    public void login( String name, String pass){
        log.info("Logging in!");
        try {
            log.info("Inserting the username!");
            username.click();
            username.sendKeys(name);
            log.info("Inserting the password!");
            password.click();
            password.sendKeys(pass);
            log.info(" Pressing Login Button!");
            login.click();
        } catch (NoSuchElementException e){
            log.error("I could not log in!");
        }
    }

    /**
     * This method checks if the Login page has loaded
     * @return returns true if Login to your account text is visible
     */
    public boolean loginPageReady(){
        log.info("Checking if Login Page is loaded!");
        boolean pageLoaded = FALSE;
        try{
            if(loginToYourAccount.isDisplayed()) {
                log.info("The login page has successfully loaded!");
                pageLoaded = TRUE;
            } else {
                log.error("The login page is not loaded!");
                pageLoaded = FALSE;
            }
        } catch(NoSuchElementException e){
            log.error("Login to your account text is not found!");
        }
        return pageLoaded ;
    }

    public void registerNewAccount(){
        log.info("Clicking on register link!");
        do {
            register.click();
            break;
        } while (!registerNewAccount.isDisplayed());
    }
}
