package com.nagarro.tests;

import com.nagarro.pageobjects.*;
import com.nagarro.testclient.TestClient;
import com.nagarro.utils.Helper;
import com.nagarro.utils.TestDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.Map;

public class AddToCartTest extends TestClient {

    private HomePage homePage;
    private Logger logger;
    private Helper helper;
    private LoginPage loginPage;
    private MyAccountPage myAccountPage;
    private ProductPage productPage;
    private ShoppingCartPage shoppingCartPage;

    private Map<String, String> itemDetailsInCartDialog;
    private Map<String, String> itemDetailsInCartTestData;
    private Map<String, String> itemInfoInShoppingCartBlock;
    private Map<String, String> itemPriceInShoppingCartBlock;
    private Map<String, String> itemDetailsInCheckoutForm;
    private Map<String, String> cartPriceSummaryTestData;
    private Map<String, String> cartPriceSummaryDetails;
    private String itemName;
    private String totalAmountToBePaid;
    private boolean isItemAddToCart = false;

    public AddToCartTest() {
        logger = LogManager.getLogger(getClass());
    }

    @BeforeClass(alwaysRun = true)
    public void setup() {
        homePage = new HomePage();
        helper = new Helper();
        myAccountPage = new MyAccountPage();

        TestDataReader.init();

        // Setting up Test data for validations
        itemDetailsInCartDialog = TestDataReader.getDataMap("ItemDetailsInCartDialog");
        itemName = itemDetailsInCartDialog.get("title");
        itemDetailsInCartTestData = TestDataReader.getDataMap("ItemDetailsInCartTestData");
        itemInfoInShoppingCartBlock = TestDataReader.getDataMap("ItemInfoInShoppingCartBlock");
        itemPriceInShoppingCartBlock = TestDataReader.getDataMap("ItemPriceInShoppingCartBlock");
        itemDetailsInCheckoutForm = TestDataReader.getDataMap("ItemDetailsInCheckoutForm");
        cartPriceSummaryTestData = TestDataReader.getDataMap("CartPriceSummaryTestData");
        totalAmountToBePaid = cartPriceSummaryTestData.get("total_amount_to_be_paid");
        cartPriceSummaryDetails = TestDataReader.getDataMap("CartPriceSummaryDetails");
    }

    @Test(priority = 11, description = "Add an Item to the Cart and verify Product details in Cart Popup",groups = {"smoke","regression"})
    public void addItemToCart() {
        // Click on Sign In Button
        loginPage = homePage.clickOnSignInBtn();

        // Login to application with Valid user
        myAccountPage = loginPage.loginToApplication();

        // Select 'WOMEN' from Top Menu
        productPage = myAccountPage.getTopMenu().clickWomenMenu();

        // Add item to cart
        shoppingCartPage = productPage.addItemToCart(itemName);

        // Verify: Detail for Product in Cart Popup
        itemDetailsInCartDialog.forEach((key, value) -> {
            logger.info("Validating: Item detail for '" + key + "' is: " + value);
            String itemInfo = key;
            Assert.assertEquals(shoppingCartPage.getProductInfo(itemInfo), value, "Item details are not matching");
        });
        Assert.assertEquals(shoppingCartPage.getSuccessMsg(), itemDetailsInCartTestData.get("successMsg"));
        isItemAddToCart = true;

        // CLick on Continue Shopping button and Validate Count on Cart should display as 1
        shoppingCartPage.clickContinueShopping();
        Assert.assertEquals(shoppingCartPage.getCountOfProductsInCart(), itemDetailsInCartTestData.get("productCountInCart"));
    }

    @Test(priority = 12, description = "Verify details of items is displaying correct on Shopping Cart Block display at the top",groups = {"regression"})
    public void verifyItemDetailsOnCartBlock() {
        if (isItemAddToCart) {
            // Expand Shopping Cart Block displaying on the top
            shoppingCartPage.expandShoppingCartBlock();

            // Verifying: Product Details should display correct in Shopping Cart Block
            Map<String, String> actualItemInfo = shoppingCartPage.getItemInfoInShoppingCartBlock();
            itemInfoInShoppingCartBlock.forEach((key, value) -> {
                logger.info("[Shopping Cart Block]: Validating Item info for '" + key + "' is: " + value);
                if (key.contains("product-name"))
                    Assert.assertTrue(actualItemInfo.get(key).contains(value));
                else
                    Assert.assertEquals(actualItemInfo.get(key), value, "[Shopping Cart Block]: [Actual value: " + actualItemInfo.get(key) + "] is not matching with [Expected value: " + value + "]");
            });

            // Verifying: Product Price should display correct in Shopping Cart Block
            itemPriceInShoppingCartBlock.forEach((key, value) -> {
                logger.info("[Shopping Cart Block]: Validating Item price for '" + key + "' is: " + value);
                Assert.assertEquals(shoppingCartPage.getCartPriceInShoppingCart(key), value, "[Shopping Cart Block]: [Actual value: " + shoppingCartPage.getCartPriceInShoppingCart(key) + "] is not matching with [Expected value: " + value + "]");
            });
        } else Assert.fail("No item is added to cart. Please execute method");
    }

    @Test(priority = 13, description = "Verifying item details on Checkout form should display correct",groups = {"regression"})
    public void verifyItemDetailsOnCheckoutForm() {
        if (isItemAddToCart) {

            // Expand Shopping Cart Block displaying on the top
            shoppingCartPage.expandShoppingCartBlock();

            // Click on 'Check out' button on Shopping Cart Block to navigate to Checkout Page
            CheckoutPage checkoutPage = shoppingCartPage.clickOnCheckout();

            // Verifying: User is at Summary Step
            Assert.assertTrue(checkoutPage.getCurrentStep().contains("Summary"), "Actual step '" + checkoutPage.getCurrentStep() + "' is not matching with Expected Step 'Summary'");

            // Verifying: Product details on Checkout Form should display correct
            Map<String, String> actualItemDetailsInCheckoutForm = checkoutPage.getProductInfoOnCheckoutForm();
            itemDetailsInCheckoutForm.forEach((key, value) -> {
                logger.info("[Checkout Form]: Validating Item Details for '" + key + "' is: " + value);
                Assert.assertEquals(actualItemDetailsInCheckoutForm.get(key), value);
            });

            // Verifying: Cart Price SUmmary on Checkout Form should display correct
            cartPriceSummaryDetails.forEach((key, value) -> {
                String label = key.replace("_", " ");
                logger.info("[Checkout Form]: Validating Cart Price Summary for '" + label + "' is: " + value);
                Assert.assertEquals(checkoutPage.getPriceSummary(label), value);
            });
        } else Assert.fail("No item is added to cart. Please execute method");
    }


    @Test(priority = 14, description = "Remove Item from the cart",groups = {"smoke","regression"})
    public void verifyRemoveItemDetailsFromCheckoutForm() {
        if (isItemAddToCart) {
            int productCount = Integer.parseInt(shoppingCartPage.getCountOfProductsInCart());
            if (productCount >= 1) {

                // Expand Shopping Cart Block displaying on the top
                shoppingCartPage.expandShoppingCartBlock();

                // Click on 'Check out' button on Shopping Cart Block to navigate to Checkout Page
                CheckoutPage checkoutPage = shoppingCartPage.clickOnCheckout();

                // Verifying: Item present on Checkout form
                Assert.assertTrue(checkoutPage.getCurrentStep().contains("Summary"), "Actual step '" + checkoutPage.getCurrentStep() + "' is not matching with Expected Step 'Summary'");
                Map<String, String> actualItemDetailsInCheckoutForm = checkoutPage.getProductInfoOnCheckoutForm();
                itemDetailsInCheckoutForm.forEach((key, value) -> {
                    logger.info("Validating: Item Details on Checkout Form for '" + key + "' is: " + value);
                    Assert.assertEquals(actualItemDetailsInCheckoutForm.get(key), value);
                });

                // Remove item from the cart
                checkoutPage.removeItemFromCart(itemName);

                // Verifying: Cart should get empty on removing the product from the cart
                Assert.assertTrue(checkoutPage.isCartIsEmpty());
            } else Assert.fail("No Product is in the cart");
        } else Assert.fail("No item is added to cart. Please execute method");
    }
}
