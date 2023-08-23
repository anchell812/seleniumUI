package ru.inno.internet;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.By;
import org.openqa.selenium.firefox.FirefoxDriver;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

// https://bonigarcia.dev/webdrivermanager
@ExtendWith(SeleniumJupiter.class)
public class FormAuthTest {


    @Test
    public void successLogin(FirefoxDriver browser) {
        //1. зайти на страницу http://the-internet.herokuapp.com/login
        browser.get("http://the-internet.herokuapp.com/login");

        //2. ввести логин tomsmith
        browser.findElement(By.cssSelector("#username")).sendKeys("tomsmith");

        //3. ввести пароль SuperSecretPassword!
        browser.findElement(By.cssSelector("#password")).sendKeys("SuperSecretPassword!");

        //4. нажать логин
        browser.findElement(By.cssSelector("button[type=submit]")).click();

        //5. проверить, что отображается зеленая плашка
        boolean contains = browser.findElement(By.cssSelector("#flash")).getAttribute("class").contains("success");

        //6. проверить, что есть кнопка логаут
        String text = browser.findElement(By.cssSelector(".icon-signout")).getText();

        browser.quit();

        assertTrue(contains);
        assertEquals("Logout", text);
    }
}
