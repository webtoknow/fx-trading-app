package utilities.logger;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.reporter.*;
import com.aventstack.extentreports.reporter.configuration.Theme;
import utilities.ConfigurationData;

public class ExtentManager {

    private static ExtentReports extentReport;
    private static String suiteName;
    private static String logDirName;

    public static ExtentReports getInstance() {
        if (extentReport == null) {
            setupExtentReports();
        }

        return extentReport;
    }

    private static ExtentReports setupExtentReports() {
        suiteName = ConfigurationData.getSuiteName();
        logDirName = suiteName + TacitusHelper.getDateAndTimeTitle();
        String reportDir = System.getProperty("user.dir") + ConfigurationData.getLoggingPath() + logDirName + "\\" + "index.html";

        ExtentHtmlReporter extentHtmlReporter = new ExtentHtmlReporter(reportDir);
        extentReport = new ExtentReports();
        extentReport.attachReporter(extentHtmlReporter);

        extentHtmlReporter.config().setDocumentTitle(ConfigurationData.getReportTitle());
        extentHtmlReporter.config().setReportName(suiteName + " " + ConfigurationData.getExecutionEnvironment());

        return extentReport;
    }

    public static String getLogDirName() {
        return logDirName;
    }

    public ExtentReports getReport() {
        return extentReport;
    }

    public static void setSuiteName(String newSuiteName) {
        suiteName = newSuiteName;
    }
}
