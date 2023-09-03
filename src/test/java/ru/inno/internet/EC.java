package ru.inno.internet;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;

public class EC {
    public static ExpectedCondition<WebElement> cssPropertyToBe(By locator, String propertyName, String valueToBe) {

        return new ExpectedCondition<>() {
            @Override
            public WebElement apply(WebDriver webDriver) {
                WebElement element = webDriver.findElement(locator);
                String currentValue = element.getCssValue(propertyName);
                System.out.println(propertyName + " = " + currentValue);
                if (currentValue.equals(valueToBe)) {
                    return element;
                }
                return null;
            }
        };
    }
}
