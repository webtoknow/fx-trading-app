package utilities.logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ChartLocation;
import com.aventstack.extentreports.reporter.configuration.Theme;
import driver.WebDriverScreenshot;
import driver.WebDriverThreadManager;
import org.openqa.selenium.WebElement;
import utilities.ConfigurationData;

import java.io.File;
import java.io.FileWriter;
import java.util.UUID;

public class Tacitus {
    private static Tacitus instance;

    private Tacitus() {
    }

    public static Tacitus getInstance() {
        if (instance == null) {
            instance = new Tacitus();
        }

        return instance;
    }

    /**
     * This method logs an .html file in the report as evidence when an WebElement is not found.
     *
     * @param element - the WebElement that was not found
     */
    public static void logElementNotFoundException(WebElement element) {
        getInstance().logScreenshot();
        getInstance().log("Element not found exception raised for element: " + element);
        getInstance().logHtmlError(WebDriverThreadManager.getDriver().getPageSource());
    }

    //==================================================================================================================
    // Console AND Report Logging
    //==================================================================================================================

    public void log(String message) {
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().info(message);
        }

        TacitusLogger.depthLogger(message);
    }

    public void log(String message, int pid) {
        if (ExtentTestManager.getTest(pid) != null) {
            ExtentTestManager.getTest(pid).info(message);
        }

        TacitusLogger.depthLogger(message);
    }

    public void logSuccess(String message) {
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().pass(message);
        }

        TacitusLogger.depthSuccessLogger(message);
    }

    public void logSuccess(String message, int pid) {
        ExtentTestManager.getPid();

        if (ExtentTestManager.getTest(pid) != null) {
            ExtentTestManager.getTest(pid).pass(message);
        }

        TacitusLogger.depthSuccessLogger(message);
    }

    public void logFail(String message) {
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().fail(message);
        }

        TacitusLogger.depthLogger(message);
    }

    public void logFail(String message, int pid) {
        if (ExtentTestManager.getTest(pid) != null) {
            ExtentTestManager.getTest(pid).fail(message);
        }

        TacitusLogger.depthLogger(message);
    }

    public void logError(String message, Exception ex) {
        message = "ERROR: " + message;

        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().error(message);
            if (ex != null) {
                ExtentTestManager.getTest().error(ex.getMessage());
            }
        }

        TacitusLogger.depthErrorLogger(message, ex);
    }

    public void logError(String message, Exception ex, int pid) {
        message = "ERROR: " + message;

        if (ExtentTestManager.getTest(pid) != null) {
            ExtentTestManager.getTest(pid).error(message);
            if (ex != null) {
                ExtentTestManager.getTest(pid).error(ex.getMessage());
            }
        }

        TacitusLogger.depthErrorLogger(message, ex);
    }

    //==================================================================================================================
    // Console ONLY Logging
    //==================================================================================================================

    public void consoleLog(String message) {
        TacitusLogger.depthLogger(message);
    }

    public void consoleLogSuccess(String message) {
        TacitusLogger.depthSuccessLogger(message);
    }

    public void consoleLogError(String message, Exception ex) {
        message = "ERROR: " + message;

        TacitusLogger.depthErrorLogger(message, ex);
    }

    public void consoleLogFail(String message) {
        TacitusLogger.depthErrorLogger(message, null);
    }

    //==================================================================================================================
    // Report ONLY Logging
    //==================================================================================================================

    public void logReportSuccess(String message) {
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().pass(message);
        }
    }

    public void logReportFail(String message) {
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.getTest().fail(message);
        }
    }

    public void logFile(File file) {
        if (ExtentTestManager.getTest() != null) {
            ExtentTestManager.logFile(file);
        }
    }

    public void logHtmlError(String htmlString) {
        if (ExtentTestManager.getTest() != null) {
            try {
                File htmlFile = new File(UUID.randomUUID().toString().replace("-", "") + ".html");
                FileWriter fw = new FileWriter(htmlFile);
                fw.write(htmlString);
                fw.close();
                ExtentTestManager.logHtmlErrorFile(htmlFile);
                log("Successfully saved html failure test evidence " + htmlFile);
                htmlFile.delete();
            } catch (Exception ex) {
                logError("Unable to create .html file", ex);
            }
        }
    }

    public void logScreenshot() {
        ExtentTestManager.logScreenshot(-1);
    }
}