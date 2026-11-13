package demo;

import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.logging.Level;

import demo.wrappers.Wrappers;

public class TestCases {
    ChromeDriver driver;

    private static final String FORM_URL = "https://docs.google.com/forms/d/e/1FAIpQLSep9LTMntH5YqIXa5nkiPKSs283kdwitBBhXWyZdAS-e4CxBQ/viewform";

    private By question(String title, String inner) {
        return By.xpath("//div[@role='listitem'][.//span[contains(text(),'" + title + "')]]" + inner);
    }

    @Test
    public void testCase01() {

        Wrappers.navigateTo(driver, FORM_URL);

        Wrappers.typeText(driver, question("Name", "//*[(self::input or self::textarea) and not(@type='hidden') and not(@aria-hidden='true')]"), "Crio Learner");

        long epoch = Instant.now().getEpochSecond();
        Wrappers.typeText(driver, question("Why are you practicing Automation", "//*[(self::input or self::textarea) and not(@type='hidden') and not(@aria-hidden='true')]"),
                "I want to be the best QA Engineer! " + epoch);

        Wrappers.clickElement(driver, By.xpath("//div[@role='radio' and @data-value='3 - 5']"));

        for (String skill : new String[] {"Java", "Selenium", "TestNG"}) {
            Wrappers.clickElement(driver, By.xpath("//div[@role='checkbox' and @aria-label='" + skill + "']"));
        }

        Wrappers.clickElement(driver, question("How should you be addressed", "//div[@role='listbox']"));
        Wrappers.clickElement(driver, By.xpath("//div[@role='option']//span[text()='Mr']"));


        String date = LocalDate.now().minusDays(7).format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        Wrappers.typeText(driver, question("What was the date 7 days ago", "//input[@type='date']"), date);

        Wrappers.typeText(driver, question("What is the time right now", "//input[@aria-label='Hour']"), "07");
        Wrappers.typeText(driver, question("What is the time right now", "//input[@aria-label='Minute']"), "30");

        Wrappers.clickElement(driver, By.xpath("//span[text()='Submit']"));

        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//div[contains(text(),'Thanks for your response')]")));
        String message = Wrappers.getText(driver, By.xpath("//div[contains(text(),'Thanks for your response')]"));
    }

        @BeforeTest
    public void startBrowser() {
        System.setProperty("java.util.logging.config.file", "logging.properties");

        ChromeOptions options = new ChromeOptions();
        LoggingPreferences logs = new LoggingPreferences();

        logs.enable(LogType.BROWSER, Level.ALL);
        logs.enable(LogType.DRIVER, Level.ALL);
        options.setCapability("goog:loggingPrefs", logs);

        options.addArguments("--start-maximized");
        options.addArguments("--no-sandbox");
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--disable-gpu");
        options.addArguments("--remote-allow-origins=*");

        System.setProperty(ChromeDriverService.CHROME_DRIVER_LOG_PROPERTY, "build/chromedriver.log");

        driver = new ChromeDriver(options);
    }


    @AfterTest
    public void endTest() {
        if (driver != null) {
            driver.close();
            driver.quit();
        }
    }
}
