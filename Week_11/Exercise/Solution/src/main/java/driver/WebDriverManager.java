package driver;

import com.profesorfalken.jpowershell.PowerShell;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import utilities.ConfigurationData;
import utilities.logger.Tacitus;

import java.io.File;

/**
 */
class WebDriverManager {
    /**
     * Creates a new WebDriver instance.
     *
     * @return - WebDriver object.
     */
    WebDriver createWebDriver() {
        BrowserTypes _browser = getDefaultBrowser();
        String _fileName = getFilePath(_browser.getFileName());

        switch (_browser) {
            case IE:
                deactivateProtectedMode();
                System.setProperty("webdriver.ie.driver", _fileName);
                Tacitus.getInstance().logSuccess("Created a new Internet Explorer instance.");
                return new InternetExplorerDriver();
            case CHROME:
                System.setProperty("webdriver.chrome.driver", _fileName);
                Tacitus.getInstance().logSuccess("Created a new Chrome instance.");
                return new ChromeDriver();
            default:
                Tacitus.getInstance().logError("Unable to start browser.", null);
                return null;
        }
    }

    /**
     * Deactivates protected mode on IE browsers
     */
    private void deactivateProtectedMode() {
        try {
            PowerShell powerShell = PowerShell.openSession();
            StringBuilder builder = new StringBuilder();
            int count = 0;
            while (count < 5) {
                builder.append("reg add ")
                        .append("\"HKCU\\SOFTWARE\\Microsoft\\Windows\\CurrentVersion\\Internet Settings\\Zones\\")
                        .append(count)
                        .append("\"")
                        .append(" /v ")
                        .append("2500 ")
                        .append("/t ")
                        .append("REG_DWORD ")
                        .append("/d ")
                        .append(3)
                        .append(" /f;")
                        .append("\r\n");
                count++;
            }

            builder.append("exit;\r\n");
            powerShell.executeCommand(builder.toString());
            if (!powerShell.executeCommand(builder.toString()).isError()) {
                Tacitus.getInstance().logSuccess("IE Protected Mode disabled successfully via PowerShell");
            }
        } catch (Exception ex) {
            Tacitus.getInstance().logError("Unable to disable Protected Mode for IE.", ex);
        }
    }

    /**
     * Gets the location of the browser file. The driver must always be placed withing the project's
     * resources folder.
     *
     * @return the driver's location.
     */
    private String getFilePath(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }

    /**
     * Retrieves the driver for the browser on which all scripts will be executed.
     *
     * @return BrowserTypes - the type of browser.
     */
    private BrowserTypes getDefaultBrowser() {
        String _browser = ConfigurationData.getDefaultBrowser();
        switch (_browser) {
            case "CHROME":
                Tacitus.getInstance().log("Current default browser is Chrome.");
                return BrowserTypes.CHROME;
            case "IE":
                Tacitus.getInstance().log("Current default browser is Internet Explorer.");
                return BrowserTypes.IE;
            default:
                Tacitus.getInstance().logError("Undefined browser type in config.properties.", null);
                return null;
        }
    }

    private DesiredCapabilities setDesiredCapabilities() {
        DesiredCapabilities _dc = DesiredCapabilities.internetExplorer();
        return _dc;
    }

    private enum BrowserTypes {
        CHROME("chromedriver.exe"),
        IE("IEDriverServer.exe");

        private String a;

        BrowserTypes(String a) {
            this.a = a;
        }

        public String getFileName() {
            return a;
        }
    }
}
