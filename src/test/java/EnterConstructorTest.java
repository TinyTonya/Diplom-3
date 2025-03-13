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
import pageObject.MainPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class EnterConstructorTest {
    private static final Logger logger = LoggerFactory.getLogger(EnterConstructorTest.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private AuthorizationPage authorizationPage;
    private RegisterPage registryPage;
    private MainPage mainPage;
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
        mainPage = new MainPage(driver);

        // Инициализация клиента для API
        stellarBurgerClient = new StellarBurgerClient(TestConfig.BASE_URI);

        // Регистрация и логин пользователя в предусловии
        registerAndLoginUser();
    }

    @Step("Регистрация и логин пользователя")
    private void registerAndLoginUser() {
        // Создание данных пользователя
        email = "testuser" + System.currentTimeMillis() + "@example.com";
        password = "password123";
        User user = new User(email, password, "Test User");

        // Регистрация пользователя через API
        accessToken = stellarBurgerClient.registerUser(user)
                .extract()
                .path("accessToken");

        // Переход в личный кабинет
        WebElement lkButton = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getLkButton()));
        lkButton.click();
        authorizationPage.enterEmail(email);
        authorizationPage.enterPassword(password);
        authorizationPage.clickAuthorizationButton();
        lkButton.click();
    }

    @Test
    @DisplayName("Проверка перехода на страницу конструктора через кнопку 'Конструктор'")
    public void testEnterConstructorViaButton() {
        logger.info("Начало теста: Проверка перехода на страницу конструктора через кнопку 'Конструктор'");

        // Шаг 1: Нажать на кнопку "Конструктор"
        logger.debug("Нажатие на кнопку 'Конструктор'");
        WebElement constructorButton = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getConstructorButton()));
        constructorButton.click();

        // Шаг 2: Проверить, что текст "Соберите бургер" отображается
        logger.debug("Проверка текста 'Соберите бургер'");
        WebElement assembleBurgerText = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getAssembleBurgerText()));
        String actualText = assembleBurgerText.getText();
        assertEquals("Соберите бургер", actualText);

        logger.info("Тест завершён успешно");
    }

    @Test
    @DisplayName("Проверка перехода на страницу конструктора через логотип")
    public void testEnterConstructorViaLogo() {
        logger.info("Начало теста: Проверка перехода на страницу конструктора через логотип");

        // Шаг 1: Нажать на логотип
        logger.debug("Нажатие на логотип");
        WebElement logo = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getLogo()));
        logo.click();

        // Шаг 2: Проверить, что текст "Соберите бургер" отображается
        logger.debug("Проверка текста 'Соберите бургер'");
        WebElement assembleBurgerText = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getAssembleBurgerText()));
        String actualText = assembleBurgerText.getText();
        assertEquals("Соберите бургер", actualText);

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