*************** Complete Framework Folder Structure *****************
1. Created a MAVEN Project
2. Add POM.xml Dependencies for selenium-java(4.1.4), TestNG(7.1.0), WebdriverManager(5.1.1), SureFire Plugin (maven-surefire-plugin) and XMLParser(2.3.0-jaxb-1.0.6)
3. Created a Package as 'com.nagarro.base' at location 'src/main/java/' which have following classes:
    a. Driver Class which have methods to intialize WebDriver objects
    b. BasePageObject extends Driver class which have method to perform operations on webelement like getElementByXpath(),waitForElementToBeClickable(By by), etc
4.  Created a Package as 'com.nagarro.pageObjects' at location 'src/main/java/' which have:
    a. All Page Object classes and each class extent BasePageObject
    b. For some Page Object classes we have locators stored in 'src/main/resources/locators/' folder i.e. For Example, CheckoutPage class have locators store in 'src/main/resources/locators/checkoutPage.locators' which have xpathLocators
    c. Some locators are stored in Class as well with By object
    d. Each Page Object class have methods to perform operation on Page Class
5.  Created a Package as com.nagarro.testclient at location 'src/main/java/' which have below class:
    a. TestClient class have initialize() method which is used to initialize webdriver instance in @BeforeClass based on settings provided in config.properties file at location 'src/main/resources/config.properties'
    b. TestClient class have reset() method which is used to quit webdriver instance  @AfterClass
    c. All Test classes like LogingValidationTest, AddressTest, etc extent TestClient class
6. Created a Package as 'src/main/java/com/nagarro/utils' which have following classes:
    a. CsvDataProviders: Read csv file stored at location 'src/main/resources/dataproviders/<ClassName>/<MethodName>' and return Iterator<Object[]>
    Note: We usually store dataprovider data in csv with path as follow 'src/main/resources/dataproviders/<ClassName>/<MethodName>' so that when we use this class as dataProviderClass it will pick up csv file automatically

     For Example: LoginValidationTest class
     @Test(priority = 1, dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class, groups = {"regression"})
        public void verifyInvalidUserCredentials(Map<String, String> testData)

    Dataprovider csv: src/main/resources/dataproviders/LoginValidationTest/verifyInvalidUserCredentials.csv

    b. Helper: Class have readConfigFile() - which read config.properties file and readLocatorFile() - which read locators
    c. Report: For Initializing Extent Report
    d. Screenshot: have methods to create screenshots
    e. TestDataReader: have methods to read test data from xml file i.e.'testData.xml' and store it in map. It uses XMLParser class to generate map from testData.xml or any other xml
    f. XMLParser: Used to parse xml data into Map
7. Created a Package as com.nagarro.listeners which have following classes:
    a. ReportListner - this listener is added to Test Suite xml
    b. ScreenshotListener - this listener is added to TestClient class
8. Created a Package 'src/main/resources/dataproviders/' to store all csv files for Dataprovider. Csv files are store in format <ClassName>/<MethodName.csv> so it can we fetched by src/main/java/com/nagarro/utils/CsvDataProviders.java
9. Created a Package 'src/main/resources/locators' which have all locators file for Page Object classes
10. Created a Package 'src/main/resources/testsuites' which have below test suites:
    a. ParallelExecutionTest.xml - to execute tests parallel
    b. RegressionGroupTest.xml - to execute tests with group tag as regression
    c. SequentialExecutionSuite - to execute tests sequential
    d. SmokeGroupTest.xml - to execute tests with group tag as smoke
11. /logs
       a. If we run the tests sequentially then all the logs will be stored in main.log
       b. if we run the tests parallel then the logs will be stored based on the xml we are running
         for example if we are running ParallelExecutionTest.xml parallely then all the logs will be stored
         in the below files belonging to the respective threads. Note in xml test name is <test name="AllRegressionTest">
         TestNG-test=AllRegressionTest-1.log
         TestNG-test=AllRegressionTest-2.log
         TestNG-test=AllRegressionTest-3.log
         [INFO ] 2022-06-05 16:21:40.643 loginWithInvalidCredentials - Create Driver for Browser: chrome
 12. /Screenshots -> Screenshots are stored in this location
 13. /htmlReports -> Extent Report are stored in this location with
    a. htmlReports/reports/Current Test Report -> have Current Report
    b. htmlReports/reports/Archived Test Reports -> have Archived Report

********************************************************************************************************************************************************************************************************
To Run the Test:
1. All the Tests are placed in /src/test/java/com/nagarro/tests
2. All the TestNG xmls are placed under src/main/resources/testsuites
3. To run the code perform below steps:
   mvn compile
   mvn test --- It will run the SmokeGroupTest.xml as we have mentioned in POM.xml