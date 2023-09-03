package ru.inno.internet;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SeleniumJupiter.class)
public class WebElementInterfaceTest {

    @Test
    public void testFind(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/upload");

        WebElement button = driver.findElement(By.cssSelector("#file-submit"));
        List<WebElement> buttons = driver.findElements(By.cssSelector("#file-submit"));

        assertEquals(1, buttons.size());

    }

    @Test
    public void testFindelements(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/upload");
        String locator = "div";
        WebElement div = driver.findElement(By.cssSelector(locator));
        List<WebElement> divs = driver.findElements(By.cssSelector(locator));

        assertEquals(div, divs.get(0));
    }

    @Test
    public void searchContextTest(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/login");
        WebElement form = driver.findElement(By.cssSelector("form#login[name='login']"));
        form.findElement(By.cssSelector("button")).click();
        //form.submit();
    }

    //NoSuchElementException
    @Test
    public void cssVsXpathTest(ChromeDriver driver) {

        driver.get("https://the-internet.herokuapp.com/login");
        WebElement css = driver.findElement(By.cssSelector(".large-6.small-12.columns"));
        WebElement xPath = driver.findElement(By.xpath("//div[@class='columns large-6']"));

        assertEquals(css, xPath);
    }

    @Test
    public void nosuchTest(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/login");

        List<WebElement> elements = driver.findElements(By.cssSelector("#no-such-element"));
        assertEquals(0, elements.size());

        driver.findElement(By.cssSelector("#no-such-element")); // NoSuchElementException
    }

    @Test
    public void clickTest(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/status_codes");

        driver.findElement(By.cssSelector("li a")).click();
        assertTrue(driver.getCurrentUrl().endsWith("/status_codes/200"));
    }

    @Test
    public void sendKeysTest(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/key_presses");

        WebElement input = driver.findElement(By.cssSelector("#target"));
        input.sendKeys("Test", " ", "12345");

        // TODO: how to send multiple keys
        input.sendKeys(Keys.chord(Keys.COMMAND, "a"), Keys.BACK_SPACE);
    }

    @Test
    public void clearInputTest(ChromeDriver driver){
        driver.manage().timeouts().implicitlyWait(Duration.of(4, ChronoUnit.SECONDS));
        driver.get("https://ya.ru");

        WebElement input = driver.findElement(By.cssSelector("#text"));
        input.sendKeys("Test", " ", "12345");
        input.clear();
        input.sendKeys("inno");
        input.clear();
        input.sendKeys("inno");
    }

    @Test
    public void getPropsTest(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/key_presses");
        WebElement h3 = driver.findElement(By.cssSelector("h3"));
        String text = h3.getText();
        boolean isDisplayed = h3.isDisplayed();
        boolean isEnabled = h3.isEnabled();
        String fontSize = h3.getCssValue("font-size");

        assertEquals("27px", fontSize);
        assertEquals("Key Presses", text);
        assertTrue(isDisplayed);
        assertTrue(isEnabled);

        String href = driver.findElement(By.cssSelector("a")).getAttribute("href");
        assertEquals("https://github.com/tourdedave/the-internet", href);
    }

    @Test
    public void isSelectedTest(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/checkboxes");

        List<WebElement> checkboxes = driver.findElement(By.cssSelector("#checkboxes")).findElements(By.cssSelector("input"));
        boolean cb1Selected = checkboxes.get(0).isSelected();
        boolean cb2Selected = checkboxes.get(1).isSelected();

        assertFalse(cb1Selected);
        assertTrue(cb2Selected);
    }


    @Test
    public void uploadFileTest(ChromeDriver driver){
        driver.get("https://the-internet.herokuapp.com/upload");
        driver.findElement(By.cssSelector("#file-upload")).sendKeys("/Users/eremin/Documents/java-projects/ui-tests/pom.xml");
        driver.findElement(By.cssSelector("#file-submit")).click();

        String text = driver.findElement(By.cssSelector("#uploaded-files")).getText();
        assertEquals("pom.xml", text);
    }

    @Test
    public void screenTest(ChromeDriver driver) throws IOException {
        driver.get("https://the-internet.herokuapp.com/login");
        File formScreenshot = driver.findElement(By.cssSelector("body")).getScreenshotAs(OutputType.FILE);
        Files.move(formScreenshot.toPath(), Path.of("./my_screen1.png"));
    }

}