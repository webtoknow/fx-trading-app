package driver;


import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import utilities.ClassFinder;
import utilities.logger.Tacitus;

import java.lang.reflect.*;
import java.util.List;

/**
 *
 * All Test classes should inherit from this class.  This class makes sure the WebDriver is initialized anc destroyed as
 * well as all WebElements used in the POM package are initialized as needed.
 */
public class BaseClass {

    /**
     * Looks into the "pages" package and scans for Classes of any type. For each class found searches for and init()
     * method. If found it executes the method to initialize all WebElements from that class.
     */
    @BeforeMethod
    public synchronized void setup() {
        WebDriverThreadManager.startDriver();

        try {
            List<Class<?>> allClasses = ClassFinder.findAllClasses("pages");

            for (Class<?> foundClass : allClasses) {
                try {
                    if (!foundClass.isEnum()) {
                        Method method = foundClass.getMethod("init");
                        method.invoke(foundClass);
                    }
                } catch (NoSuchMethodException ex) {
                    Tacitus.getInstance().log("Unable to find init() method in class " + foundClass.getName());
                }
            }

        } catch (Exception ex) {
            Tacitus.getInstance().log("Unable to reinitialize static classes.");
        }
    }

    /**
     * Closes the WebDriver and waits for 2 seconds.
     */
    @AfterMethod
    public synchronized void tearDown() {
        WebDriverThreadManager.destroyDriver();
        DriverUtils.driverSleep(2);
    }
}