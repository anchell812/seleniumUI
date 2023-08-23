package ru.inno.internet;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class WebDriverInterfaceTest {

    private WebDriver driver;

    @BeforeEach
    public void openBrowser() {
        driver = new FirefoxDriver();
        driver.manage().window().setPosition(new Point(120, -1000));
    }

    @AfterEach
    public void closeBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    public void failedTest() {
        driver = null; // якобы все развалилось
    }

    @Test
    public void urlTest() {
        String urlToVisit = "https://the-internet.herokuapp.com/";
        driver.get(urlToVisit);

        String currentUrl = driver.getCurrentUrl();
        assertEquals(urlToVisit, currentUrl);
    }

    @Test
    public void titleTest() {
        String urlToVisit = "https://the-internet.herokuapp.com";
        driver.get(urlToVisit);
        String title = driver.getTitle();
        assertEquals("The Internet", title);
    }

    @Test
    public void pageSourceTest() {
        String urlToVisit = "https://the-internet.herokuapp.com";
        driver.get(urlToVisit);
        String source = driver.getPageSource();

        System.out.println(source);
    }

    @Test
    public void manageWindowTest() {
        String urlToVisit = "https://the-internet.herokuapp.com";
        driver.get(urlToVisit);

        driver.manage().window().minimize();
        driver.manage().window().maximize();
        driver.manage().window().fullscreen();
    }

    @Test
    public void manageSizeTest() {
        String urlToVisit = "https://the-internet.herokuapp.com";
        driver.get(urlToVisit);

        Dimension dimensionBefore = driver.manage().window().getSize();
        System.out.println(dimensionBefore);

        driver.manage().window().setSize(new Dimension(500, 500));

        Dimension dimensionAfter = driver.manage().window().getSize();
        System.out.println(dimensionAfter);
        assertEquals(new Dimension(500, 500), dimensionAfter);
    }

    @Test
    public void managePositionTest() {
        String urlToVisit = "https://the-internet.herokuapp.com";
        driver.get(urlToVisit);

        Point positionBefore = driver.manage().window().getPosition();
        System.out.println(positionBefore);

        driver.manage().window().setPosition(new Point(100, -1000));

        Point positionAfter = driver.manage().window().getPosition();
        System.out.println(positionAfter);
        assertEquals(new Point(100, -1000), positionAfter);
    }

    @Test
    public void implicitlyWaitFailedTest() {
        String url = "https://the-internet.herokuapp.com/dynamic_loading/2";
        driver.get(url);
        driver.findElement(By.cssSelector("#start button")).click();

        //NoSuchElementException
        String text = driver.findElement(By.cssSelector("#finish")).getText();

        assertEquals("Hello World!", text);
    }

    @Test
    public void implicitlyWaitSuccessTest() {
        driver.manage().timeouts().implicitlyWait(Duration.of(10, ChronoUnit.SECONDS));

        String url = "https://the-internet.herokuapp.com/dynamic_loading/2";
        driver.get(url);
        driver.findElement(By.cssSelector("#start button")).click();

        //NoSuchElementException
        String text = driver.findElement(By.cssSelector("#finish")).getText();

        assertEquals("Hello World!", text);
    }

    @Test
    public void manageCookies() {
        driver.get("https://www.labirint.ru");
//        Set<Cookie> cookies = driver.manage().getCookies(); //все куки

        Cookie cookie = new Cookie("cookie_policy", "1");
        driver.manage().addCookie(cookie);
        driver.get("https://www.labirint.ru");
    }

    @Test
    public void githubGetRepos() {
        //user_session
        driver.get("https://github.com");
        Cookie cookie = new Cookie("user_session", "token...");
        driver.manage().addCookie(cookie);
        driver.navigate().refresh();
    }

    @Test
    public void navigateTest() {
        String page1 = "https://the-internet.herokuapp.com/";
        String page2 = "https://the-internet.herokuapp.com/abtest";
        driver.get(page1);
        driver.get(page2);

        driver.navigate().back();
        assertEquals(page1, driver.getCurrentUrl());

        driver.navigate().forward();
        assertEquals(page2, driver.getCurrentUrl());

        // driver.navigate().to("https://...); === driver.get("https://...")

        driver.navigate().refresh();
        assertEquals(page2, driver.getCurrentUrl());
    }

    @Test
    public void windowHandlesTest() {
        driver.get("https://the-internet.herokuapp.com/windows");
        driver.findElement(By.linkText("Click Here")).click();

        String windowHandle = driver.getWindowHandle(); // текущая вкладка
        Set<String> windowHandles = driver.getWindowHandles(); // все вкладки

        String[] tabs = new String[windowHandles.size()];
        windowHandles.toArray(tabs);
        String firstTab = tabs[0];
        String secondTab = tabs[1];

        driver.switchTo().window(secondTab);

        assertEquals(firstTab, windowHandle);
        assertEquals("New Window", driver.getTitle());
    }

    @Test
    public void closeTest() {
        driver.get("https://the-internet.herokuapp.com/windows");
        driver.findElement(By.linkText("Click Here")).click();

        Set<String> windowHandles = driver.getWindowHandles(); // все вкладки
        String[] tabs = new String[windowHandles.size()];
        windowHandles.toArray(tabs);

        String firstTab = tabs[0];
        String secondTab = tabs[1];

        driver.switchTo().window(secondTab);

        driver.close();

        driver.switchTo().window(firstTab);
        String currentWH = driver.getWindowHandle();
        assertEquals(currentWH, firstTab);
    }

    @Test
    public void framesFailedTest() {
        driver.get("https://the-internet.herokuapp.com/iframe");
        String content = driver.findElement(By.cssSelector("#tinymce")).getText();

        assertEquals("Your content goes here.", content);
    }

    @Test
    public void framesSuccessTest() {
        driver.get("https://the-internet.herokuapp.com/iframe");

        driver.switchTo().frame("mce_0_ifr");

        String content = driver.findElement(By.cssSelector("#tinymce")).getText();
        assertEquals("Your content goes here.", content);

        driver.switchTo().defaultContent();
        String h3 = driver.findElement(By.cssSelector("h3")).getText();
        assertEquals("An iFrame containing the TinyMCE WYSIWYG Editor", h3);
    }



    // TODO: how to manage logs


}
