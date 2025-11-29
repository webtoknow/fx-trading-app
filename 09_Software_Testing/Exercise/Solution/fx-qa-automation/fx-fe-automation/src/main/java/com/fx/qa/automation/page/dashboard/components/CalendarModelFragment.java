package com.fx.qa.automation.page.dashboard.components;

import com.fx.qa.automation.driver.annotations.PageFragment;
import com.fx.qa.automation.page.Base;
import lombok.extern.slf4j.Slf4j;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import java.util.List;
import java.util.NoSuchElementException;

@Slf4j
@PageFragment
public class CalendarModelFragment extends Base {
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
    private static List<WebElement> dayNumber;

    //Selects all days from other month
    @FindBy(xpath="//span[@class='is-other-month']")
    private static List<WebElement> dayOtherMonth;

    @FindBy(xpath="ssdadasd")
    private static List<List<WebElement>> tableElement;

    @FindBy(id="date-picker-icon")
    private static WebElement dateIcon;

    //CalendarModal methods

    /**
     * This method selects the current date
     * @param day the day number
     */
    public void selectDate(int day){
        dateIcon.click();
        selectActiveDay(day);
    }

    /**
     *  This method selects the day from the date modal window
     * @param day the day to be selected
     */
    private void selectActiveDay( int day){
        log.info("Selecting the day");
        try{
            utils.hoverThenClickElement(dayNumber.get(day));
        } catch(NoSuchElementException e) {
            log.error("Could ot select the day");
        }
    }
}
