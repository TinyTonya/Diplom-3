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
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobject.AuthorizationPage;
import pageobject.RegisterPage;
import pageobject.SetNewPasswordPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

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
        authorizationPage.logIn(email, password);

        // Шаг 5: Проверить, что текст кнопки "Сделать заказ" равен ожидаемому
        logger.debug("Проверка текста кнопки 'Оформить заказ'");
        assertTrue(authorizationPage.isMakeOrderButtonDisplayed());


        logger.info("Тест завершён успешно");
    }

    @Test
    @DisplayName("Проверка входа через кнопку 'Личный кабинет'")
    public void testSuccessfulAuthorizationFromLKButton() {
        registryPage.waitForLKButton();
        registryPage.clickLKButton();
        authorizationPage.logIn(email, password);
        assertTrue(authorizationPage.isMakeOrderButtonDisplayed());
    }

    @Test
    @DisplayName("Проверка входа через кнопку в форме регистрации")
    public void testSuccessfulAuthorizationInRegistryForm() {
        registryPage.waitForLKButton();
        registryPage.clickLKButton();
        registryPage.navigateToRegisterLinkElement();
        registryPage.clickRegisterLink();
        registryPage.waitForLogInButtonUnderRegistryForm();
        registryPage.clickLogInButtonUnderRegistryFormElement();
        authorizationPage.logIn(email, password);
        assertTrue(authorizationPage.isMakeOrderButtonDisplayed());
    }

    @Test
    @DisplayName("Проверка входа через кнопку в форме восстановления пароля")
    public void testSuccessfulAuthorizationInRecoverPasswordForm() {
        registryPage.waitForLKButton();
        registryPage.clickLKButton();
        recoverPasswwordPage.navigateToRecoverPasswordLinkElement();
        recoverPasswwordPage.clickRecoverPasswordLink();
        recoverPasswwordPage.navigateToLogInLinkOnRecoverPasswordPage();
        recoverPasswwordPage.clickLogInLinkOnRecoverPasswordPage();
        authorizationPage.logIn(email, password);
        assertTrue(authorizationPage.isMakeOrderButtonDisplayed());
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