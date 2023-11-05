package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.*;
import java.util.function.Predicate;

public class CheckoutPage extends BasePageObject {
    private Logger logger = LogManager.getLogger(getClass());
    private Properties locators = getLocators("locators/checkoutPage");
    private TopMenu topMenu;
    private ShoppingCartPage shoppingCartPage;

    public CheckoutPage() {
        topMenu = new TopMenu();
        shoppingCartPage = new ShoppingCartPage();

    }

    private By checkoutPage = By.id("order");
    private Predicate<WebElement> checkGreenColor = webelement -> {
        String color = webelement.getCssValue("border-bottom-color");
        logger.info("Color: " + color);
        return color.contains("rgba(32, 137, 49, 1)");
    };

    public boolean isOpen() {
        try {
            waitForElementToBeDisplayed(checkoutPage);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error(noSuchElementException);
        } catch (TimeoutException timeoutException) {
            logger.error(timeoutException);
        }
        return isElementDisplayed(checkoutPage);
    }

    public TopMenu getTopMenu() {
        return topMenu;
    }

    public void setTopMenu(TopMenu topMenu) {
        this.topMenu = topMenu;
    }

    public Map getProductInfoOnCheckoutForm() {
        int numberOfItems = Integer.parseInt(shoppingCartPage.getCountOfProductsInCart());
        Map<String, String> productInfo = new LinkedHashMap<>();

        for (int i = 1; i <= numberOfItems; i++) {
            List<WebElement> productDetails = getElements(locators.getProperty("productInfoForRow").replace("@@row@@", Integer.toString(i)));
            for (WebElement elProductInfo : productDetails) {
                String key = elProductInfo.getAttribute("class").split(" ")[0];
                if (key.equals("cart_description"))
                    productInfo.put(key + i, getCartDescriptionOnCheckoutForm(i));
                else if (key.equals("cart_quantity"))
                    productInfo.put(key + i, getCartQuantityOnCheckoutForm(i));
                else
                    productInfo.put(key + i, getTextValue(elProductInfo, null));
            }
            logger.info("Product info is: " + productInfo);
        }
        return productInfo;
    }

    public String getCartDescriptionOnCheckoutForm(int row) {
        String xpathCartDesc = locators.getProperty("productDescription").replace("@@row@@", Integer.toString(row));
        String cartDesc = getTextValue(getElementByXpath(xpathCartDesc), null).replace("\r\n", " ").replace("\n", ";");
        logger.info("Cart Description is: " + cartDesc);
        return cartDesc;
    }

    public String getCartQuantityOnCheckoutForm(int row) {
        String xpathCartQuantity = locators.getProperty("productQuantity").replace("@@row@@", Integer.toString(row));
        String cartQuantity = getTextValue(getElementByXpath(xpathCartQuantity), null);
        logger.info("Cart Quantity is: " + cartQuantity);
        return cartQuantity;
    }

    public String getCurrentStep(){
        return getTextValue(getElementByXpath(locators.getProperty("currentHeader")),checkGreenColor);
    }

    public String getPriceSummary(String labelName){
        String xpathPriceSummary = locators.getProperty("cartPriceSummary").replace("@@label@@",labelName);
        return getTextValue(getElementByXpath(xpathPriceSummary),null);
    }

    public void removeItemFromCart(String productName){
        String xpathDeleteBtn = locators.getProperty("deleteBtnOnItem").replace("@@productName@@",productName);
        clickWebElement(xpathDeleteBtn);
        waitForElementToBeInvisible(xpathDeleteBtn);
    }

    public boolean isCartIsEmpty(){
        return isElementDisplayed(locators.getProperty("emptyCartMsg"));
    }


}
