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
import pageObject.PersonalAccountPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class EnterPersonalAccountTest {
    private static final Logger logger = LoggerFactory.getLogger(EnterPersonalAccountTest.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private AuthorizationPage authorizationPage;
    private RegisterPage registryPage;
    private PersonalAccountPage accountPage;
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
        accountPage = new PersonalAccountPage(driver);

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
    @DisplayName("Проверка входа в личный кабинет и отображения данных")
    public void testEnterPersonalAccount() {
        logger.info("Начало теста: Проверка входа в личный кабинет и отображения данных");

        // Шаг 1: Нажать на кнопку "Личный кабинет"
        logger.debug("Нажатие на кнопку 'Личный кабинет'");
        WebElement lkButton = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getLkButton()));
        lkButton.click();
        authorizationPage.enterEmail(email);
        authorizationPage.enterPassword(password);
        authorizationPage.clickAuthorizationButton();
        lkButton.click();
        // Шаг 2: Получить текст со страницы и проверить его
        logger.debug("Получение текста со страницы");
        WebElement accountDescriptionText = wait.until(ExpectedConditions.visibilityOfElementLocated(accountPage.getAccountDescriptionText()));
        String actualText = accountDescriptionText.getText();
        assertEquals("В этом разделе вы можете изменить свои персональные данные", actualText);

        // Шаг 3: Получить кнопку "Выход" и проверить её текст
        logger.debug("Проверка текста кнопки 'Выход'");
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(accountPage.getLogoutButton()));
        String logoutButtonText = logoutButton.getText();
        assertEquals("Выход", logoutButtonText);

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