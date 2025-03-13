import client.StellarBurgerClient;
import config.BrowserFactory;
import config.TestConfig;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObject.AuthorizationPage;
import pageObject.RegisterPage;
import pageObject.SetNewPasswordPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class AuthorizationPageTest {
    private static final Logger logger = LoggerFactory.getLogger(AuthorizationPageTest.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private AuthorizationPage authorizationPage;
    private RegisterPage registryPage;
    private SetNewPasswordPage recoverPasswwordPage;
    private StellarBurgerClient stellarBurgerClient;
    private String accessToken; // Для хранения токена созданного пользователя
    private String email; // Для хранения email пользователя
    private String password; // Для хранения пароля пользователя

    @Before
    public void setUp() {
        //Инициализация драйвера
        driver = BrowserFactory.getWebDriver();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Открытие страницы авторизации
        driver.get(TestConfig.BASE_URI);

        // Инициализация Page Object
        authorizationPage = new AuthorizationPage(driver);
        registryPage = new RegisterPage(driver);
        recoverPasswwordPage = new SetNewPasswordPage(driver);

        // Инициализация клиента для API
        stellarBurgerClient = new StellarBurgerClient(TestConfig.BASE_URI);

        // Регистрация пользователя в предусловии
        registerUser();
    }

    @Step("Регистрация пользователя")
    private void registerUser() {
        // Создание данных пользователя
        email = "testuser" + System.currentTimeMillis() + "@example.com";
        password = "password123";
        User user = new User(email, password, "Test User");

        // Регистрация пользователя через API
        accessToken = stellarBurgerClient.registerUser(user)
                .extract()
                .path("accessToken");
    }

    @Test
    @DisplayName("Проверка входа по кнопке «Войти в аккаунт» на главной")
    public void testSuccessfulAuthorizationFromLogInToAccountButton() {
        logger.info("Начало теста: Проверка входа по кнопке «Войти в аккаунт» на главной");

        // Шаг 1: Нажать на кнопку "Войти в аккаунт"
        logger.debug("Нажатие на кнопку 'Войти в аккаунт'");
        authorizationPage.clickLogInToAccountButton();
        authorizationPage.enterEmail(email);
        authorizationPage.enterPassword(password);
        authorizationPage.clickAuthorizationButton();

        // Шаг 5: Проверить, что текст кнопки "Сделать заказ" равен ожидаемому
        logger.debug("Проверка текста кнопки 'Оформить заказ'");
        WebElement makeOrderButton = wait.until(ExpectedConditions.visibilityOfElementLocated(authorizationPage.getMakeOrderButton()));
        String buttonText = makeOrderButton.getText();
        assertEquals("Оформить заказ", buttonText);

        logger.info("Тест завершён успешно");
    }

    @Test
    @DisplayName("Проверка входа через кнопку 'Личный кабинет'")
    public void testSuccessfulAuthorizationFromLKButton() {
        logger.info("Начало теста: Проверка входа через кнопку 'Личный кабинет'");

        // Шаг 1: Нажать на кнопку "Личный кабинет"
        logger.debug("Нажатие на кнопку 'Личный кабинет'");
        WebElement lkButton = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getLkButton()));
        lkButton.click();

        authorizationPage.enterEmail(email);
        authorizationPage.enterPassword(password);
        authorizationPage.clickAuthorizationButton();

        // Шаг 5: Проверить, что текст кнопки "Оформить заказ" равен ожидаемому
        logger.debug("Проверка текста кнопки 'Оформить заказ'");
        WebElement makeOrderButton = wait.until(ExpectedConditions.visibilityOfElementLocated(authorizationPage.getMakeOrderButton()));
        String buttonText = makeOrderButton.getText();
        assertEquals("Оформить заказ", buttonText);

        logger.info("Тест завершён успешно");
    }

    @Test
    @DisplayName("Проверка входа через кнопку в форме регистрации")
    public void testSuccessfulAuthorizationInRegistryForm() {
        logger.info("Начало теста: Проверка входа через кнопку в форме регистрации");

        // Шаг 1: Нажать на кнопку "Личный кабинет"
        logger.debug("Нажатие на кнопку 'Личный кабинет'");
        WebElement lkButton = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getLkButton()));
        lkButton.click();

        // Шаг 2: Переход на страницу регистрации
        logger.debug("Переход на страницу регистрации");
        WebElement registerLinkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getRegisterLinkElement()));
        registryPage.scrollToElement(registerLinkElement);
        registerLinkElement.click();

        // Шаг 3: Нажать на кнопку "Войти" под формой регистрации
        logger.debug("Нажатие на кнопку 'Войти' под формой регистрации");
        WebElement logInUnderRegistryFormElement = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getLogInButtonUnderRegistryFormElement()));
        logInUnderRegistryFormElement.click();

        authorizationPage.enterEmail(email);
        authorizationPage.enterPassword(password);
        authorizationPage.clickAuthorizationButton();

        // Шаг 7: Проверить, что текст кнопки "Оформить заказ" равен ожидаемому
        logger.debug("Проверка текста кнопки 'Оформить заказ'");
        WebElement makeOrderButton = wait.until(ExpectedConditions.visibilityOfElementLocated(authorizationPage.getMakeOrderButton()));
        String buttonText = makeOrderButton.getText();
        assertEquals("Оформить заказ", buttonText);

        logger.info("Тест завершён успешно");
    }

    @Test
    @DisplayName("Проверка входа через кнопку в форме восстановления пароля")
    public void testSuccessfulAuthorizationInRecoverPasswordForm() {
        logger.info("Начало теста: Проверка входа через кнопку в форме восстановления пароля");
        logger.debug("Нажатие на кнопку 'Личный кабинет'");
        WebElement lkButton = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getLkButton()));
        lkButton.click();
        logger.debug("Скролл до элемента 'Восстановить пароль'");
        WebElement getrecoverPasswordLinkElement = wait.until(ExpectedConditions.visibilityOfElementLocated(recoverPasswwordPage.getRecoverPasswordLinkElement()));
        registryPage.scrollToElement(getrecoverPasswordLinkElement);
        logger.debug("Клик на 'Восстановить пароль'");
        getrecoverPasswordLinkElement.click();
        logger.debug("Скролл до элемента 'Войти' под формой восстановления пароля");
        WebElement getlogInLinkOnRecoverPasswordPageElement = wait.until(ExpectedConditions.visibilityOfElementLocated(recoverPasswwordPage.getLogInLinkOnRecoverPasswordPageElement()));
        registryPage.scrollToElement(getlogInLinkOnRecoverPasswordPageElement);
        logger.debug("Клик по гипертексту 'Войти'");
        getlogInLinkOnRecoverPasswordPageElement.click();
        authorizationPage.enterEmail(email);
        authorizationPage.enterPassword(password);
        authorizationPage.clickAuthorizationButton();
        logger.debug("Проверка текста кнопки 'Оформить заказ'");
        WebElement makeOrderButton = wait.until(ExpectedConditions.visibilityOfElementLocated(authorizationPage.getMakeOrderButton()));
        String buttonText = makeOrderButton.getText();
        assertEquals("Оформить заказ", buttonText);

        logger.info("Тест завершён успешно");
    }

    @After
    public void tearDown() {
        // Удаление созданного пользователя через API
        if (accessToken != null) {
            stellarBurgerClient.deleteUser(accessToken);
        }

        // Закрытие браузера после теста
        if (driver != null) {
            driver.quit();
        }
    }
}