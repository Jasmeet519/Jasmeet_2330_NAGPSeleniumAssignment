package com.nagarro.base;

import com.nagarro.utils.Helper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.interactions.MoveTargetOutOfBoundsException;
import org.openqa.selenium.support.ui.*;

import java.time.Duration;
import java.util.List;
import java.util.Properties;
import java.util.function.Function;
import java.util.function.Predicate;

public class BasePageObject extends Driver {

    private WebDriver driver = getDriverInstance();
    private Logger logger = LogManager.getLogger(getClass());
    private Helper helper = new Helper();


    /**
     * Opens url set in config.properties file
     */
    public void openApplication() {
        String url = configFile.getProperty("url");
        logger.info("Opening url: " + url);
        driver.get(url);
    }

    /**
     * Method used to find element in dom based on locator
     *
     * @param by is locator for WebElement
     * @return WebElement based on 'by' locator
     */
    public WebElement getElement(By by) {
        waitForElementToBeDisplayed(by);
        return driver.findElement(by);
    }

    /**
     * Method used to find element in dom based on xpath
     *
     * @param xpath is the locator to find web element in dom
     * @return WebElement based on xpath
     */
    public WebElement getElementByXpath(String xpath) {
        return driver.findElement(By.xpath(xpath));
    }

    /**
     * Method to get list of WebElement by locator
     * @param by is the locator for the WebElement
     * @return list of WebElement in dom having 'by' locator
     */
    public List<WebElement> getElements(By by) {
        return driver.findElements(by);
    }

    /**
     * Method to get list of WebElement by xpath locator
     *
     * @param xpath is the locator to find web element in dom
     * @return list of WebElements based on xpath
     */
    public List<WebElement> getElements(String xpath) {
        return driver.findElements(By.xpath(xpath));
    }

    /**
     * Method helps to click on Webelement
     *
     * @param by is locator for the WebElement
     */
    public void clickWebElement(By by) {
        waitForElementToBeClickable(by);
        WebElement element = getElement(by);
        try {
            element.click();
        } catch (ElementClickInterceptedException e) {
            executeJavascript("arguments[0].click();", element);
        }
    }

    /**
     * Method helps to click on Webelement
     *
     * @param xpathLocator is locator for Webelement
     */
    public void clickWebElement(String xpathLocator) {

        WebElement webElement = getElementByXpath(xpathLocator);
        waitForElementToBeClickable(webElement);
        try {
            webElement.click();
        } catch (ElementClickInterceptedException e) {
            executeJavascript("arguments[0].click();", webElement);
        }
    }


    /**
     * Method helps to check if element is displayed or not
     *
     * @param by is the locator of the Webelement
     * @return true if element is displayed else fase
     */
    public boolean isElementDisplayed(By by) {
        List<WebElement> list = getElements(by);
        if (list.size() > 0) {
            return true;
        } else return false;
    }

    /**
     * Method helps to check if element is displayed or not
     *
     * @param xpath is the locator for the element
     * @return true if element is displayed else fase
     */
    public boolean isElementDisplayed(String xpath) {
        List<WebElement> list = getElements(xpath);
        if (list.size() > 0) {
            return true;
        } else return false;
    }

    /**
     * Methods helps to initialize locator file
     *
     * @param fileName is the name of file where locators are present
     * @return Properties object of fileName
     */
    public Properties getLocators(String fileName) {
        Properties locators = helper.readLocatorFile(fileName);
        return locators;
    }

    /**
     * Helps to execute Javascripts
     * @param script which we want to execute
     * @param element is the Webelement
     */
    public void executeJavascript(String script, WebElement element) {
        JavascriptExecutor executor = (JavascriptExecutor) getDriverInstance();
        executor.executeScript(script, element);
    }

    /**
     * Helps to enter value in text field
     * @param element is the WebElement
     * @param value to be entered in text field
     */
    public void enterValueInTextField(WebElement element, String value) {
        logger.info("Entering value in text field: " + value);
        element.clear();
        if (!element.getAttribute("value").isEmpty()) {
            logger.info("Retrying: Clearing value in text field: ");
            element.clear();
        }
        element.sendKeys(value);
        if (!element.getAttribute("value").equals(value)) {
            logger.info("Retrying: Entering value in text field: " + value);
            element.sendKeys(value);
        }
    }

    /**
     * Helps to wait based on waitCondition
     *
     * @param waitCondition
     */
    public void initializeFluentWait(Function<WebDriver, Boolean> waitCondition) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Long.parseLong(configFile.getProperty("minTimeout"))))
                .pollingEvery(Duration.ofSeconds(Long.parseLong(configFile.getProperty("pollingTime"))))
                .ignoring(NoSuchElementException.class);
        fluentWait.until(waitCondition);

    }

    /**
     * Helps to wait based on waitCondition
     *
     * @param condition
     */
    public void initializeFluentWait(ExpectedCondition<WebElement> condition) {
        Wait<WebDriver> fluentWait = new FluentWait<>(driver)
                .withTimeout(Duration.ofSeconds(Long.parseLong(configFile.getProperty("minTimeout"))))
                .pollingEvery(Duration.ofSeconds(Long.parseLong(configFile.getProperty("pollingTime"))))
                .ignoring(NoSuchElementException.class);
        fluentWait.until(condition);
    }

    /**
     * Helps to Wait for WebElement to be displayed
     *
     * @param by is the locator of WebElement
     */
    public void waitForElementToBeDisplayed(By by) {
        Function<WebDriver, Boolean> isDisplayed = webDriver -> webDriver.findElement(by).isDisplayed();
        initializeFluentWait(isDisplayed);
    }

    /**
     * Helps to Wait for WebElement to be displayed
     *
     * @param xpath is the locator of WebElement
     */
    public void waitForElementToBeDisplayed(String xpath) {
        Function<WebDriver, Boolean> isDisplayed = webdriver -> webdriver.findElement(By.xpath(xpath)).isDisplayed();
        initializeFluentWait(isDisplayed);
    }

    /**
     * Helps to Wait until WebElement is clickable
     *
     * @param by is the locator for WebElement
     */
    public void waitForElementToBeClickable(By by) {
        initializeFluentWait(ExpectedConditions.elementToBeClickable(by));
    }

    /**
     * Helps to Wait until WebElement is clickable
     *
     * @param webElement
     */
    public void waitForElementToBeClickable(WebElement webElement) {
        initializeFluentWait(ExpectedConditions.elementToBeClickable(webElement));
    }

    /**
     * Helps to Wait until WebElement is invisible
     *
     * @param xpath is the locator
     */
    public void waitForElementToBeInvisible(String xpath) {
        initializeFluentWait(ExpectedConditions.invisibilityOf(getElementByXpath(xpath)));
    }

    /**
     * Helps to move to element using Action Class
     *
     * @param element
     */
    public void moveToElement(WebElement element) {
        Actions actions = new Actions(getDriverInstance());
        try {
            actions.moveToElement(element).build().perform();
        } catch (MoveTargetOutOfBoundsException moveTargetOutOfBoundsException) {
            logger.error(moveTargetOutOfBoundsException);
            executeJavascript("arguments[0].scrollIntoView();", element);
            actions.moveToElement(element).build().perform();
        }

    }

    /**
     * Helps to get text value for any WebElement
     *
     * @param element
     * @param condition is boolean condition for text to be fetched
     * @return text value on WebElement
     */
    public String getTextValue(WebElement element, Predicate<WebElement> condition) {
        String textValue = element.getText();
        textValue = textValue.isEmpty() ? element.getAttribute("value") : textValue;

        if (condition != null) {
            return condition.test(element) ? textValue : null;
        }
        return textValue;
    }

}
