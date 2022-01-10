package pages;

import driver.WebDriverThreadManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.logger.Tacitus;

public class RegisterANewAccount {

	private static WebDriver driver;

	public static void init(){
		driver = WebDriverThreadManager.getDriver();
		PageFactory.initElements(driver, RegisterANewAccount.class);
	}

	//region Page elements listing

	@FindBy(id = "inputUsername")
	private static WebElement username;

	@FindBy(id = "inputEmail")
	private static WebElement email;

	@FindBy(id = "inputPassword")
	private static WebElement password;

	@FindBy(xpath="//input[@formcontrolname='confirmPassword']")
	private static WebElement confirmPassword;

	@FindBy(xpath="//button[@type='submit']")
	private static WebElement register;

	@FindBy(linkText = "/login")
	private static WebElement login;

	@FindBy  (xpath="//h4[text()='Register a new account']")
	private static WebElement regNewAccountText;

	@FindBy(xpath="//div[text()='Password must be at least 6 characters!']")
	private static WebElement invalidPassText;
	//endregion
//TODO exercise -> create methods for this page so that we are able to fill in the data and also check the presence of elements like error messages


	/**
	 * Method used to insert a Username value into the field from Registration page
	 * @param userName what to insert
	 */
	public static void insertUserName(String userName){
		Tacitus.getInstance().log("Trying to insert Username in the registration field");
		try{
			username.click();
			username.sendKeys(userName);
			Tacitus.getInstance().logSuccess("Success fully inserted username value"+userName);
		} catch (Exception ex){
			Tacitus.getInstance().logError("Error while trying to insert Username in the registration field", ex);
		}
	}


}