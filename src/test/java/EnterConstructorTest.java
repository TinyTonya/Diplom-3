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
import pageobject.AuthorizationPage;
import pageobject.RegisterPage;
import pageobject.MainPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

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
        driver = BrowserFactory.getWebDriver();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        driver.get(TestConfig.BASE_URI);
        authorizationPage = new AuthorizationPage(driver);
        registryPage = new RegisterPage(driver);
        mainPage = new MainPage(driver);
        stellarBurgerClient = new StellarBurgerClient(TestConfig.BASE_URI);
        registerAndLoginUser();
    }

    @Step("Регистрация и логин пользователя")
    private void registerAndLoginUser() {
        email = "testuser" + System.currentTimeMillis() + "@example.com";
        password = "password123";
        User user = new User(email, password, "Test User");
        accessToken = stellarBurgerClient.registerUser(user)
                .extract()
                .path("accessToken");


        registryPage.waitForLKButton();
        registryPage.clickLKButton();
        authorizationPage.enterEmail(email);
        authorizationPage.enterPassword(password);
        authorizationPage.clickAuthorizationButton();
        registryPage.clickLKButton();
    }

    @Test
    @DisplayName("Проверка перехода на страницу конструктора через кнопку 'Конструктор'")
    public void testEnterConstructorViaButton() {
        mainPage.waitForConstructorButton();
        mainPage.clickConstructorButton();
        mainPage.waitForAssembleBurgerText();
        assertTrue(mainPage.isAssembleBurgerTextDisplayed());
    }

    @Test
    @DisplayName("Проверка перехода на страницу конструктора через логотип")
    public void testEnterConstructorViaLogo() {
        mainPage.waitForLogo();
        mainPage.clickLogo();
        mainPage.waitForAssembleBurgerText();
        assertTrue(mainPage.isAssembleBurgerTextDisplayed());
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