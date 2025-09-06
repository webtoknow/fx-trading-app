package utilities;

import driver.WebDriverThreadManager;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import utilities.logger.Tacitus;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AppUtils {


	/**
	 * Method used to get random strings for usage in test cases only used characters are letters and numbers
	 *
	 * @param length used to get the length of the required string
	 * @return It returns a String object
	 */
	public static String getRandomCharacterString(int length) {
		StringBuilder sb = new StringBuilder();
		String characters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
		for (int i = 0; i < length; i++) {
			int index = (int) (Math.random() * characters.length());
			sb.append(characters.charAt(index));
		}
		return sb.toString();
	}

	/**
	 * This method gets the system date in dd/MM/yyyy format
	 *
	 * @return returns the system date as a string
	 */
	public static String getSystemDate() {
		Tacitus.getInstance().log("Getting the system day, month and year!");
		try {
			DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			LocalDateTime now = LocalDateTime.now();
			return dtf.format(now);
		} catch (Exception e) {
			Tacitus.getInstance().log("The date could not be created!");
		}
		return null;
	}


	/**
	 * Method used to click WebElements through javascript
	 *
	 * @param webElement the element which is needed to be clicked through javascript injection
	 */
	public static void jsClick(WebElement webElement) {
		JavascriptExecutor executor = (JavascriptExecutor) WebDriverThreadManager.getDriver();
		executor.executeScript("arguments[0].click();", webElement);

	}
}