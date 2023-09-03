package ru.inno.internet;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.RepeatedTest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.nio.file.Path;
import java.util.concurrent.TimeoutException;

import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.MILLIS;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SeleniumJupiter.class)
public class WaitTest {

    @Test
    public void sleepTest(ChromeDriver driver) throws InterruptedException {
        String textToBe = "Data loaded with AJAX get request.";
        driver.get("http://uitestingplayground.com/ajax");

        By buttonLocator = By.cssSelector("#ajaxButton");
        By textLocator = By.cssSelector("#content p");
        driver.findElement(buttonLocator).click();

        Thread.sleep(20 * 1000);

        String textAsIs = driver.findElement(textLocator).getText();
        assertEquals(textToBe, textAsIs);
    }

    @Test
    public void implicityWait(ChromeDriver driver) { // неявное ожидание
        driver.manage().timeouts().implicitlyWait(of(4, SECONDS));

        String textToBe = "Data loaded with AJAX get request.";
        driver.get("http://uitestingplayground.com/ajax");

        By buttonLocator = By.cssSelector("#ajaxButton");
        driver.findElement(buttonLocator).click();

        By textLocator = By.cssSelector("#content p");

        String textAsIs = driver.findElement(textLocator).getText();

        assertEquals(textToBe, textAsIs);
    }

    @Test
    public void progressbarTest(ChromeDriver driver) throws InterruptedException, TimeoutException {
        driver.manage().timeouts().implicitlyWait(of(20, SECONDS));
        driver.get("http://uitestingplayground.com/progressbar");

        By startButton = By.cssSelector("#startButton");
        By stopButton = By.cssSelector("#stopButton");
        By progressBar = By.cssSelector("#progressBar");
        By result = By.cssSelector("#result");

        driver.findElement(startButton).click();

        awaitElementTextShouldBe(driver, progressBar, 40 * 1000, "75%");

        driver.findElement(stopButton).click();
        String resultText = driver.findElement(result).getText();

        System.out.println(resultText);
        assertTrue(resultText.startsWith("Result: 0,"));
    }

    private static boolean awaitElementTextShouldBe(WebDriver driver, By locator, int timeToWait, String textToBe) throws TimeoutException {
        long start = System.currentTimeMillis();
        while (System.currentTimeMillis() - start <= timeToWait) {
            String elementText = driver.findElement(locator).getText();
            if (elementText.equals(textToBe)) {
                return true;
            }
        }
        throw new TimeoutException("не дождались");
    }

    @Test
    @RepeatedTest(20)
    public void explicityWaitTest(ChromeDriver driver) {
        driver.get("http://uitestingplayground.com/progressbar");

        By startButton = By.cssSelector("#startButton");
        By stopButton = By.cssSelector("#stopButton");
        By progressBar = By.cssSelector("#progressBar");
        By result = By.cssSelector("#result");

        driver.findElement(startButton).click();

        WebDriverWait wait = new WebDriverWait(driver, of(40, SECONDS));
        wait.withMessage("Так и не дождались 75 процентов")
                .pollingEvery(of(1, MILLIS))
                .until(ExpectedConditions.textToBe(progressBar, "75%"));

        driver.findElement(stopButton).click();
        String resultText = driver.findElement(result).getText();

        System.out.println(resultText);
        assertTrue(resultText.startsWith("Result: 0,"));
    }

    @Test
    public void customECTest(ChromeDriver driver) {
        String colorToBe = "rgba(163, 217, 135, 1)";

        String filepath = Path.of("").toAbsolutePath() + "/src/test/resources/trafficlight.html";
        driver.get("file://" + filepath);
        By light = By.cssSelector("#light");

        WebDriverWait wait = new WebDriverWait(driver, of(1, SECONDS));
//        wait.until(ExpectedConditions.attributeContains(light, "style", colorToBe));
        wait.withMessage("Кнопка не стала зеленой")
                .until(EC.cssPropertyToBe(light, "background-color", colorToBe)).click();

        String text = driver.findElement(light).getText();

        assertEquals("green", text);
    }

    @Test
    public void conflictTest(ChromeDriver driver) { // 50 seconds ?
        driver.manage().timeouts().implicitlyWait(of(25, SECONDS));

        String textToBe = "Data loaded with AJAX get request.";
        driver.get("http://uitestingplayground.com/ajax");

        By buttonLocator = By.cssSelector("#ajaxButton");
        By textLocator = By.cssSelector("#content1 p");
        driver.findElement(buttonLocator).click();

        WebDriverWait wait = new WebDriverWait(driver, of(30, SECONDS));
        boolean textEquals = wait.until(ExpectedConditions.textToBe(textLocator, textToBe));

        assertTrue(textEquals);
    }
}
