package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.NoSuchElementException;
import java.util.Properties;

public class ProductPage extends BasePageObject {
    private Logger logger = LogManager.getLogger(getClass());
    private Properties locators = getLocators("locators/productPage");

    public boolean isOpen(String categoryName) {
        String xpathCategoryName = locators.getProperty("pageLocator").replace("@@categoryName@@", categoryName);
        try {
            waitForElementToBeDisplayed(xpathCategoryName);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error(noSuchElementException);
        } catch (TimeoutException timeoutException) {
            logger.error(timeoutException);
        }
        return isElementDisplayed(xpathCategoryName);
    }

    public ShoppingCartPage addItemToCart(String itemName) {
        logger.info("Adding Item '" + itemName + "' to cart");
        WebElement elItemImage = getElementByXpath(locators.getProperty("itemImage").replace("@@itemName@@", itemName));
        moveToElement(elItemImage);
        clickWebElement(locators.getProperty("addToCartBtn").replace("@@itemName@@", itemName));
        ShoppingCartPage cartPage = new ShoppingCartPage();
        if (cartPage.isOpen())
            return cartPage;
        else return null;
    }


}
