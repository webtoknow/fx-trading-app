package pages;

import driver.WebDriverThreadManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import utilities.AppUtils;
import utilities.logger.Tacitus;

import java.util.List;
import java.util.NoSuchElementException;

public class CalendarModal {

	private static WebDriver driver;

	public static void init() {
		driver = WebDriverThreadManager.getDriver();
		PageFactory.initElements(driver, CalendarModal.class);
	}


	//Page elements listing

	@FindBy(xpath="//bs-days-calendar-view//span[text()='‹']")
	private static WebElement previousMonth;

	@FindBy(xpath="//bs-days-calendar-view//span[text()='›']")
	private static WebElement nextMonth;

	@FindBy(xpath="//button[@class='current']")
	private static WebElement yearButton;

	@FindBy(xpath="//button[@class='current ng-star-inserted']")
	private static WebElement monthButton;

	@FindBy(xpath="//td[@class='week ng-star-inserted']")
	private static List<WebElement> weekNumber;

	@FindBy(xpath="//th[@aria-label='weekday']")
	private static List<WebElement> weekday;

	//Selects all Day elements, irrespective of active or inactive( previous or next month)
	@FindBy(xpath="//span[@bsdatepickerdaydecorator and not(contains(@class,'is-other-month'))]")
	//span[@ng-reflect-day='[object Object]' and not(@class='is-other-month')]
	//span[@bsdatepickerdaydecorator and not(contains(@class,'is-other-month'))]
	//span[@bsdatepickerdaydecorator and not(contains(@class,'is-other-month'))]
	private static List<WebElement> dayNumber;

	//Selects all days from other month
	@FindBy(xpath="//span[@class='is-other-month']")
	private static List<WebElement> dayOtherMonth;

	@FindBy(xpath="ssdadasd")
	private static List<List<WebElement>> tableElement;

	//CalendarModal methods

	/**
	 *  This method selects the day from the date modal window
	 * @param day the day to be selected
	 */
	public static void selectActiveDay( int day){
		Tacitus.getInstance().log("Selecting the day");
		try{
			AppUtils.jsClick(dayNumber.get(day));
		} catch(NoSuchElementException e)
		{
			Tacitus.getInstance().log("Could ot select the day");
		}
	       }



}
