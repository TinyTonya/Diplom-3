package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class RegisterPage {
    private WebDriver webDriver;

    // Локаторы элементов
    private final By lkButton = By.xpath("//p[contains(text(), 'Личный Кабинет')]");
    private final By registerLink = By.xpath("//a[contains(@class, 'Auth_link__1fOlj') and @href='/register']");
    private final By inputFields = By.xpath(".//form[starts-with(@class, 'Auth_form')]//fieldset//div[@class=" +
            "'input__container']//input");
    private final By registerButton = By.xpath("//button[contains(@class, 'button_button__33qZ0') and contains(text(), 'Зарегистрироваться')]");
    private final By enterTitle = By.xpath(".//main//h2");
    private final By wrongPasswordErrorMessage = By.xpath(".//*[text()='Некорректный пароль']");
    private final By logInButtonUnderRegistryForm = By.xpath("//a[text()='Войти']");

    // Конструктор класса
    public RegisterPage(WebDriver driver) {
        webDriver = driver;
    }

    public By getLkButton() {
        return lkButton;
    }

    @Step("Клик по кнопке 'Личный кабинет'")
    public void clickLKButton() {
        WebElement lkbutton = webDriver.findElement(lkButton);
        lkbutton.click();
    }

    public By getRegisterLinkElement() {
        return registerLink;
    }

    @Step("Клик по тексту 'Зарегистрироваться'")
    public void clickRegisterLink() {
        WebElement registerlink = webDriver.findElement(registerLink);
        registerlink.click();
    }

    @Step("Ввод имени")
    public void enterName(String name) {
        webDriver.findElements(inputFields).get(0).sendKeys(name);
    }

    @Step("Ввод email")
    public void enterEmail(String email) {
        webDriver.findElements(inputFields).get(1).sendKeys(email);
    }

    @Step("Ввод пароля")
    public void enterPassword(String password) {
        webDriver.findElements(inputFields).get(2).sendKeys(password);
    }

    @Step("Клик по кнопке 'Зарегистрироваться'")
    public void clickRegisterButton() {
        WebElement registerbutton = webDriver.findElement(registerButton);
        registerbutton.click();
    }

    @Step("Получение текста заголовка 'Вход'")
    public By getEnterTitleText() {
        return enterTitle;
    }

    @Step("Прокрутка вниз до элемента")
    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) webDriver).executeScript("arguments[0].scrollIntoView(true);", element);
    }

    public By getLogInButtonUnderRegistryFormElement() {
        return logInButtonUnderRegistryForm;
    }

    @Step("Получение сообщения об ошибке Некорректный пароль")
    public String getWrongPasswordErrorMessage() {
        return webDriver.findElement(wrongPasswordErrorMessage).getText();
    }
}