package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;

import java.util.Properties;

public class HomePage extends BasePageObject {
    private Logger logger = LogManager.getLogger(getClass());
    private Properties locators = getLocators("locators/homepage");

    private By signInBtn = By.className("login");

    public HomePage(){
        openApplication();
    }


    public LoginPage clickOnSignInBtn() {
        clickWebElement(signInBtn);
        LoginPage loginPage = new LoginPage();
        if (loginPage.isOpen())
            return loginPage;
        else return null;
    }



}
