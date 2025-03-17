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
import pageobject.PersonalAccountPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

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
        driver = BrowserFactory.getWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(TestConfig.BASE_URI);

        authorizationPage = new AuthorizationPage(driver);
        registryPage = new RegisterPage(driver);
        accountPage = new PersonalAccountPage(driver);

        stellarBurgerClient = new StellarBurgerClient(TestConfig.BASE_URI);
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
        registryPage.waitForLKButton();
        registryPage.clickLKButton();
        authorizationPage.logIn(email, password);
        registryPage.clickLKButton();
        assertTrue(accountPage.isAccountDescriptionTextDisplayed());
        assertTrue(accountPage.isLogoutButtonDisplayed());
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