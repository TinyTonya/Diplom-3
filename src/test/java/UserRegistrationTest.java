import client.StellarBurgerClient;
import config.BrowserFactory;
import config.TestConfig;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import model.Credentials;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObject.RegisterPage;

import java.time.Duration;

public class UserRegistrationTest {

    private WebDriver driver;
    private RegisterPage registerPage;
    private StellarBurgerClient stellarBurgerClient;
    private String accessToken; // Для хранения токена созданного пользователя

    @Before
    public void setUp() {
        driver = BrowserFactory.getWebDriver();
        driver.get(TestConfig.BASE_URI);
        registerPage = new RegisterPage(driver);
        stellarBurgerClient = new StellarBurgerClient(TestConfig.BASE_URI);
    }

    @Test
    @DisplayName("Тест на успешную регистрацию")
    public void testSuccessfulRegistration() {
        // Регистрация через фронт
        registerPage.clickLKButton();
        registerPage.clickRegisterLink();
        registerPage.enterName("Иван");
        registerPage.enterEmail("ivan34567891@example.com");
        registerPage.enterPassword("123456");
        registerPage.clickRegisterButton();
        registerPage.getEnterTitleText();
    }

    @Test
    @DisplayName("Регистрация с паролем менее 6 символов")
    public void testRegistrationWithPasswordLessThan6Characters() {

        registerPage.clickLKButton();
        registerPage.clickRegisterLink();
        registerPage.enterName("Иван");
        registerPage.enterEmail("ivan34567891@example.com");
        registerPage.enterPassword("123");
        registerPage.clickRegisterButton();
        registerPage.getWrongPasswordErrorMessage();
    }


    @After
    public void tearDown() {
        // Логин через API для получения токена
        Credentials credentials = new Credentials("ivan34567891@example.com", "123456");
        accessToken = stellarBurgerClient.loginUser(credentials)
                .extract()
                .path("accessToken");
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