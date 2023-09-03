package ru.inno.internet;

import io.github.bonigarcia.seljup.SeleniumJupiter;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.inno.internet.constants.Constants;
import ru.inno.internet.pages.MainPage;
import ru.inno.internet.pages.SearchResultPage;

import java.util.*;

import static java.time.Duration.of;
import static java.time.temporal.ChronoUnit.SECONDS;
import static org.junit.jupiter.api.Assertions.assertEquals;


@ExtendWith(SeleniumJupiter.class)
public class Labirint {

    @Test
    public void itemsToBucketTest(ChromeDriver driver) throws InterruptedException {
        Constants constants = new Constants();
        driver.get(constants.BASE_URL);
        Cookie cookie = new Cookie(constants.COOKIE, constants.COOKIE_VALUE);
        driver.manage().addCookie(cookie);
        driver.get(constants.BASE_URL);
        MainPage mainPage = new MainPage();
        driver.findElement(mainPage.searchField).sendKeys(constants.SEARCH_QUERY, Keys.ENTER);
        String sortingType = driver.findElement(mainPage.sorting).getText();
        if(sortingType != constants.ORDER_BY_HIGH_RATING) {
            driver.findElement(mainPage.sorting).click();
            driver.findElement(mainPage.highRating).click();
        }
        SearchResultPage searchResultPage = new SearchResultPage();

        WebDriverWait wait = new WebDriverWait(driver, of(1, SECONDS));
        wait.withMessage("Не появились кнопки *В корзину*")
                .until(ExpectedConditions.stalenessOf(driver.findElement(searchResultPage.addToCartBtn)));

        List<WebElement> addToCartBtn = driver.findElements(searchResultPage.addToCartBtn);

        for (WebElement e : addToCartBtn) {
            e.click();
        }

        Thread.sleep(5000);

        int itemCounter = Integer.parseInt(driver.findElement(mainPage.cart).getText());

        assertEquals(addToCartBtn.size(), itemCounter);
    }
}
