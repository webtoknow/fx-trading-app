package utilities;

import utilities.logger.Tacitus;

import java.io.File;
import java.util.Properties;

/**
 *  * Class name: ${NAME}
 * <p>
 */
public class ConfigurationData {

    public static String getDefaultBrowser() {
        return getValueFromConfigurationFile("browser");
    }

    public static String getLoggingPath() {
        return getValueFromConfigurationFile("loggingPath");
    }

    public static String getScriptsID() {
        return getValueFromConfigurationFile("scriptsID");
    }

    public static String getReportTitle() {
        return getValueFromConfigurationFile("reportTitle");
    }

    public static String getExecutionEnvironment() {
        return getValueFromConfigurationFile("executionEnvironment");
    }

    public static Boolean getForceBrowserClosure() {
        return Boolean.valueOf(getValueFromConfigurationFile("forceBrowserClosure"));
    }

    public static Boolean getBase64Screenshot() {
        return Boolean.valueOf(getValueFromConfigurationFile("base64screenshot"));
    }

    public static Integer getFailedReattemptsNumber() {
        return Integer.valueOf(getValueFromConfigurationFile("failedReattempts"));
    }

    public static String getSQLUrl() {
        return getValueFromConfigurationFile("sqlurl");
    }

    public static String getSQLUsername() {
        return getValueFromConfigurationFile("sqlusername");
    }

    public static String getSQLPassword() {
        return getValueFromConfigurationFile("sqlpassword");
    }

    /**
     * Retrieves a value from the config.properties file.
     *
     * @param value - String - name of the value to be retrieved
     * @return String - the retrieved value
     */
    private static String getValueFromConfigurationFile(String value) {
        Properties _props = new Properties();
        String _propFileName = "config.properties";

        try {
            _props.load(ConfigurationData.class.getClassLoader().getResourceAsStream(_propFileName));
            return _props.getProperty(value);
        } catch (Exception ex) {
            Tacitus.getInstance().logError("Unable to retrieve value from config.properties. Value: " + value, ex);
            return null;
        }
    }

    /**
     * Gets the location of a file located in the resources folder.
     *
     * @param fileName the name of the file.
     * @return the files's location.
     */
    private static String getFilePath(String fileName) {
        ClassLoader classLoader = ConfigurationData.class.getClassLoader();
        File file = new File(classLoader.getResource(fileName).getFile());
        return file.getAbsolutePath();
    }

    public static String getSuiteName() {
        return getValueFromConfigurationFile("suiteName");
    }
}
