package driver;

import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.FluentWait;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ConfigurationData;
import utilities.logger.Tacitus;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 */
public class DriverUtils {

    private static String mainWindowHandle;

    public static void switchToWindow(String title) {
        Tacitus.getInstance().log("Looking for window \"" + title + "\" to switch to.");

        DriverUtils.driverSleep(3000);

        WebDriver _driver = WebDriverThreadManager.getDriver();

        for (String winHandle : _driver.getWindowHandles()) {
            if (_driver.switchTo().window(winHandle).getTitle().equals(title)) {
                Tacitus.getInstance().logSuccess("Found window with title \"" + title + "\".");
                _driver.switchTo().window(winHandle);
                break;
            } else if (winHandle.equals(title)) {
                Tacitus.getInstance().logSuccess("Found window with handle \"" + title + "\".");
                _driver.switchTo().window(winHandle);
                break;
            }
            Tacitus.getInstance().log("Current window is: " + _driver.switchTo().window(winHandle).getTitle());
        }
    }

    public static void driverSleep(int milliseconds) {
        try {
            Tacitus.getInstance().log("Waiting for " + milliseconds + " milliseconds.");
            Thread.sleep(milliseconds);
        } catch (Exception ex) {
            Tacitus.getInstance().logError("Unable to wait for " + milliseconds + " milliseconds.", ex);
        }
    }

    public static void setMainWindowHandle() {
        mainWindowHandle = WebDriverThreadManager.getDriver().getWindowHandle();
    }

    public static String getMainWindowHandle() {
        return mainWindowHandle;
    }

    static String getFileName() {
        String fileName = "";

        StackTraceElement[] stackTrace = Thread.currentThread().getStackTrace();
        try {
            for (StackTraceElement element : stackTrace) {
                if (element.getFileName().contains(ConfigurationData.getScriptsID())) {
                    fileName = element.getFileName().replace(".java", "");
                    break;
                }
            }
        } catch (Exception ex) {
            System.out.println("Unable to retrieve file name.");
            System.out.println(ex.getMessage());
        }

        return fileName;
    }

    static String getDateAndTimeTitle() {
        return new SimpleDateFormat("_yyyy_MM_dd_HH_mm").format(new Date());
    }


    /**
     * This method switches between the frames on the site
     * @param frameName - is requred as a webelement for the frame name
     */
    public static void switchFrame(WebElement frameName){
        Tacitus.getInstance().log("Searching the frame!");
        try {
            WebDriverThreadManager.getDriver().switchTo().frame(frameName);
            Tacitus.getInstance().logSuccess("Found the frame "+ frameName + "!");
        } catch (NoSuchElementException e){
            Tacitus.getInstance().logError("Frame "+frameName+" was not found!", e);
        }
    }

    /**
     * This method switches to the default context
     */
    public static void switchToDefault(){
        Tacitus.getInstance().log("Switching to default context");
        WebDriverThreadManager.getDriver().switchTo().defaultContent();
    }

    public static void closeAllButTitle(String title) {
        Tacitus.getInstance().log("Closing all but the the desired window, "+title);

        WebDriver _driver = WebDriverThreadManager.getDriver();
        DriverUtils.driverSleep(3000);
        _driver.getWindowHandles().forEach(s -> System.out.print(_driver.switchTo().window(s).getTitle()));
        for(String s : _driver.getWindowHandles()){
            _driver.switchTo().window(s);
            if(!_driver.getTitle().equalsIgnoreCase(title)){
                _driver.close();
            }
        }
        for(String s : _driver.getWindowHandles()){
            _driver.switchTo().window(s);
            if(_driver.getTitle().equalsIgnoreCase(title)){
                _driver.manage().window().maximize();
            }
        }

    }

    /**
     *  this method waits for a webelement to be visible
     * @param el - element to be checked
     */
    public static void waitForElement( WebElement el){
        WebDriver _driver = WebDriverThreadManager.getDriver();
        WebDriverWait wait = new WebDriverWait(_driver, 5);
        wait.until(ExpectedConditions.visibilityOf(el));
    }

    /**
     * This method waits for the element to be visible
     * @param e element element iot waits for
     */

    public  static void waitForElementToBeVisible(WebElement e){
        Tacitus.getInstance().log("Waiting for element" + e + "to be visible");
        try{
               FluentWait<WebDriver> fluentWait = new FluentWait<>(WebDriverThreadManager.getDriver())
                .withTimeout(120, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
            fluentWait.until(ExpectedConditions.visibilityOf(e));
        }catch (NoSuchElementException eex){
            Tacitus.getInstance().logError("The element could not be found in 40 seconds", eex);
        }catch (Exception ex ){
            Tacitus.getInstance().logError("The page exceeded 120 seconds to load", ex);
        }
    }

    /**
     * This method waits for the element to be clickable
     * @param e element element it waits for
     */
    public  static void waitforElementToBeClickable(WebElement e){
        Tacitus.getInstance().log("Wating for element" + e + "to be clickable");
        try{
            FluentWait<WebDriver> fluentWait = new FluentWait<>(WebDriverThreadManager.getDriver())
                .withTimeout(120, TimeUnit.SECONDS)
                .pollingEvery(100, TimeUnit.MILLISECONDS)
                .ignoring(NoSuchElementException.class);
            fluentWait.until(ExpectedConditions.elementToBeClickable(e));
        }catch (NoSuchElementException eex){
            Tacitus.getInstance().logError("The element could not be found in 120 seconds", eex);
        }catch (Exception ex ){
            Tacitus.getInstance().logError("The page exceeded 120 seconds to load", ex);
        }
    }
}
