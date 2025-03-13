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
import pageObject.PersonalAccountPage;
import pageObject.RegisterPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertEquals;

public class ExitPersonalAccountTest {
    private static final Logger logger = LoggerFactory.getLogger(EnterConstructorTest.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private AuthorizationPage authorizationPage;
    private RegisterPage registryPage;
    private PersonalAccountPage personalAccountPage;
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
        personalAccountPage = new PersonalAccountPage(driver);

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
    @DisplayName("Проверка разлогина при нажатии на Выход в ЛК")
    public void testLogOut() {
        logger.info("Начало теста: Проверка разлогина при нажатии на Выход");

        logger.debug("Нажатие на кнопку 'Выход'");
        WebElement logoutButton = wait.until(ExpectedConditions.visibilityOfElementLocated(personalAccountPage.getLogoutButton()));
        logoutButton.click();

        logger.debug("Проверка текста 'Вход'");
        WebElement assembleBurgerText = wait.until(ExpectedConditions.visibilityOfElementLocated(registryPage.getEnterTitleText()));
        String actualText = assembleBurgerText.getText();
        assertEquals("Вход", actualText);

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
