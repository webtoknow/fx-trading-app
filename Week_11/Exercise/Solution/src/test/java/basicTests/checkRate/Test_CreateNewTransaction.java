package basicTests.checkRate;

import basicTests.createNewUser.Test_CreateNewUser;
import driver.BaseClass;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.DashBoard;
import pages.LandingPage;
import pages.enums.CcyPair;
import pages.enums.Currency;
import pages.enums.Tenor;
import utilities.AppUtils;
import utilities.listeners.RetryAnalyzer;
import utilities.logger.Tacitus;

public class Test_CreateNewTransaction extends BaseClass {

	//region testData
	private static final String CURRENCY_PAIR = CcyPair.EUR_RON.getValue();
	public static final String USERNAME = Test_CreateNewUser.USERNAME;
	public static final String PASSWORD = Test_CreateNewUser.PASSWORD;
	public static final String AMOUNT = "5";
	public static final String PRIMARY_CURRENCY = Currency.EUR.getValue();
	public static final String SECONDARY_CURRENCY = Currency.RON.getValue();
	public static final String TENOR = Tenor.SP.getValue();
	public static final String TRANSACTION_DATE = AppUtils.getSystemDate().toString();
	public static final int DAY = Integer.parseInt(TRANSACTION_DATE.substring(0,2)) -1;

	// end region test data


	@Test(description = "This test checks the currency rate for given transaction id and currency",
		dependsOnMethods="basicTests.createNewUser.Test_CreateNewUser.createNewUser",
		retryAnalyzer = RetryAnalyzer.class)

	private static void checkCurrenyRate() {

		LandingPage.login(USERNAME, PASSWORD);
		Assert.assertTrue(DashBoard.dashboardVisibleCheck(), "Test failed as the dashboard page did not load");
		DashBoard.createTransaction(AMOUNT, TENOR, PRIMARY_CURRENCY, SECONDARY_CURRENCY);
		String transactionDate = AppUtils.getSystemDate().toString();
		DashBoard.selectCurrencyPair(CURRENCY_PAIR);
		Tacitus.getInstance().log("Selected the currency pair");
		DashBoard.selectDate(DAY);
		Tacitus.getInstance().log("Selected the date");
		String lastRowValues = USERNAME+" "+PRIMARY_CURRENCY+"/"+SECONDARY_CURRENCY;
		String lastRowValues2 = " BUY "+ AMOUNT+" "+TENOR+" "
			+ transactionDate;
		Assert.assertTrue(DashBoard.getLastRowValues().contains(lastRowValues) && DashBoard.getLastRowValues()
							.contains(lastRowValues2),"The transaction could not be found in the table!");
		Tacitus.getInstance().logSuccess("The test case ended successfully!");
	}
}
