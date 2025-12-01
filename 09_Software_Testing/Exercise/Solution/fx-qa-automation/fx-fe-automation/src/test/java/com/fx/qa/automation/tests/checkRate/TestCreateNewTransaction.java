package com.fx.qa.automation.tests.checkRate;

import com.fx.qa.automation.SpringBaseTestNGTest;
import com.fx.qa.automation.enums.CcyPair;
import com.fx.qa.automation.enums.Currency;
import com.fx.qa.automation.enums.Tenor;
import com.fx.qa.automation.tests.createNewUser.TestCreateNewUser;
import com.fx.qa.automation.utils.AppUtils;
import lombok.extern.slf4j.Slf4j;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TestCreateNewTransaction extends SpringBaseTestNGTest {
    private static final String CURRENCY_PAIR = CcyPair.EUR_RON.getValue();
    public static final String USERNAME = TestCreateNewUser.USERNAME;
    public static final String PASSWORD = TestCreateNewUser.PASSWORD;
    public static final String AMOUNT = "5";
    public static final String PRIMARY_CURRENCY = Currency.EUR.getValue();
    public static final String SECONDARY_CURRENCY = Currency.RON.getValue();
    public static final String TENOR = Tenor.SP.getValue();
    public static final String TRANSACTION_DATE = AppUtils.getSystemDate().toString();
    public static final int DAY = Integer.parseInt(TRANSACTION_DATE.substring(0,2)) -1;

    @Test(description = "This test checks the currency rate for given transaction id and currency",
            dependsOnGroups = "userCreation")
    public void checkCurrenyRate() throws InterruptedException {
        loginPage.getLoginFormFragment().login(USERNAME, PASSWORD);
        Assert.assertTrue(dashboardPage.getDashBoardFormFragment().dashboardVisibleCheck(), "Test failed as the dashboard page did not load");
        dashboardPage.getDashBoardTransactionFragment().createTransaction(AMOUNT, TENOR, PRIMARY_CURRENCY, SECONDARY_CURRENCY);
        String transactionDate = AppUtils.getSystemDate().toString();
        dashboardPage.getDashBoardFormFragment().selectCurrencyPair(CURRENCY_PAIR);
        log.info("Selected the currency pair");
        dashboardPage.getCalendarModelFragment().selectDate(DAY);
        log.info("Selected the date");
        String lastRowValues = USERNAME + " " + PRIMARY_CURRENCY + "/" + SECONDARY_CURRENCY;
        String lastRowValues2 = " BUY " + AMOUNT + " " + TENOR + " "
                + transactionDate;
        TimeUnit.SECONDS.sleep(2);
        Assert.assertTrue(dashboardPage.getDashBoardFormFragment().getLastRowValues().contains(lastRowValues)
                && dashboardPage.getDashBoardFormFragment().getLastRowValues().contains(lastRowValues2),"The transaction could not be found in the table!");
        log.info("The test case ended successfully!");
    }
}
