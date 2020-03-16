# Week 11 - Automation Testing Exercises

## Framework short description

The presented Framework is used to handle the UI tests for the developed application.
It is developed in Java, using Java 8, Test NG, Selenium and Maven.
To run the Full suite the only command needed is ```mvn test``` and it will run the suite from the pom.xml page using the maven surefire plugin.
The project uses the following structures:

- The webdriver is handled using a Singleton design pattern
- The webPages are designed as Page Object Model and selenium annotations to have the locators static and easier to be handled by the QA Engineers and methods are used for each interaction or group of interactions for the UI.
- The values from the drop-downs are stored into ENUMS for a safer usage
- There is the ability to retry a test several times, this ensures no false negatives are in place if there is an issue with the environment
- There is a report generated at the end using Extent Report library to have all the data gathered by the logger and screenshots in place for every scenario, pass, fail, error, etc.
- Suites are handled in .XML files using interfaces from Test NG and specific annotations
- In order to not have to write the Before Method and After Method on each test every test extends a Base Class which has these methods builtin (Class which handles the Browser start initialisation of the methods and tear-down)
- A screenshot is saved in case of failure or at the end of the test as test evidence.
- There is a ```ConfigurationData``` Class which handles the reading from ```config.properties``` file, These properties include the testing environment, which browser to use, etc
- ```AppUtils``` handles random string generators and other sorts of useful methods for data
- ```DriverUtils``` Class handles WebDriver useful methods like waiting for an element to be visible, waiting for it to be clickable, closing all pages but the one with a certain title, switching to a frame etc.

## Exercises

1.Locating a WebElement;

WebElements are representations of HTML objects which Selenium and thus the automated browser can interact with.

- ID ```findElement(By.id("IdName"))```
- Name ```findElement(By.name("Name"))```
- TagName ```findElement(By.tagName("HTML Tag Name"))```
- ClassName ```findElement(By.className("Element Class"))```
- LinkText ```findElement(By.linkText("LinkText"))```
- PartialLinkText ```findElement(By.partialLinkText("partialLinkText"))```
- CSS ```findElement(By.cssSelector(tag#id))``` ```findElement(By.cssSelector(tag.class))``` ```findElement(By.cssSelector(tag[attribute=value]))``` ```findElement(By.cssSelector(tag.class[attribute=value]))```
[More details about CSS](https://www.softwaretestingmaterial.com/css-selector-selenium-webdriver-tutorial/)
- xPath ```findElement(By.xpath("XPath"))```
This kind of locator is preferred since it produces the most reliable locators. XPath is designed to allow the navigation of XML documents, with the purpose of selecting individual elements, attributes, or some other part of an XML document for specific processing.
[Useful tutorial for xPath](https://www.guru99.com/xpath-selenium.html)

- [ ] Locate all elements from the landing page using xPath
- [ ] Create a new testcase and insert it into the suite.
- [ ] Build Methods for the WebElements located on Register New Account page. The tester should be able to fill in the data using those methods and to be able to log the actions using the framework.
- [ ] Make methods to check the validation messages (username required, email required, password matching, password minimum characters message)

More documentation for selenium can be found on [Selenium Official Page](https://selenium.dev/documentation/en/getting_started/)
