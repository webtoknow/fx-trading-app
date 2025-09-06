package utilities.listeners;

import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;
import utilities.ConfigurationData;
import utilities.logger.ExtentTestManager;
import utilities.logger.Tacitus;


/**
 *
 * Class name: ${NAME}
 *
 */
public class RetryAnalyzer implements IRetryAnalyzer {

    private static int attempts = 0;
    private static int maxAttempts = ConfigurationData.getFailedReattemptsNumber();

    @Override
    public boolean retry(ITestResult iTestResult) {
        if (!iTestResult.isSuccess()) {
            if (attempts < maxAttempts) {
                attempts++;

                ExtentTestManager.removeTest();
                return true;
            }
        } else {
            iTestResult.setStatus(ITestResult.SUCCESS);
        }

        Tacitus.getInstance().logError("Test was re-executed " + maxAttempts + " times, but it still failed.", null);
        return false;
    }

    public static Boolean shouldBeLogged() {
        return attempts == maxAttempts;
    }
}
