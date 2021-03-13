package com.sivalabs.moviebuffs;

import com.github.javafaker.Faker;
import java.util.List;
import java.util.Random;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;

public class SeleniumTests {
    private static WebDriver driver = null;
    private static Random r = new Random();
    static Faker faker = new Faker();
    private static final String basePath = "http://localhost:8080";

    public static void main(String[] args) {
        System.setProperty(
                "webdriver.chrome.driver", System.getProperty("user.home") + "/mybin/chromedriver");
        driver = new ChromeDriver();
        placeOrders(3);
    }

    private static void placeOrders(int n) {
        login();
        waitFor(2);
        for (int i = 0; i < n; i++) {
            driver.get(basePath);
            driver.manage().window().maximize();
            waitFor(2);
            addItemsToCart(2);
            waitFor(1);

            WebElement linkByText = driver.findElement(By.linkText("Next"));
            scrollTo(linkByText);
            linkByText.click();
            waitFor(1);

            addItemsToCart(1);
            waitFor(1);

            gotoCartAndPlaceOrder();
            waitFor(2);
        }
    }

    private static void gotoHomePage() {
        driver.get(basePath);
        driver.manage().window().maximize();
        waitFor(2);
    }

    private static void gotoCartAndPlaceOrder() {
        WebElement linkByText = driver.findElement(By.partialLinkText("Cart ("));
        linkByText.click();
        waitFor(1);
        enterInputById("customerName", faker.name().fullName());
        enterInputById("customerEmail", faker.regexify("[a-z]{10}") + "@gmail.com");
        enterInputById("deliveryAddress", faker.address().fullAddress());
        enterInputById("creditCardNumber", faker.regexify("[0-9]{16}"));
        enterInputById("cvv", faker.regexify("[0-9]{3}"));

        WebElement orderButton =
                driver.findElement(By.xpath("//button[contains(text(),'Place Order')]"));
        orderButton.click();
    }

    private static void enterInputById(String elementId, String value) {
        WebElement input = driver.findElement(By.id(elementId));
        input.sendKeys(value);
    }

    private static void login() {
        gotoHomePage();
        WebElement linkByText = driver.findElement(By.linkText("Login"));
        linkByText.click();
        waitFor(1);
        WebElement emailInput = driver.findElement(By.id("email"));
        WebElement passwordInput = driver.findElement(By.id("password"));
        WebElement loginButton = driver.findElement(By.xpath("//button[contains(text(),'Login')]"));
        emailInput.sendKeys("admin@gmail.com");
        passwordInput.sendKeys("admin");
        loginButton.click();
    }

    private static void addItemsToCart(int n) {
        List<WebElement> elements =
                driver.findElements(By.xpath("//button[contains(text(),'Add to Cart')]"));

        int low = 0;
        int high = elements.size();

        for (int i = 0; i < n; i++) {
            int result = r.nextInt(high - low) + low;
            WebElement element = elements.get(result);
            scrollTo(element);
            element.click();
            waitFor(1);
        }
    }

    private static void scrollTo(WebElement element) {
        Actions actions = new Actions(driver);
        actions.moveToElement(element);
        actions.perform();
    }

    private static void waitFor(int n) {
        try {
            Thread.sleep(n * 1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
