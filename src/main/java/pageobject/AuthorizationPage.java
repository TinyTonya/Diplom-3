package pageobject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class AuthorizationPage {
    private final WebDriver driver;
    private WebDriverWait wait;

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
    this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
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

    @Step("Логин пользователя")
    public void logIn(String email, String password) {
        enterEmail(email);
        enterPassword(password);
        clickAuthorizationButton();
    }

    public By getMakeOrderButton() {
        return makeOrderButton;
    }

    @Step("Ожидание видимости кнопки Оформить заказ")
    public WebElement waitForMakeOrderButton() {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(getMakeOrderButton()));
    }

    @Step("Проверка текста кнопки Оформить заказ")
    public boolean isMakeOrderButtonDisplayed() {
        WebElement makeOrderButton = waitForMakeOrderButton();
        return makeOrderButton.getText().equals("Оформить заказ");
    }
}
