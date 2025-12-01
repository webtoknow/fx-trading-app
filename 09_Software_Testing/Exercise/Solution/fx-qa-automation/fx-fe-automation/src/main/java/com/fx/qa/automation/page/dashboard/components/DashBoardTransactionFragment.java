package com.fx.qa.automation.page.dashboard.components;

import com.fx.qa.automation.driver.annotations.PageFragment;
import com.fx.qa.automation.page.Base;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@PageFragment
public class DashBoardTransactionFragment extends Base {
    @FindBy(xpath="//button[span[@class='fa fa-plus button-plus']]")
    private static WebElement addRateButton;

    @FindBy(name = "primaryCcy")
    private static WebElement primaryCurrency;

    @FindBy(name = "secondaryCcy")
    private static WebElement secondaryCurrency;

    @FindBy(xpath="//button[text()='Ok']")
    private static WebElement acceptCurrency;

    @FindBy(id="amount")
    private static WebElement ammount;

    @FindBy(name="tenor")
    private static WebElement tenor;

    @FindBy(xpath="//button[text()='Buy']")
    private static WebElement buy;

    @FindBy(xpath="//div[@aria-label='Transaction saved']")
    private static WebElement transactionSaved;


    /**
     * This method creates a sell transaction
     * @param ammountValue the amount for the transaction
     * @param tenorValue the tenor value
     */
    public boolean createTransaction(String ammountValue, String tenorValue, String pCurrency,
                                     String sCurrency){
        log.info("Creating the sell transaction");
        boolean transactionDone;
        try {
            createTransactionModal();
            log.info("Selecting the primary and secondary currency!");
            selectPrimaryCurrency(pCurrency);
            selectSecondaryCurrency(sCurrency);
            acceptCurrency.click();
            ammount.sendKeys(ammountValue);
            selectTenor(tenorValue);
            buy.click();
            if (transactionSaved.isDisplayed()) {
                log.info("The transaction was saved in the table!");
                transactionDone = TRUE;
            } else {
                log.error("The transaction was not created!");
                transactionDone = FALSE;
            }
        } catch(NoSuchElementException e) {
            transactionDone = FALSE;
        }
        return transactionDone;
    }

    private static void createTransactionModal() {
        log.info("Clicking on add new transaction button!");
        do {
            addRateButton.click();
        } while (!primaryCurrency.isDisplayed());
    }

    /**
     * Method used to select a value from Primary Currency drop down list
     * @param currencyValue used to select a value from Primary Currency DDL
     */
    private static void selectPrimaryCurrency(String currencyValue) {
        log.info("Finding the Primary Currency drop Down List");
        try {
            primaryCurrency.click();
            Select select = new Select(primaryCurrency);
            select.selectByVisibleText(currencyValue);
            log.info("Successfully selected the Primary Currency: {}", currencyValue);
        } catch (Exception ex) {
            log.error("Error when selecting the Primary Currency: {}", currencyValue, ex);
        }
    }

    /**
     * Method used to select a value from Secondary Currency drop down list
     * @param currencyValue used to select a value from Secondary currency DDL
     */
    private static void selectSecondaryCurrency(String currencyValue) {
       log.info("Finding the Secondary Currency drop Down List");
        try {
            secondaryCurrency.click();
            Select select = new Select(secondaryCurrency);
            select.selectByVisibleText(currencyValue);
            log.info("Successfully selected the Secondary Currency: {}", currencyValue);
        } catch (Exception ex) {
            log.error("Error when selecting the secondary Currency: {}", currencyValue, ex);
        }
    }

    /**
     * Method used to select a value from Ccy Pair drop down list
     * @param tenorValue used to select a value from Tenor DDL
     */
    public static void selectTenor(String tenorValue) {
        log.info("Finding the tenor DDL");
        try {
            tenor.click();
            Select select = new Select(tenor);
            select.selectByVisibleText(tenorValue);
            log.info("Successfully selected the tenor value: {}", tenorValue);
        } catch (Exception ex) {
            log.error("Error when selecting the tenor value: {}", tenorValue, ex);
        }
    }
}
