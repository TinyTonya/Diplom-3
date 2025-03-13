package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AuthorizationPage {
    private final WebDriver driver;

    //Локатор кнопки 'Войти в аккаунт'
    private final By logInToAccountButton = By.xpath("//button[text()='Войти в аккаунт']");

    // Локатор полей ввода
    private final By inputFields = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class=" +
            "'input__container']//input");
    // Локатор кнопки Войти
    private final By logInButton = By.xpath(".//form[starts-with(@class, 'Auth_form')]/button");

    // Локатор кнопки  'Оформить заказ'
    private final By makeOrderButton = By.xpath("//div[@class='BurgerConstructor_basket__container__2fUl3 mt-10']//button[text()='Оформить заказ']");

    public AuthorizationPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Клик по кнопке 'Войти в аккаунт'")
    public void clickLogInToAccountButton() {
        driver.findElement(logInToAccountButton).click();
    }

    @Step("Ввод Email")
    public void enterEmail(String email) {
        driver.findElements(inputFields).get(0).sendKeys(email);
    }

    @Step("Ввод пароля")
    public void enterPassword(String password) {
        driver.findElements(inputFields).get(1).sendKeys(password);
    }

    @Step("Клик по кнопке 'Войти'")
    public void clickAuthorizationButton() {
        driver.findElement(logInButton).click();
    }

    public By getMakeOrderButton() {
        return makeOrderButton;
    }
}
