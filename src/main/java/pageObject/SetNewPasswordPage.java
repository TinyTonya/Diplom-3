package pageObject;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class SetNewPasswordPage {
    private final WebDriver webDriver;

    // Локаторы элементов
    private final By recoverPasswordLink = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Восстановить пароль']");
    private final By recoverPasswordEmailField = By.xpath("//label[contains(@class, 'input__placeholder') and text()='Email']");
    private final By recoverButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and text()='Восстановить']");
    private final By logInLinkOnRecoverPasswordPage = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and text()='Войти']");

    public SetNewPasswordPage(WebDriver driver) {
        webDriver = driver;
    }

    public By getRecoverPasswordLinkElement() {
        return recoverPasswordLink;
    }

    public By getLogInLinkOnRecoverPasswordPageElement() {
        return logInLinkOnRecoverPasswordPage;
    }

}
