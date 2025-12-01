package com.fx.qa.automation.page.dashboard.components;

import com.fx.qa.automation.driver.annotations.PageFragment;
import com.fx.qa.automation.page.Base;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

@Slf4j
@PageFragment
public class DashBoardFormFragment extends Base {
    //region FX rates View
    @FindBy(xpath="//button[text()='Sell']")
    private static WebElement sell;

    @FindBy(xpath="//span[@class='fa fa-times close']")
    private static WebElement closeTransaction;

    // region Blotter view
    @FindBy(id="Ccy")
    private static WebElement selectCcyPair;

    @FindBy (xpath="//div[@class='input-group input-group-sm']")
    private static WebElement datePickerField;

    @FindBy(xpath="//input[@bsdatepicker]")
    private static WebElement datePickerInput;

    @FindBy(xpath="//app-blotter-view/div[3]")
    private static WebElement transactionTable;

    @FindBy(xpath="//tbody")
    private static WebElement tbody;

    @FindBy(xpath ="//h4[text()='Fx Rates View']")
    private static WebElement fixRatesView;

    @FindBy(xpath ="//h4[text()='Blotter View']")
    private static WebElement blotterView;

    @FindBy(xpath="//img[@class='fx-main-logo']")
    private static WebElement fxLogo;

    @FindBy(xpath="//tr/td[1]")
    private static List<WebElement> transactionIdRows;

    @FindBy(xpath="//tr/td[2]")
    private static List<WebElement> usernameRows;

    @FindBy(xpath="//tr/td[3]")
    private static List<WebElement> ccyPairRows;

    @FindBy(xpath="//tr/td[4]")
    private static List<WebElement> rateRows;

    @FindBy(xpath="//tr/td[5]")
    private static List<WebElement> actionRows;

    @FindBy(xpath="//tr/td[6]")
    private static List<WebElement> notionalRows;

    @FindBy(xpath="//tr/td[7]")
    private static List<WebElement> tenorRows;

    @FindBy(xpath="//tr/td[8]")
    private static List<WebElement> dateRows;

    //region toast messages

    /**
     * This method checks if the dashboard page has loaded
     * @return returns true if fx logo image and fix rates, blotter view tests are visible
     */
    public boolean dashboardVisibleCheck() {
        boolean dbVisible = FALSE;
        log.info(" Checking FX Logo, FX Rates View and Blotter View elements for visibility");
        try{
            utils.waitForElementToBeVisible(fixRatesView);
            utils.waitForElementToBeVisible(blotterView);
            utils.waitForElementToBeVisible(fxLogo);

            if(fixRatesView.isDisplayed() && blotterView.isDisplayed() && fxLogo.isDisplayed()){
                log.info("The dashboard page is loaded!");
                dbVisible = TRUE;
            } else {
                log.error("The dashboard page did not load!");
            }
        } catch(NoSuchElementException | NullPointerException e){
            log.error("Could not start the check for the visibility of dashboard page!");
        }
        return dbVisible;
    }

    /**
     * Method used to select a value from Ccy Pair drop down list
     * @param currencyValue used to select a value from Ccy Pair DDL
     */
    public void selectCurrencyPair(String currencyValue) {
        log.info("Finding the Currency Pair Drop Down");
        try {
            selectCcyPair.click();
            Select select = new Select(selectCcyPair);
            select.selectByVisibleText(currencyValue);
            log.info("Successfully selected the Currency Pair value: {}", currencyValue);
        } catch (Exception ex) {
            log.error("Error when selecting the Currency Pair value: {}", currencyValue, ex);
        }
    }

    /**
     *  This method gets the last row values from the transaction table
     * @return returns the string with the values
     */
    public String getLastRowValues() {
        log.info("Getting the transaction details from the table!");
        try {
            int rowNumbers = tbody.findElements(By.xpath("./tr")).size() - 1;
            String lastRowValues = tbody.findElements(By.xpath("./tr")).get(rowNumbers).getText();
            log.info("Found the transaction in the table!");
            return lastRowValues;
        } catch (NullPointerException e) {
            log.error("The transaction could not be found!", e);
        }
        return null;
    }
}
