package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.*;

public class ShoppingCartPage extends BasePageObject {
    private Logger logger = LogManager.getLogger(getClass());
    private Properties locators = getLocators("locators/shoppingCartPage");

    private By shoppingCartDialog = By.id("layer_cart");
    private By checkoutBtn = By.id("button_order_cart");

    public boolean isOpen() {
        try {
            waitForElementToBeDisplayed(shoppingCartDialog);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error(noSuchElementException);
        } catch (TimeoutException timeoutException) {
            logger.error(timeoutException);
        }
        return isElementDisplayed(shoppingCartDialog);
    }

    public String getProductInfo(String itemInfo) {
        String xpathItemInfo = locators.getProperty("itemInfo").replace("@@itemDetail@@", itemInfo);
        return getTextValue(getElementByXpath(xpathItemInfo), null);
    }

    public String getSuccessMsg() {
        return getTextValue(getElementByXpath(locators.getProperty("successMsg")), null);
    }

    public void clickContinueShopping() {
        clickWebElement(locators.getProperty("continueShoppingBtn"));
        waitForElementToBeInvisible(locators.getProperty("continueShoppingBtn"));
    }

    public String getCountOfProductsInCart() {
        WebElement elProductCounts = getElementByXpath(locators.getProperty("countOfProductsInCart"));
        return getTextValue(elProductCounts, null);
    }

    public void expandShoppingCartBlock() {
        int productCounts = Integer.parseInt(getCountOfProductsInCart());
        if (productCounts > 0)
            moveToElement(getElementByXpath(locators.getProperty("countOfProductsInCart")));
        else logger.error("No Items present in shopping cart");
    }

    public Map getItemInfoInShoppingCartBlock() {
        int numberOfProductsInCart = Integer.parseInt(getCountOfProductsInCart());
        List<WebElement> listCartInfo = getElements(locators.getProperty("cartInfo"));
        Map<String, String> cartInfo = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfProductsInCart; i++) {
            for (WebElement elCartInfo : listCartInfo) {
                cartInfo.put(elCartInfo.getAttribute("class") + i, elCartInfo.getText());
            }
        }
        logger.info("Cart info is: " + cartInfo);
        return cartInfo;
    }

    public String getCartPriceInShoppingCart(String priceType) {
        String xpathPrice = locators.getProperty("cartPrice").replace("@@priceType@@", priceType);
        return getTextValue(getElementByXpath(xpathPrice), null);
    }

    public CheckoutPage clickOnCheckout(){
        clickWebElement(checkoutBtn);
        CheckoutPage checkoutPage = new CheckoutPage();
        if(checkoutPage.isOpen())
            return checkoutPage;
        else return null;
    }


}
