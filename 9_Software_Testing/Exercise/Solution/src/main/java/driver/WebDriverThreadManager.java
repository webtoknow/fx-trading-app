package driver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.ConfigurationData;
import utilities.logger.Tacitus;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Class name: ${NAME}
 *
 */
public class WebDriverThreadManager {

    private static WebDriverManager webDriverManager = new WebDriverManager();

    private static Map<Integer, WebDriver> webDriverMap = new HashMap<>();

    /**
     * Instantiates a session of an WebDriver object and navigates to the default URL.
     *
     * @return WebDriver object.
     */
    public static synchronized WebDriver startDriver() {
        forceCloseEverything();
        if (getDriver() == null) {
            String url = ConfigurationData.getExecutionEnvironment();
            presetupDriver(url);
        }
        return getDriver();
    }

    /**
     * Instantiates a session of an WebDriver object and navigates to a specific URL.
     *
     * @return WebDriver object.
     */
    public static synchronized WebDriver startDriver(String url) {
        forceCloseEverything();
        if (getDriver() == null) {
            presetupDriver(url);
        }
        return getDriver();
    }

    /**
     * Returns the current driver instance.
     *
     * @return WebDriver - the current WebDriver.
     */
    public static synchronized WebDriver getDriver() {
        return webDriverMap.get((int) Thread.currentThread().getId());
    }

    /**
     * Navigates to a specific url.
     *
     * @param url - the URL where we want to navigate.
     */
    public static synchronized void open(String url)  {
        getDriver().get(url);
    }

    /**
     * Closes the current WebDriver instance.
     */
    public static synchronized void destroyDriver() {
        if (getDriver() != null) {
            getDriver().quit();
            webDriverMap.remove((int) Thread.currentThread().getId());
        }
    }

    /**
     * Instantiate the driver and sets its parameters.
     *
     * @param url - URL where the driver should navigate as a first page.
     */
    private static synchronized void presetupDriver(String url) {
        WebDriver driver = webDriverManager.createWebDriver();

        webDriverMap.put((int) Thread.currentThread().getId(), driver);

        driver.manage().timeouts().implicitlyWait(25, TimeUnit.SECONDS);
        driver.manage().timeouts().pageLoadTimeout(25, TimeUnit.SECONDS);
        driver.manage().window().maximize();
        open(url);
    }

    /**
     * Method used to close every instance of IE
     */
    private static synchronized void forceCloseEverything() {
        try {
            if (ConfigurationData.getForceBrowserClosure()) {
                Tacitus.getInstance().log("Force-closing browsers and drivers...");
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K taskkill /F /IM iexplore.exe /im cmd.exe");
                Runtime.getRuntime().exec("cmd /c start cmd.exe /K taskkill /F /IM IEDriverServer.exe /im cmd.exe");
            } else {
                Tacitus.getInstance().log("Force-closing of browser and drivers mechanism disabled.");
            }

        } catch (Exception ex) {
            Tacitus.getInstance().logError("Unable to force close the browser and driver.", ex);
        }
    }


    /**
     * Method used to referesh the Browser window
     */
    public static void refreshBrowser () {
        Tacitus.getInstance().log("Refreshing the browser");
        try {
            getDriver().navigate().refresh();
        } catch (Exception ex) {
            Tacitus.getInstance().logError("The browser could not be refreshed", ex);
        }
    }

    // this method accepts the browser pop up message alert

    /**
     * Method used to accept Browser Alert Messages
     * @return true if it can accept, false otherwise
     */
    public static boolean acceptBrowserAlert(){
        Tacitus.getInstance().log("Accepting the browser alert");
        try {
            getDriver().switchTo().alert().accept();
            return true;
        } catch (Exception ex) {
            Tacitus.getInstance().logError("The warning window is not displayed", ex);
            return false;
        }
    }
}
