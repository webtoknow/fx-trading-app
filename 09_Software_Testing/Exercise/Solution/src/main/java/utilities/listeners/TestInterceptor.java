package utilities.listeners;

import com.aventstack.extentreports.Status;
import driver.WebDriverThreadManager;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import utilities.logger.ExtentTestManager;
import utilities.logger.Tacitus;

/**
 *
 * Class name: ${NAME}
 *
 */
public class TestInterceptor implements ITestListener {

    @Override
    public void onTestStart(ITestResult iTestResult) {
        ExtentTestManager.startTest(iTestResult.getInstanceName(), iTestResult.getMethod().getDescription(), iTestResult.getTestContext().getCurrentXmlTest().getName());
    }

    @Override
    public void onTestSuccess(ITestResult iTestResult) {
        try {
            ExtentTestManager.getTest().log(Status.PASS, "Test Case: " + iTestResult.getName() + " - PASS");
            ExtentTestManager.logScreenshot(iTestResult.getStatus());
        } catch (Exception ex) {
            ExtentTestManager.getTest().warning("Unable to log test result/capture screenshot.");
        }
    }

    @Override
    public void onTestFailure(ITestResult iTestResult) {
        try {
            if (RetryAnalyzer.shouldBeLogged()) {
                ExtentTestManager.getTest().log(Status.FAIL, "Test Case: " + iTestResult.getName()+ " - FAIL");
                ExtentTestManager.logScreenshot(iTestResult.getStatus());
            }
        } catch (Exception ex) {
            ExtentTestManager.getTest().warning("Unable to log test result/capture screenshot.");
        }
    }

    @Override
    public void onTestSkipped(ITestResult iTestResult) {
        if (RetryAnalyzer.shouldBeLogged()) {
            ExtentTestManager.getTest().skip("Test Case " + iTestResult.getName() + " has been skipped.");
        }
    }

    @Override
    public void onTestFailedButWithinSuccessPercentage(ITestResult iTestResult) {

    }

    @Override
    public void onStart(ITestContext iTestContext) {

    }

    @Override
    public void onFinish(ITestContext iTestContext) {
        WebDriverThreadManager.destroyDriver();
    }
}
