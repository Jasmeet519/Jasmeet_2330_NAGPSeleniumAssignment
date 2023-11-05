package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.checkerframework.checker.units.qual.A;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;

public class MyAccountPage extends BasePageObject {
    private Logger logger = LogManager.getLogger(getClass());
    private TopMenu topMenu;


    private By welcomePage = By.className("info-account");
    private By myAddressBtn = By.xpath("//ul[@class='myaccount-link-list']/li/a/span[text()='My addresses']");

    public MyAccountPage() {
        topMenu = new TopMenu();
    }

    public boolean isOpen() {
        try {
            waitForElementToBeDisplayed(welcomePage);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error(noSuchElementException);
        } catch (TimeoutException timeoutException) {
            logger.error(timeoutException);
        }
        return isElementDisplayed(welcomePage);
    }

    public String getWelcomeText() {
        WebElement elWelcomePage = getElement(welcomePage);
        logger.info("Welcome page heading is: " + elWelcomePage.getText());
        return elWelcomePage.getText();
    }

    public AddressPage clickMyAddressesOption() {
        clickWebElement(myAddressBtn);
        AddressPage addressPage = new AddressPage();
        if (addressPage.isOpen())
            return addressPage;
        else return null;
    }

    public TopMenu getTopMenu() {
        return topMenu;
    }

    public void setTopMenu(TopMenu topMenu) {
        this.topMenu = topMenu;
    }
}
