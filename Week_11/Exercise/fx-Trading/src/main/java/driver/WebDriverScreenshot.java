package driver;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import utilities.ConfigurationData;
import utilities.logger.ExtentManager;
import utilities.logger.Tacitus;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

/**
 */
public class WebDriverScreenshot {

    private static List<String> allFilenames;

    public static String takeScreenshot() {
        return captureScreenshot("");
    }

    public static String takeScreenshot(int result) {
        switch (result) {
            case 1:
                return captureScreenshot("0_");
            case 2:
                return captureScreenshot("1_");
            default:
                return captureScreenshot("");
        }
    }

    private static String getFileName() {
        if (allFilenames == null) {
            allFilenames = new ArrayList<>();
        }

        String randomID;

        do {
            randomID = UUID.randomUUID().toString().replace("-", "");
        } while (allFilenames.contains(randomID));

        allFilenames.add(randomID);

        return randomID + ".png";
    }

    private static String captureScreenshot(String result) {
        String fileName = result + getFileName();

        String filePath = System.getProperty("user.dir")
                + ConfigurationData.getLoggingPath()
                + ExtentManager.getLogDirName()
                + "\\screenshots\\"
                + fileName;

        try {
            if (WebDriverThreadManager.getDriver() != null) {
                WebDriver driver = WebDriverThreadManager.getDriver();
                if (ConfigurationData.getBase64Screenshot()) {
                    fileName = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BASE64);
                } else {
                    File screenshotFile = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
                    FileUtils.copyFile(screenshotFile, new File(filePath));
                }
            } else {
                Tacitus.getInstance().log("Screenshot not performed as WebDriver is not initialized... Is a screenshot really necessary?");
            }

        } catch (Exception ex) {
            Tacitus.getInstance().logError("Unable to capture screenshot", ex);
        }

        return fileName;
    }
}