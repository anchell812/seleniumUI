package ru.inno.internet.pages;

import org.openqa.selenium.By;

public class MainPage {

    public By searchField = By.xpath("//input[@id='search-field']");
    public By sorting = By.xpath("//span[contains(@class, 'orting-value')]/span[contains(@class, 'navisort-item')]");

    public By highRating = By.xpath("//a[@data-event-content='высокий рейтинг']");

    public By cart = By.xpath("//span[contains(@class, 'basket-in-cart')]");
}
