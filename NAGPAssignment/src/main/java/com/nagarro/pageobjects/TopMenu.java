package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Properties;

public class TopMenu extends BasePageObject {

    private Logger logger = LogManager.getLogger(getClass());
    private Properties locator = getLocators("locators/topMenu");

    public void clickMenu(String menuOption) {
        String xpathMenu = locator.getProperty("topMenu").replace("@@option@@", menuOption);
        clickWebElement(xpathMenu);
    }

    public ProductPage clickDressesMenu() {
        clickMenu("Dresses");
        ProductPage productPage = new ProductPage();
        if (productPage.isOpen("Dresses")) return productPage;
        else return null;
    }

    public ProductPage clickWomenMenu() {
        clickMenu("Women");
        ProductPage productPage = new ProductPage();
        if (productPage.isOpen("Women"))
            return productPage;
        else return null;
    }
}
