package pages;

import driver.WebDriverThreadManager;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.Select;
import utilities.logger.Tacitus;

import java.util.List;

import static java.lang.Boolean.FALSE;
import static java.lang.Boolean.TRUE;

public class DashBoard {

	private static WebDriver driver;

	public static void init(){
		driver = WebDriverThreadManager.getDriver();
		PageFactory.initElements(driver, DashBoard.class);
	}

	//Page elements listing

	//region FX rates View
	@FindBy(xpath="//button[span[@class='fa fa-plus button-plus']]")
	private static WebElement addRateButton;

	@FindBy(name = "primaryCcy")
	private static WebElement primaryCurrency;

	@FindBy(xpath="//button[text()='Ok']")
	private static WebElement acceptCurrency;

	@FindBy(name = "secondaryCcy")
	private static WebElement secondaryCurrency;

	@FindBy(id="amount")
	private static WebElement ammount;

	@FindBy(name="tenor")
	private static WebElement tenor;

	@FindBy(xpath="//button[text()='Sell']")
	private static WebElement sell;

	@FindBy(xpath="//button[text()='Buy']")
	private static WebElement buy;

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

	@FindBy(id="date-picker-icon")
	private static WebElement dateIcon;

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
	@FindBy(xpath="//div[@aria-label='Transaction saved']")
	private static WebElement transactionSaved;


	//Methods for page

	/**
	 * Method used to select a value from Ccy Pair drop down list
	 * @param currencyValue used to select a value from Ccy Pair DDL
	 */
	public static void selectCurrencyPair(String currencyValue) {
		Tacitus.getInstance().log("Finding the Currency Pair Drop Down");
		try {
			selectCcyPair.click();
			Select select = new Select(selectCcyPair);
			select.selectByVisibleText(currencyValue);
		Tacitus.getInstance().logSuccess("Successfully selected the Currency Pair value: "+ currencyValue);
		} catch (Exception ex) {
			Tacitus.getInstance().logError("Error when selecting the Currency Pair value: "+currencyValue, ex);
		}
	}


	/**
	 * Method used to select a value from Primary Currency drop down list
	 * @param currencyValue used to select a value from Primary Currency DDL
	 */
	public static void selectPrimaryCurrency(String currencyValue) {
		Tacitus.getInstance().log("Finding the Primary Currency drop Down List");
		try {
			primaryCurrency.click();
			Select select = new Select(primaryCurrency);
			select.selectByVisibleText(currencyValue);
			Tacitus.getInstance().logSuccess("Successfully selected the Primary Currency: "+ currencyValue);
		} catch (Exception ex) {
			Tacitus.getInstance().logError("Error when selecting the Primary Currency: "+currencyValue, ex);
		}
	}

	/**
	 * Method used to select a value from Secondary Currency drop down list
	 * @param currencyValue used to select a value from Secondary currency DDL
	 */
	public static void selectSecondaryCurrency(String currencyValue) {
		Tacitus.getInstance().log("Finding the Secondary Currency drop Down List");
		try {
			secondaryCurrency.click();
			Select select = new Select(secondaryCurrency);
			select.selectByVisibleText(currencyValue);
			Tacitus.getInstance().logSuccess("Successfully selected the Secondary Currency: "+ currencyValue);
		} catch (Exception ex) {
			Tacitus.getInstance().logError("Error when selecting the secondary Currency: "+currencyValue, ex);
		}
	}

	/**
	 * This method checks if the dashboard page has loaded
	 * @return returns true if fx logo image and fix rates, blotter view tests are visible
	 */
	public static boolean dashboardVisibleCheck(){
		boolean dbVisible = FALSE;
		Tacitus.getInstance().log(" Checking FX Logo, FX Rates View and Blotter View elements for visibility");
		try{
			if(fixRatesView.isDisplayed()&&blotterView.isDisplayed()&&fxLogo.isDisplayed()){
				Tacitus.getInstance().logSuccess("The dashboard page is loaded!");
				dbVisible = TRUE;
			} else {
				Tacitus.getInstance().logFail("The dashboard page did not load!");
				dbVisible = FALSE;
			}
		} catch(NoSuchElementException e){
			Tacitus.getInstance().log("Could not start the check for the visibility of dashboard page!");
		} catch(NullPointerException e){
			Tacitus.getInstance().log("Could not start the check for the visibility of dashboard page!");
		}
		return dbVisible;
	}

	/**
	 * this method opens up the transaction window
	 */
	public static void createTransactionModal(){
		Tacitus.getInstance().log("Clicking on add new transaction button!");
		do {
			addRateButton.click();
		} while (!primaryCurrency.isDisplayed());
	}

	/**
	 * This method selects the current date
	 * @param day the day number
	 */
	public static void selectDate(int day){
		dateIcon.click();
		CalendarModal.selectActiveDay(day);
	}

	/**
	 * This method creates a sell transaction
	 * @param ammountValue the amount for the transaction
	 * @param tenorValue the tenor value
	 */
	public static boolean createTransaction(String ammountValue, String tenorValue, String pCurrency,
											String sCurrency){
		Tacitus.getInstance().log("Creating the sell transaction");
		boolean transactionDone;
		try {
			createTransactionModal();
			Tacitus.getInstance().log("Selecting the primary and secondary currency!");
			selectPrimaryCurrency(pCurrency);
			selectSecondaryCurrency(sCurrency);
			acceptCurrency.click();
			ammount.sendKeys(ammountValue);
			selectTenor(tenorValue);
			buy.click();
			if (transactionSaved.isDisplayed()) {
				Tacitus.getInstance().log("The transaction was saved in the table!");
				transactionDone = TRUE;
			} else {
				Tacitus.getInstance().log("The transaction was not created!");
				transactionDone = FALSE;
			}
			} catch(NoSuchElementException e){
			  transactionDone = FALSE;
		}
		return transactionDone;
	}

	/**
	 * Method used to select a value from Ccy Pair drop down list
	 * @param tenorValue used to select a value from Tenor DDL
	 */
	public static void selectTenor(String tenorValue) {
		Tacitus.getInstance().log("Finding the tenor DDL");
		try {
			tenor.click();
			Select select = new Select(tenor);
			select.selectByVisibleText(tenorValue);
			Tacitus.getInstance().logSuccess("Successfully selected the tenor value: "+ tenorValue);
		} catch (Exception ex) {
			Tacitus.getInstance().logError("Error when selecting the tenor value: "+tenorValue, ex);
		}
	}


	/**
	 *
	 * @param id used to select the id of the transaction
	 * @param rate used to check the rate for the transaction id
	 * @return returns true if the transaction id matches the desired value
	 */
	public static Boolean checkIdRate(String id, String rate) {
		Tacitus.getInstance().log("Finding the rate for the given currency pair");
		boolean matchRate;
		try {
			for (WebElement webElement: transactionIdRows){
				if (webElement.getText().equalsIgnoreCase(id)
					&& rateRows.get(transactionIdRows.indexOf(webElement)).getText().equalsIgnoreCase(rate)){
					Tacitus.getInstance().logSuccess("The rate for the id: "+ id + "is the right one!");
					matchRate = TRUE;
					return matchRate;
				} else {
					Tacitus.getInstance().log("Could not find the rate you were searching that corresponds to" +
											  " currency pair");
					matchRate = FALSE;
					return  matchRate;
				}
			}
		} catch (Exception ex) {
			Tacitus.getInstance().logError("There was an error when searching the currency pair ", ex);
			return false;
		}
		return null;
	}

	/**
	 *  This method gets the last row values from the transaction table
	 * @return returns the string with the values
	 */
	public static String getLastRowValues() {
		Tacitus.getInstance().log("Getting the transaction details from the table!");
		try {
			int rowNumbers = tbody.findElements(By.xpath("./tr")).size() - 1;
			String lastRowValues = tbody.findElements(By.xpath("./tr")).get(rowNumbers).getText();
			Tacitus.getInstance().log("Found the transaction in the table!");
			return lastRowValues;
		} catch (NullPointerException e) {
			Tacitus.getInstance().logError("The transaction could not be found!", e);
		}
		return null;
	}


	}
