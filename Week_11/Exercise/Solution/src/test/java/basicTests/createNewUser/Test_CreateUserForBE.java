package basicTests.createNewUser;

import driver.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.LandingPage;
import utilities.AppUtils;

public class Test_CreateUserForBE extends BaseClass {

	//region testData
	public static final String USERNAME = "marius" ;
	public static final String PASSWORD = "1234567";
	private static final String EMAIL = "abc123@ab.com";
	//end region test data




	@Test
	public void createNewUser(){
		LandingPage.registerNewAccount();
		LandingPage.registerAccount(USERNAME,PASSWORD,EMAIL);
		Assert.assertTrue(LandingPage.loginPageReady(), "The login page was not loaded after registering the account");
	}

}
