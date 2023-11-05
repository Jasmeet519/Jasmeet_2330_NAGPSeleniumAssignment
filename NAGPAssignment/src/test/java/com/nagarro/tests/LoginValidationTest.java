package com.nagarro.tests;

import com.nagarro.pageobjects.MyAccountPage;
import com.nagarro.utils.CsvDataProviders;
import com.nagarro.pageobjects.HomePage;
import com.nagarro.pageobjects.LoginPage;
import com.nagarro.testclient.TestClient;
import com.nagarro.utils.Helper;
import com.nagarro.utils.TestDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;
import java.util.Properties;

public class LoginValidationTest extends TestClient {
    private HomePage homePage;
    private Logger logger;

    private Helper helper;
    private  LoginPage loginPage;
    private Properties config;
    private Map<String, String> valideUserLoginTestData;
    private Map<String,String> alreadyRegisterEmailAddressTestData;

    public LoginValidationTest() {
        logger = LogManager.getLogger(getClass());
    }

    @BeforeClass(alwaysRun = true)
    public void setup() {
        homePage = new HomePage();
        helper = new Helper();
        config = helper.readConfigFile();

        // Setting up Test data for validations
        TestDataReader.init();
        valideUserLoginTestData = TestDataReader.getDataMap("ValidUserLoginTestData");
        alreadyRegisterEmailAddressTestData = TestDataReader.getDataMap("AlreadyRegisterEmailAddressTestData");

    }


    @Test(priority = 1, dataProvider = "csvReader", dataProviderClass = CsvDataProviders.class, groups = {"regression"})
    public void verifyInvalidUserCredentials(Map<String, String> testData) {
        // I have failed 2nd test intentionally

        // Click on Sign In Button
        loginPage = homePage.clickOnSignInBtn();

        loginPage.enterEmailAddressInLoginForm(testData.get("emailAddress"));
        loginPage.enterPasswordInLoginForm(testData.get("password"));
        loginPage.clickSignIn();
        Assert.assertEquals(loginPage.getValidationMessage(), testData.get("validationMessage"), "Validation Message is displaying incorrect.");
    }

    @Test(priority = 2, groups = {"smoke","regression"})
    public void verifyAccountCannotBeCreatedWithAlreadyRegisteredEmail(){
        loginPage = homePage.clickOnSignInBtn();
        loginPage.createAccount(config.getProperty("userName"));
        Assert.assertEquals(loginPage.getCreateAccountErrorMsg(),alreadyRegisterEmailAddressTestData.get("errorMsg"));
    }

    @Test(priority = 3, groups = {"smoke","regression"})
    public void verifyValidUserCredentials() {
        loginPage = homePage.clickOnSignInBtn();

        // Reading: Valid user detail from config.properties file
        loginPage.enterEmailAddressInLoginForm(config.getProperty("userName"));
        loginPage.enterPasswordInLoginForm(config.getProperty("password"));

        MyAccountPage myAccountPage = loginPage.clickSignIn();

        //Verifying: Login is successful with valid credentials
        Assert.assertNotNull(myAccountPage);
        Assert.assertEquals(myAccountPage.getWelcomeText(), valideUserLoginTestData.get("welcomeText"));

    }


}
