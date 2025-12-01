package com.fx.qa.automation.page.register.components;

import com.fx.qa.automation.driver.annotations.PageFragment;
import com.fx.qa.automation.page.Base;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.concurrent.TimeUnit;

@Slf4j
@PageFragment
public class RegisterFormFragment extends Base {
    @FindBy(id = "inputUsername")
    private static WebElement username;

    @FindBy(id = "inputEmail")
    private static WebElement email;

    @FindBy(id = "inputPassword")
    private static WebElement password;

    @FindBy(xpath="//button[@type='submit']")
    private static WebElement register;

    @FindBy(linkText = "/login")
    private static WebElement login;

    @FindBy  (xpath="//h4[text()='Register a new account']")
    private static WebElement regNewAccountText;

    @FindBy(xpath="//div[text()='Password must be at least 6 characters!']")
    private static WebElement invalidPassText;

    @FindBy(xpath="//h4[text()='Register a new account']")
    private static WebElement registerNewAccount;

    @FindBy(id="inputUsername")
    private static WebElement createUsername;

    @FindBy(id="inputEmail")
    private static WebElement createEmail;

    @FindBy(id="inputPassword")
    private static WebElement createPassword;

    @FindBy(id="inputConfirmPassword")
    private static WebElement confirmPassword;

    @FindBy(xpath="//button[@type='submit']")
    private static WebElement newAccountRegister;

    /**
     * Method used to insert a Username value into the field from Registration page
     * @param userName what to insert
     */
    public static void insertUserName(String userName){
        log.info("Trying to insert Username in the registration field");
        try{
            username.click();
            username.sendKeys(userName);
            log.info("Success fully inserted username value: {}", userName);
        } catch (Exception ex){
            log.error("Error while trying to insert Username in the registration field", ex);
        }
    }

    /**
     * This method creates a new account
     * @param uName - userName to be completed
     * @param pass - password to be completed
     * @param eMail - email to be completed
     */
    public void registerAccount( String uName, String pass, String eMail) {
        log.info("Creating a new account");
        try {
            insertUserName(uName);
            createUsername.click();
//		createUsername.sendKeys(uName);

            createEmail.click();
            createEmail.sendKeys(eMail);

            createPassword.click();
            createPassword.sendKeys(pass);

            confirmPassword.click();
            confirmPassword.sendKeys(pass);

            newAccountRegister.click();
            TimeUnit.SECONDS.sleep(2);
        } catch(NoSuchElementException e){
            log.error(" Could not create a new transaction", e);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
