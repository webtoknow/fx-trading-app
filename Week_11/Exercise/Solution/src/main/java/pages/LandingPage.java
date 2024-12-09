package pages;

import driver.DriverUtils;
import driver.WebDriverThreadManager;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.logger.Tacitus;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class LandingPage {


	private static WebDriver driver;

	public static void init(){
		driver = WebDriverThreadManager.getDriver();
		PageFactory.initElements(driver, LandingPage.class);
	}

	//Page elements listing

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
	//h4[contains(text(), 'Login to your account')] --//h4[text()='Login to your account']
	private static WebElement loginToYourAccount;


	// Register a new account
	@FindBy(xpath="//h4[text()='Register a new account']")
	private static WebElement registerNewAccount;
	// correct xpath //h1[text()='Register a new account']

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

	//Methods for login page

	/**
	 * This method logs in
	 * @param name - name to be completed
	 * @param pass - password to be completed
	 */

	public static void login( String name, String pass){
		Tacitus.getInstance().log("Logging in!");
		try {
			Tacitus.getInstance().log("Inserting the username!");
			username.click();
			username.sendKeys(name);
			Tacitus.getInstance().log("Inserting the password!");
			password.click();
			password.sendKeys(pass);
			Tacitus.getInstance().log(" Pressing Login Button!");
			login.click();
		} catch (NoSuchElementException e){
			Tacitus.getInstance().log("I could not log in!");
		}
      }

	/**
	 * This method checks if the Login page has loaded
	 * @return returns true if Login to your account text is visible
	 */
      public static boolean loginPageReady(){
		Tacitus.getInstance().log("Checking if Login Page is loaded!");
		boolean pageLoaded = FALSE;
		try{
			if(loginToYourAccount.isDisplayed()){
				Tacitus.getInstance().logSuccess("The login page has successfully loaded!");
				pageLoaded = TRUE;
			} else {
				Tacitus.getInstance().logFail("The login page is not loaded!");
				pageLoaded = FALSE;
			}
		} catch(NoSuchElementException e){
			Tacitus.getInstance().logFail("Login to your account text is not found!");
		}
		return pageLoaded ;
	  }

	//Methods for Register page

	/**
	 * This method creates a new account
	 * @param uName - userName to be completed
	 * @param pass - password to be completed
	 * @param eMail - email to be completed
	 */
	public static void registerAccount( String uName, String pass, String eMail){
	Tacitus.getInstance().log("Creating a new account");
	try {
		RegisterANewAccount.insertUserName(uName);
		createUsername.click();
//		createUsername.sendKeys(uName);

		createEmail.click();
		createEmail.sendKeys(eMail);

		createPassword.click();
		createPassword.sendKeys(pass);

		confirmPassword.click();
		confirmPassword.sendKeys(pass);

		newAccountRegister.click();
		DriverUtils.driverSleep(2000);
	} catch(NoSuchElementException e){
		Tacitus.getInstance().logError(" Could not create a new transaction", e);
	}
	}

	/**
	 * This method clicks on register button
	 */
	public static void registerNewAccount(){
		Tacitus.getInstance().log("Clicking on register link!");
			do {
				register.click();
				break;
			} while (!registerNewAccount.isDisplayed());
		}

}
