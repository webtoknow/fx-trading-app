package basicTests.login;

import driver.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashBoard;
import pages.LandingPage;
import utilities.listeners.RetryAnalyzer;
import utilities.logger.Tacitus;

public class Test_Login extends BaseClass {

	//region testData
	public static final String USERNAME = "astronaut";
	public static final String PASSWORD = "astronauc";

	//end region test data


	// region test description
	@Test(description = "This scenario the log in function is tested", retryAnalyzer = RetryAnalyzer.class)
	// end region test description


	// region test case


	public void login(){
	 LandingPage.login(USERNAME, PASSWORD);
	 Assert.assertTrue(DashBoard.dashboardVisibleCheck(), "Test failed as The dashboard page did not load");
	 Tacitus.getInstance().logSuccess("The Login test case ended successfully!");
	}

	// end region test case
}
