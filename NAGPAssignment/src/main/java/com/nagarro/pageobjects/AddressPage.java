package com.nagarro.pageobjects;

import com.nagarro.base.BasePageObject;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebElement;

import java.util.List;
import java.util.NoSuchElementException;

public class AddressPage extends BasePageObject {
    private Logger logger = LogManager.getLogger(getClass());
    private By addressPage = By.id("addresses");
    private By addressDetails = By.xpath("//*[@id='addresses']//div[@class='addresses']//ul/li[not(@class)]");


    /**
     * Helps to check if Address Page is open or not
     * @return true if Address Page is open else false
     */
    public boolean isOpen() {
        try {
            waitForElementToBeDisplayed(addressPage);
        } catch (NoSuchElementException noSuchElementException) {
            logger.error(noSuchElementException);
        } catch (TimeoutException timeoutException) {
            logger.error(timeoutException);
        }
        return isElementDisplayed(addressPage);
    }

    public String getAddressDetails() {
        List<WebElement> listAddressDetails = getElements(addressDetails);
        String addressDetail = "";

        for (WebElement elAddressDetail : listAddressDetails) {
            if (!elAddressDetail.getText().isEmpty()){
                if (addressDetail.isEmpty())
                    addressDetail = elAddressDetail.getText();
                else
                    addressDetail = addressDetail  + "; "+ elAddressDetail.getText();
            }
        }
        return addressDetail;
    }


}
