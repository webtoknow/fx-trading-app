package utilities.listeners;

import org.testng.ISuite;
import org.testng.ISuiteListener;
import utilities.logger.ExtentManager;
import utilities.logger.Tacitus;

import java.util.logging.LogManager;

/**
 * Class name: ${NAME}
 */
public class SuiteInterceptor implements ISuiteListener {

    @Override
    public void onStart(ISuite iSuite) {

        // Disable 3rd party logging - disables everything (only tacitus and ExtentReports will work!!!!)
        LogManager.getLogManager().reset();

        String suiteName;

        if (iSuite.getXmlSuite().getParentSuite() != null) {
            suiteName = iSuite.getXmlSuite().getParentSuite().getName();
        } else {
            suiteName = iSuite.getName();
        }

        ExtentManager.setSuiteName(suiteName);
    }

    @Override
    public void onFinish(ISuite iSuite) {
        ExtentManager.getInstance().flush();
    }
}
