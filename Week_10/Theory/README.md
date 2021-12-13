
**

## Why we are testing

**

 - The testing of any given product is done in order to ensure the
   product is doing what it was designed to do without issues. 
  - Testing ensures that the quality of the delivered product is high. Through QA
   effort risks can be removed (major issues detected before delivery).
   - Testing saves reources in long term(less production issues, for
   example -> Think of all auto dealers major callbacks. Are they
   cheap?) A satisfied customer will always buy more from the
   team/company which delivered a good product!!!

## **How we are testing**

**Types of testing**


 - Functional Testing
		
 1. White box

			White box testing is a method of software testing that tests internal programming structures of an application. This type of testing technique is known as clear box testing, open box testing, structural testing, and transparent box testing. Its operation is opposite to black-box testing and is used at unit, integration, and system levels of the testing process.

			The testing techniques of white box testing include:

				Statement Coverage – In this technique, all programming statements are applied with a minimal number of tests.
				Branch Coverage – In this type of technique, all branches shall be tested by running them a sequence of tests.
				Path Coverage – All paths including statements and branches are tested using this technique.
		
	2.Black box

		Black box testing is a type of software testing, which checks for the functionality of a software or an application without knowing the design, internal components, or structure of an application to be tested. It is also referred to as Specifications-based testing.

			The black box testing method is mainly used to find missing functions, performance errors, initialization errors, and errors while accessing the external database.

			The testing techniques of black box testing include

				Equivalence Partitioning – In equivalence partitioning, the input data of an application to be tested into equal partitions. This technique ensures to cover each partition at least once.

				Boundary Value Analysis – In boundary value analysis is a technique used in which the testing of an application is done using the boundary values.

				Cause-effect Graph – In this type of testing technique, causes are the inputs of a program and effects as the outputs of the program. Here, a graphical representation is used to show the relationship between the input and output and the factors that impact the outcome

				Experience Based Testing, also known as Error guessing and exploratory testing.
				This is the spot where functional QA Engineers shine. Using their creativity, skill and experience the QA team perform various scenarious in order to break the system/component/feature or at least show its limits.
				Usually the defects which result from these efforts ensure that the end user won't be able to accidentally stumble into some very bad error or crash.


				All-pairs Testing – In this approach, the software is tested using a combinatorial method to test all the possible discrete combinations of the parameters involved.
				


	
- Non-Functional testing

				1. Static testing - is a software testing technique which is used to check defects in software application without 		executing the code. Here we can include the documentation review, branch coverage review and code coverage review.
				2. Performance - this type of testing checks that a system can handle the stresses of a production environment in any circumstance (practically every ecommerce app on black friday).
				3. Security -  is a type of Software Testing that uncovers vulnerabilities, threats, risks in a software application and prevents malicious attacks from intruders. The purpose of Security Tests is to identify all possible loopholes and weaknesses of the software system which might result in a loss of information, revenue, repute at the hands of the employees or outsiders of the Organization.
				4. Usability - also known as User Experience(UX) Testing, is a testing method for measuring how easy and user-friendly a software application is. 	A small set of target end-users, use software application to expose usability defects. Usability testing mainly focuses on user’s ease of using application, flexibility of application to handle controls and ability of application to meet its objectives.
				5. Compatibility -Compatibility Testing is a type of Software testing to check whether your software is capable of running on different hardware, operating systems, applications, network environments or Mobile devices.

Testing can also be categorized by its granularity:

	1. Unit - test which are done in isolation of a given small component or given function (usually done by the dev team)
	2. Integration - tests which are validating how the components function together
	3. System testing - a type of testing which checks the whole system for faults and unwanted behaviour, usually done through black box techniques.
	4. Acceptance - usually done by a specific team or by the client, tests whch follow the flow of the system. This type of testing is not looking for defects, as are the rest of the methods of testing, but it is checking that the client's requirements are met (practically it encourages the system to pass).
		

		
**

## Writing a defect

**
 - how and why:
	A defect should inform the team of a problem found in the system under test, should be very explicit and easy to understand.
	A defect should have:
		Title – concise description of the problem
		Description – detailed description of the problem
		Steps to reproduce – actions performed to simulate the defective behavior
		Actual results - self explanatory
		Expected results - self explanatory
		Attached evidence – screenshots, logs, db extracts, other data evidence proving the existence of the defect
		Severity – defect importance from technical impact perspective (established by QA team)
		Priority – defect importance from business value perspective (established by business representatives)
		Assignee – delegated developer that looks into the issue - this will be handled by the defect management system used (JIRA, Bugzilla etc.)
		Status – current state the defect is in - same as assignee
		
	Other optional info:
		Build version, fix version, release version
		Root cause, environment, resolution
		SLA, detected in phase, labels, components
		Many others, depending on the tools used for defect management.

Example of a defect:

**Title**:

	The user can insert exponent into the Amount field
	
**Description:**

		When trying inserting the amount of currency the user wants to buy or sell he can also insert exponent in an all numerical, thus posibly creating transactions which can exploit the system.
	
**Steps to reproduce:**

			1.Launch the FXTrading app
			2.Login with a valid user
			3.Add an exhange container and choose a currency pairs
			4.Choose any Tenor
			5.Insert any number followed by an exponent, positive or negative -> 76e-5
			6.Buy or Sell
			7.See the transaction is processed without triggerring an error message.

**Expected results:**

	The user should not be able to insert any non integer number into the Amount field.
	
**Actual results:**

	The user can insert an exponent, thus posibly inserting a non integer number into the Amount field.
	
**Notes:**

	Please check the attached screenshot.
	![Evidence](/relative/exploit.JPG?raw=true "Evidence")
 
 
**

## Writing a testcase

** 

	Testcases are used to validate that a given functionality of a system is implemented correctly.
	A testcase contains test steps, test data, precondition, postcondition developed for specific test scenario to verify any requirement. The testcase includes specific variables or conditions, using which a testing engineer can compare expected and actual results to determine whether a software product is functioning as per the requirements of the customer.
	
	
Testcase can have two outcomes:
		1. positive - where the test is passed if a successful operation is done (The user can successfully purchase 1 item from an ecommerce app)
		2. negative  - where the user is passed if a validation message is trigerred (The user cannot successfully login into the system with an empty password)
		
	
Testcase need to have the following:
		

   1. Title and description - should be VERY specific so that the whole
    team can instantly indentify what is being done in that particular
    case 
   2. Prerequisites  (if aplicable) - this will contain all that is
    needed in order to start a testing scenario (username and passwords,
    access to databases, environment grants etc.) 		
   3. Test steps - these
    need to be succint and only checking one aspect, they will also need
    to have a description of what the QA needs to do, what is the
    EXPECTED result and what is the ACTUAL result. 		
   4. Postrequisites (if aplicable) - cleanup which needs to be done after the testing was
    performed, so that the system is not altered and make other testing
    a false positive or false negative.

	
Best Practises:
		While drafting a test case include the following information:

			- The description of what requirement is being tested
			- The explanation of how the system will be tested
			- The test setup like a version of an application under test, software, data files, operating system, hardware, security access, physical or logical date, time of day, prerequisites such as other tests and any other setup information pertinent to the requirements being tested
			- Inputs and outputs or actions and expected results
			- Any proofs or attachments
			- Use active case language
			- Test Case should not be more than 15 steps
			- An automated test script is commented with inputs, purpose and expected results
			- The setup offers an alternative to pre-requisite tests
			- With other tests, it should be an incorrect business scenario order
			- The test case should generate the same results every time no matter who tests it

## Example of a testcase
	
	
|Step action                |Expected result                          |Actual result                         |
|----------------|-------------------------------|-----------------------------|
|Go to fxTrading app Login page at: http://fx-trading-app.go.ro/login | User can reach fxTrading app        |            |
|Check the fxTrading Logo is displayed in the left side of the screen          |The logo is correctly shown in the left side of the screen            |           |

	
	
**

## Exercises:

**

**Exercise 1:**
		Create 3 testcases for the Login page:
			

 
 - 1 positive testcase 
 - 2 negative testcase
	
	
	
**Exercise 2:**

	Write a defect found on the Login page
	
**Exercise 3:**

	Write a testcase that would catch the defect given as example 
	
