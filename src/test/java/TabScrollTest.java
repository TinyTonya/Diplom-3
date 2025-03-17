import config.BrowserFactory;
import config.TestConfig;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobject.MainPage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

import static org.junit.Assert.assertTrue;

public class TabScrollTest {
    private static final Logger logger = LoggerFactory.getLogger(TabScrollTest.class);

    private WebDriver driver;
    private WebDriverWait wait;
    private MainPage mainPage;

    @Before
    public void setUp() {
        //Инициализация драйвера
        driver = BrowserFactory.getWebDriver();

        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize(); // Максимизировать окно браузера
        driver.get(TestConfig.BASE_URI);
        mainPage = new MainPage(driver);
    }

    @Test
    public void testBunsSectionVisibility() {
        // Кликаем на таб "Соусы" (чтобы убедиться, что мы переключаемся на "Булки")
        mainPage.clickSaucesTab();
        mainPage.waitForBunsTab();
        mainPage.clickBunsTab();
        mainPage.waitForBunsHeader();
        assertTrue(mainPage.isBunsHeaderDisplayed());

        // Ожидаем, что первый ингредиент в разделе "Булки" станет видимым
        mainPage.waitForBunsIngredient();
        assertTrue(mainPage.isBunsIngredientDisplayed());
        // Проверяем, что ингредиент находится в пределах viewport
        assertTrue(mainPage.isElementInViewport(mainPage.waitForBunsIngredient()));
    }

    @Test
    public void testSaucesSectionVisibility() {
        // Предварительный шаг: переключаемся на таб "Начинки" (не "Соусы" и не "Булки")
        mainPage.waitForFillingsTab();
        mainPage.clickFillingsTab();
        mainPage.waitForSaucesTab();
        mainPage.clickSaucesTab();
        mainPage.waitForSaucesHeader();
        assertTrue(mainPage.isSaucesHeaderDisplayed());
        // Ожидаем, что первый ингредиент в разделе "Соусы" станет видимым
        mainPage.waitForSaucesIngredient();
        assertTrue(mainPage.isSaucesIngredientDisplayed());
        // Проверяем, что ингредиент находится в пределах viewport
        assertTrue(mainPage.isElementInViewport(mainPage.waitForSaucesIngredient()));
    }

    @Test
    public void testFillingsSectionVisibility() {
        // Предварительный шаг: переключаемся на таб "Соусы" (не "Начинки" и не "Булки")
        mainPage.waitForSaucesTab();
        mainPage.clickSaucesTab();
        mainPage.waitForFillingsTab();
        mainPage.clickFillingsTab();

        // Ожидаем, что заголовок "Начинки" станет видимым
        mainPage.waitForFillingsHeader();
        mainPage.isFillingsHeaderDisplayed();
        // Ожидаем, что первый ингредиент в разделе "Начинки" станет видимым
        mainPage.waitForFillingsIngredient();
        assertTrue(mainPage.isFillingsIngredientDisplayed());

        mainPage.scrollToElement(mainPage.waitForFillingsIngredient());
        // Проверяем, что ингредиент находится в пределах viewport
        assertTrue(mainPage.isElementInViewport(mainPage.waitForFillingsIngredient()));
    }

    @After
    public void tearDown() {
        // Закрываем браузер после каждого теста
        if (driver != null) {
            driver.quit();
        }
    }
}