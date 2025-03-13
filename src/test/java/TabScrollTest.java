import config.BrowserFactory;
import config.TestConfig;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Rectangle;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageObject.MainPage;
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

        // Открытие главной страницы
        driver.get(TestConfig.BASE_URI);

        // Инициализация Page Object
        mainPage = new MainPage(driver);
    }

    @Test
    public void testBunsSectionVisibility() {
        // Кликаем на таб "Соусы" (чтобы убедиться, что мы переключаемся на "Булки")
         driver.findElement(mainPage.getSaucesTab()).click();

        // Ожидаем, что таб "Булки" станет кликабельным
        WebElement bunsTab = wait.until(ExpectedConditions.elementToBeClickable(mainPage.getBunsTab()));
        // Кликаем на таб "Булки"
        bunsTab.click();

        // Ожидаем, что заголовок "Булки" станет видимым
        WebElement bunsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getBunsHeader()));
        assertTrue("Заголовок 'Булки' невидимый!", bunsHeader.isDisplayed());

        // Ожидаем, что первый ингредиент в разделе "Булки" станет видимым
        WebElement bunsIngredient = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getBunsIngredient()));
        assertTrue("Ингредиент 'Булки' невидим!", bunsIngredient.isDisplayed());

        // Проверяем, что ингредиент находится в пределах viewport
        assertTrue("Ингредиент 'Булки' не находится в пределах видимой области!", isElementInViewport(bunsIngredient));
    }

    @Test
    public void testSaucesSectionVisibility() {
        // Предварительный шаг: переключаемся на таб "Начинки" (не "Соусы" и не "Булки")
        WebElement fillingsTab = wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFillingsTab()));
        fillingsTab.click();

        // Ожидаем, что таб "Соусы" станет кликабельным
        WebElement saucesTab = wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSaucesTab()));
        // Кликаем на таб "Соусы"
        saucesTab.click();

        // Ожидаем, что заголовок "Соусы" станет видимым
        WebElement saucesHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getSaucesHeader()));
        assertTrue("Заголовок 'Соусы' невидим!", saucesHeader.isDisplayed());

        // Ожидаем, что первый ингредиент в разделе "Соусы" станет видимым
        WebElement saucesIngredient = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getSaucesIngredient()));
        assertTrue("Ингредиент 'Соусы' невидим!", saucesIngredient.isDisplayed());

        // Проверяем, что ингредиент находится в пределах viewport
        assertTrue("Ингредиент 'Соусы' не находится в пределах видимой области!", isElementInViewport(saucesIngredient));
    }

    @Test
    public void testFillingsSectionVisibility() {
        // Предварительный шаг: переключаемся на таб "Соусы" (не "Начинки" и не "Булки")
        WebElement saucesTab = wait.until(ExpectedConditions.elementToBeClickable(mainPage.getSaucesTab()));
        saucesTab.click();

        // Ожидаем, что таб "Начинки" станет кликабельным
        WebElement fillingsTab = wait.until(ExpectedConditions.elementToBeClickable(mainPage.getFillingsTab()));
        // Кликаем на таб "Начинки"
        fillingsTab.click();

        // Ожидаем, что заголовок "Начинки" станет видимым
        WebElement fillingsHeader = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getFillingsHeader()));
        assertTrue("Заголовок 'Начинки' невидим!", fillingsHeader.isDisplayed());

        // Ожидаем, что первый ингредиент в разделе "Начинки" станет видимым
        WebElement fillingsIngredient = wait.until(ExpectedConditions.visibilityOfElementLocated(mainPage.getFillingsIngredient()));
        assertTrue("Ингредиент 'Начинки' невидим!", fillingsIngredient.isDisplayed());

        // Прокручиваем страницу к элементу
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", fillingsIngredient);


        // Проверяем, что ингредиент находится в пределах viewport
        assertTrue("Ингредиент 'Начинки' не находится в пределах видимой области!", isElementInViewport(fillingsIngredient));
    }

    // Метод для проверки, находится ли элемент в пределах viewport
    private boolean isElementInViewport(WebElement element) {
        // Получаем координаты и размеры элемента
        org.openqa.selenium.Rectangle rect = element.getRect();

        // Получаем размеры окна браузера
        int windowWidth = driver.manage().window().getSize().getWidth();
        int windowHeight = driver.manage().window().getSize().getHeight();

        // Логирование для отладки
        logger.info("Element coordinates: x={}, y={}, width={}, height={}", rect.getX(), rect.getY(), rect.getWidth(), rect.getHeight());
        logger.info("Window size: width={}, height={}", windowWidth, windowHeight);

        // Проверяем, что элемент полностью находится в пределах viewport
        boolean isInViewport = rect.getX() >= 0
                && rect.getY() >= 0
                && rect.getX() + rect.getWidth() <= windowWidth
                && rect.getY() + rect.getHeight() <= windowHeight;

        logger.info("Is element in viewport: {}", isInViewport);
        return isInViewport;
    }

    @After
    public void tearDown() {
        // Закрываем браузер после каждого теста
        if (driver != null) {
            driver.quit();
        }
    }
}