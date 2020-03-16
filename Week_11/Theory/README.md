# Week 11 - Automation Testing Introduction

**Performance Testing Core Activities**
**Overview**

This document provides a high-level introduction to the most common core activities involved in automation UI testing your applications and the systems that support those applications.
Automation testing is a complex activity that cannot effectively be shaped into a “one-size-fits-all” or even a “one-size-fits-most” approach. Projects, environments, business drivers, acceptance criteria, technologies, timelines, legal implications, and available skills and tools simply make any notion of a common, universal approach unrealistic.
Automation testing should handle all the activities which are very repetitive and should handle all the prone to mistake or prone to fail test cases from the functional testing suites. The test suites should have test cases which are very simple, the framework should be coded to respect the programming principles (DRY, KISS, etc.)
The output of this module for students is for them to be able to know why is automation necessary, what it does, have an understanding of what automation does and how it can handle the UI testing and to know the basic structure of a suite.

## Table of contents

1. [Why Implement Automated testing?](#why-implement-automated-testing?)
2. [What should we automate?](#what-should-we-automate?)
3. [Challenges when working with automated testing](#challenges-when-working-with-automated-testing)
4. [Tools and integrations](#Tools-and-integrations)
5. [Example of a most basic UI testing Script](#example-of-a-most-basic-ui-test)

## Why implement Automated testing?

- **To do repeating work the functional teams should do**

Automation comes in handy when there is a need for sanity checks and regression testing.

- ``Regression testing is the part of the functional testing which ensures the functionalities which have already been in place are not broken by the new code inserted through the development process``
- ``Sanity testing is an activity which checks that the main functionalities of the applications are in working. This is done as the first part of testing a new build.``

 Automation is very good ally to the project team since it can handle very numerous test cases faster and reliable, every time in the same conditions without human interaction.

 Automation framework can reliably run for many items the same scenarios with the same data, it can repeat the same action as to ensure that there are no changes to the system/application under test while having multiple activities repeated over and over again.

 Automation is usually done to mimic the actions of average users through the systems and to get the functional testing teams the possibility of running more creative test scenarios as to be able to check more of the system/application under test.

 Automation does not handle the testing of a system/application alone, it is a way for the functional team to have more time to creatively test a system/application and it also guarantees the testing team that the scenarios are done in the exact same way over and over again.

- **To have testing integrated in a CI/CD lifecycle**

 Automated testing comes in very handy, alongside performance testing in a CI/CD lifecycle.

 Usually in a CI/CD system automation and performance testing are used to validate a build for the next phases.

``For example: A new version is being deployed from a lower environment to an upper one, at first there will be a suite of unit tests (which will validate that the code and its components are in working conditions and there are no mistakes inserted into the build), then a Sanity check will commence and a Regression suite will follow (the Sanity will validate the main functionalities ad the Regression will validate that the full functionalities are working without issues) and after all the functional automated tests there will commence the Performance testing which will validate that the system/application is responding in the agreed parameters. After all the aforementioned activities are marked as **PASSED** the build is validated and can proceed to the next phase. (Besides Automation and Performance there ay also be some ther testing types being done, such as Penetration testing and other security-wise testings)``

## What should we automate?

There are a few key questions which help us decide what we automate:

- What can be done in the chosen programming language?

        Any functionality from the system/application under test that can be interacted with (front end or back end) through a programming language should be automated so that the posibility of handling as many scenarios as possible is taken care of.

- What can be done without human/third party interactions

        For this question the team should be able to asses what can be scenarios can be executed without human or third party interractions or how much of that given data can be mocked for it to ensure a valid testing approach.

``For example: if there is a need to take some input data as JSON from another application, how costly would it be to have it as an input object, how would it be created and processed before the test scenario is executed.``

- What repetitive actions/scenarios are there?

``For example, in our course application, we have a login page, we could login and log out for 100 times to validate that the system can successfully fulfil that task as many times as it is needed.``

For a functional tester these kind of testing scenarios are time consuming and prone to mistakes.(If there is an error on the 10th successful login and the tester doesn't type the correct password on the 9th try, he or she will have to restart the whole process, thus an automated suite is more reliable on this scenarios than any human).

## Challenges when working with automated testing

- With automated testing there can be many challenges, but most common ones are in the following list:
  - Deciding what to automate from Front End and/or Back End
    - Automation can handle UI tets, Back End tests or both, depending on how the systems are built, their architecture, interfacing applications, etc.
  - Anything that is dynamic
    - This mostly applies to the front end on which a more responsive application and a more dynamic UI can generate a lot of challenges for the automation team to overcome
    ``For example: UI interface may have a whole different locators for the same element when viewed from a tablet/desktop browser/mobile phone etc. (buttons with other locators or just different options available in the app)``
  - Keeping it simple, small, robust and easy to maintain
    - First of all, it is a very good way to have smaller tests than larger ones and it is very good to set the granularity level from the beginning, otherwise many problems can occur along the way 
    - After creating a suite a QA Engineer may encounter many changes of the system which need to be handled, changes in the workflow of the application/changes of the environment etc.
    - The robustness of a test script is dictated by how the test can be successfully run or show a point of failure from the application and not have false positives or false negatives (pass despite a bug or fail without anything being wrong with the application)

## Tools and integrations

- Creating an automation framework requires usage of any tools, libraries and integrations with various other systems

**Tools**:

- For UI:
  - Most used Selenium, but there are other tools, like protractor, cypress.io, Appium (for mobile apps) etc.
  - Most UI tests require locators for the elements(id, classname, tag, id etc.)
- For Back End:
  - WebServices - Postman (for webservices), custom built clients(Web Services), Restassured (WebServices and other APIs), database connectors etc.
  - Programming language
    - Here the choice is mostly dictated by the knowledge of the team or by how fast can the team learn a certain programming language and also by how well a certain programming language is suited for what needs to be done
  - Software project management tool *Maven, Gradle, etc.

**Integrations**:

- Test Management tools
  - Test Management tools help the QA Teams prepare testing scenarios and validate them
  - Examples: HP ALM, Zephyr, DevTest, PractiTest, TestRail, TESTPAD, XRAY, HipTest etc.
- Reporting Tools:  
  - Reporting tools help us track issues (Bugs, tickets, tasks, etc.)
  - Examples: JIRA, HP ALM, BugZilla, BugHerd, Microsoft Azure DevOps Server, DevTrack etc.
- Continuous Integration/Continuous Delivery
  - The ability of an Automated Framework to be integrated into a pipeline for seamless CI/CD with tools like TeamCity, Jenkins etc. means that the tests can be run remotely and the results can be parsed in such a manner that will trigger the following step in that pipeline.

## Example of a most Basic UI Test

```java
package basicTests;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class basicTest {
    public class test {
        private WebDriver driver;

        /**
         * What is done before the actual test is run
         * The annotation handles the actions that are done before all the methods with @Test are run
         * A new instance of the WebDriver for Chrome is created and it is launched
         */
        @BeforeClass
        public void setup(){
            //actual location of the webdriver executable
            System.setProperty("webdriver.chrome.driver", "src\\main\\resources\\chromedriver.exe");
            driver = new ChromeDriver();
        }


        /**
         * Actual test case
         * Steps:
         *  1. go to the application
         *  2. print the actual URL - can also have a check here to see that the browser is not redirected elsewhere
         *  3. check the login button is present
         */
        @Test
        public void basicTest(){
            driver.get("http://norulmeu.go.ro:8000/login");
            System.out.println(driver.getCurrentUrl());
            Assert.assertTrue(driver.findElement(By.xpath("//button[@type='submit']")).isDisplayed()
                    , "Login button is not shown in the Landing page");

        }

        /**
         * This is what happens after the test is run
         * The browser is closed, optional here you can have some sort of cleanup, delete some data that was created
         * during the test or revert some changes so that other tests are not affected due to a possible test failure
         */
        @AfterClass
        public void tearDown(){
            driver.quit();
            //optional data clearance
        }
    }
}
```
