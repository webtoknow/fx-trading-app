package utilities.logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import driver.WebDriverScreenshot;
import driver.WebDriverThreadManager;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.NoSuchWindowException;
import utilities.ConfigurationData;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class ExtentTestManager {

    static ExtentReports extentReport = ExtentManager.getInstance();

    static Map<Integer, ExtentTest> map = new HashMap<>();

    public static synchronized ExtentTest startTest(String testName, String description, String category) {
        ExtentTest test = extentReport.createTest(testName, description);
        test.assignCategory(category);
        map.put((int) Thread.currentThread().getId(), test);
        return test;
    }

    public static synchronized ExtentTest getTest() {
        return map.get((int) Thread.currentThread().getId());
    }

    public static synchronized ExtentTest getTest(int pid) {
        return map.get(pid);
    }

    public static synchronized void removeTest() {
        extentReport.removeTest(map.get((int) Thread.currentThread().getId()));
    }

    public static synchronized int getPid() {
        return (int) Thread.currentThread().getId();
    }

    public static synchronized void logScreenshot(Integer status) {
        String fileName;
        try {
            if (WebDriverThreadManager.getDriver() != null) {
                if (WebDriverThreadManager.getDriver().getWindowHandle() != null) {

                    if (ConfigurationData.getBase64Screenshot()) {
                        fileName = "data:image/gif;base64, " + WebDriverScreenshot.takeScreenshot(status);
                    } else {
                        fileName = "screenshots\\" + WebDriverScreenshot.takeScreenshot(status);
                    }

                    String code = "<br><a href='" + fileName + "' data-featherlight='image'><img width='40%' src='" + fileName + "' /></a>";

                    if (status == 1) {
                        getTest().pass("Test Evidence: " + code);
                    } else if (status == 2) {
                        getTest().fail("Test Evidence: " + code);
                    } else {
                        getTest().info("Test Evidence: " + code);
                    }
                }
            }
        } catch (NoSuchWindowException ex) {
            Tacitus.getInstance().logError("There is no window active to take screenshot", ex);
        } catch (Exception e) {
            Tacitus.getInstance().logError("An error occurred when attempting to log screenshot", e);
        }
    }

    public static synchronized void logFile(File file) {
        String fileName = file.getName();

        // Valid only for temporary .xml files.
        if (fileName.contains(".tmp") && fileName.contains(".xml")) {
            fileName = fileName.replace(".xml", "_");
            fileName = fileName.substring(0, fileName.length() - 3) + "xml";
        }

        saveFileToReport(file, fileName);
    }

    public static synchronized void logHtmlErrorFile(File file) {
        String fileName = file.getName();

        saveFileToReport(file, fileName);
    }

    private static void saveFileToReport(File file, String fileName) {

        String folder = fileName.substring(fileName.lastIndexOf("."));

        File newFile = new File(System.getProperty("user.dir")
                + ConfigurationData.getLoggingPath()
                + ExtentManager.getLogDirName()
                + "\\" + folder
                + "\\" + fileName);

        try {
            FileUtils.copyFile(file, newFile);
            FileUtils.forceDelete(file);
        } catch (IOException ex) {
            getTest().error("Unable to copy file to report location.");
        }

        String src = folder + "\\" + fileName;
        String code = "Test evidence data saved as: <a href='" + src + "' target='_blank'>" + fileName + "</a>";

        getTest().info(code);
    }
}
