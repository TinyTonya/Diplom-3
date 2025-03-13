package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PersonalAccountPage {
    private WebDriver driver;

    // Локатор кнопки "Выход"
    private final By logoutButton = By.xpath("//li[@class='Account_listItem__35dAP']//button[text()='Выход']");

    // Локатор текста "В этом разделе вы можете изменить свои персональные данные"
    private final By accountDescriptionText = By.xpath("//p[@class='Account_text__fZAIn text text_type_main-default']");

    public PersonalAccountPage(WebDriver driver) {
        this.driver = driver;
    }

    // Геттер для кнопки "Выход"
    public By getLogoutButton() {
        return logoutButton;
    }

    // Геттер для текста "В этом разделе вы можете изменить свои персональные данные"
    public By getAccountDescriptionText() {
        return accountDescriptionText;
    }

    // Метод для клика по кнопке "Выход"
    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }

    // Метод для получения текста "В этом разделе вы можете изменить свои персональные данные"
    public String getAccountDescriptionTextContent() {
        return driver.findElement(accountDescriptionText).getText();
    }
}