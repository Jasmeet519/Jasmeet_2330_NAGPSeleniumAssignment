package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import com.nagarro.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;
import java.util.Properties;

public class LoginPage extends BasePageObject {

    private Logger logger = LogManager.getLogger(getClass());
    private Properties locators = getLocators("locators/loginPage");
    private Helper helper = new Helper();

    private By loginForm = By.id("login_form");
    private By signInBtn = By.id("SubmitLogin");
    private By createAccountBtn = By.id("SubmitCreate");
    private By emailAddressInCreateAccount = By.id("email_create");
    private By createAccountErrorMsg = By.id("create_account_error");


    public boolean isOpen() {
        try {
            waitForElementToBeDisplayed(loginForm);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error(noSuchElementException);
        } catch (TimeoutException timeoutException) {
            logger.error(timeoutException);
        }
        return isElementDisplayed(loginForm);
    }

    public void enterEmailAddressInLoginForm(String emailAddress) {
        WebElement elEmailAddress = getElementByXpath(locators.getProperty("registeredEmailAddressTxtFld"));
        enterValueInTextField(elEmailAddress, emailAddress);
    }

    public void enterPasswordInLoginForm(String password) {
        WebElement elPassword = getElementByXpath(locators.getProperty("registeredPasswordTxtFld"));
        enterValueInTextField(elPassword, password);
    }

    public MyAccountPage clickSignIn() {
        clickWebElement(signInBtn);
        MyAccountPage myAccountPage = new MyAccountPage();
        if (myAccountPage.isOpen())
            return myAccountPage;
        else return null;
    }

    public String getValidationMessage() {
        String xpathValidationMsg = locators.getProperty("validationMsg");
        waitForElementToBeDisplayed(xpathValidationMsg);
        WebElement elValidationMsg = getElementByXpath(xpathValidationMsg);
        logger.info("Validation message is: " + elValidationMsg.getText());
        return elValidationMsg.getText();
    }

    public MyAccountPage loginToApplication() {
        Properties config = helper.readConfigFile();
        enterEmailAddressInLoginForm(config.getProperty("userName"));
        enterPasswordInLoginForm(config.getProperty("password"));
        MyAccountPage myAccountPage = clickSignIn();
        return myAccountPage;
    }

    public void createAccount(String emailAddress){
        enterValueInTextField(getElement(emailAddressInCreateAccount),emailAddress);
        clickWebElement(createAccountBtn);
    }

    public String getCreateAccountErrorMsg(){
        return getTextValue(getElement(createAccountErrorMsg),null);
    }


}
