package demo.wrappers;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

public class Wrappers {

    public static void navigateTo(ChromeDriver driver, String url) {
        driver.get(url);
    }

    public static void typeText(ChromeDriver driver, By locator, String text) {
        WebElement element = driver.findElement(locator);
        element.click();
        element.sendKeys(text);
    }

    public static void clickElement(ChromeDriver driver, By locator) {
        WebElement element = driver.findElement(locator);
        element.click();
    }

    public static String getText(ChromeDriver driver, By locator) {
        return driver.findElement(locator).getText();
    }
}
