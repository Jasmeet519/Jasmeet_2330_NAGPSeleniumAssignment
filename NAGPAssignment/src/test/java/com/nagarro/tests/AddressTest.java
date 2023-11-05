package com.nagarro.tests;

import com.nagarro.pageobjects.AddressPage;
import com.nagarro.pageobjects.HomePage;
import com.nagarro.pageobjects.LoginPage;
import com.nagarro.pageobjects.MyAccountPage;
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

public class AddressTest extends TestClient {
    private HomePage homePage;
    private Logger logger;
    private Helper helper;
    private Properties config;
    private LoginPage loginPage;
    private Map<String, String> addressValidationTestData;

    public AddressTest() {
        logger = LogManager.getLogger(getClass());
    }

    @BeforeClass(alwaysRun = true)
    public void setup() {
        homePage = new HomePage();
        helper = new Helper();
        config = helper.readConfigFile();

        // Setting up Test data for validations
        TestDataReader.init();
        addressValidationTestData = TestDataReader.getDataMap("AddressValidationTestData");
    }

    @Test(priority = 20, description = "Verify Address Detail on Logged-In User",groups = {"regression"})
    public void verifyAddressDetailOnLoggedInUser() {
        loginPage = homePage.clickOnSignInBtn();

        // Log-in to application
        loginPage.enterEmailAddressInLoginForm(config.getProperty("userName"));
        loginPage.enterPasswordInLoginForm(config.getProperty("password"));
        MyAccountPage myAccountPage = loginPage.clickSignIn();

        // Verifying: Address details are displaying correct for logged in user
        AddressPage addressPage = myAccountPage.clickMyAddressesOption();
        Assert.assertEquals(addressPage.getAddressDetails(), addressValidationTestData.get("addressDetails"));
    }
}
