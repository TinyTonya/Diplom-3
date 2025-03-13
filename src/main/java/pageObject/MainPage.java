package pageObject;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class MainPage {
    private final WebDriver driver;

    // Локатор кнопки "Конструктор"
    private final By constructorButton = By.xpath("//a[@class='AppHeader_header__link__3D_hX']");

    // Локатор логотипа
    private final By logo = By.xpath("//div[@class='AppHeader_header__logo__2D0X2']//a");

    // Локатор текста "Соберите бургер"
    private final By assembleBurgerText = By.xpath("//h1[contains(@class, 'text_type_main-large') and text()='Соберите бургер']");

    // Локаторы для табов
    private final By bunsTab = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')][1]"); // Первый таб
    private final By saucesTab = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')][2]"); // Второй таб
    private final By fillingsTab = By.xpath("//div[contains(@class, 'tab_tab__1SPyG')][3]"); // Третий таб

    // Локаторы для заголовков разделов
    private final By bunsHeader = By.xpath("//h2[text()='Булки']");
    private final By saucesHeader = By.xpath("//h2[text()='Соусы']");
    private final By fillingsHeader = By.xpath("//h2[text()='Начинки']");

    // Локаторы для первых ингредиентов в каждом разделе
    private final By bunsIngredient = By.xpath("//h2[text()='Булки']/following-sibling::ul//a[contains(@class, 'BurgerIngredient_ingredient__1TVf6')]");
    private final By saucesIngredient = By.xpath("//h2[text()='Соусы']/following-sibling::ul//a[contains(@class, 'BurgerIngredient_ingredient__1TVf6')]");
    private final By fillingsIngredient = By.xpath("//h2[text()='Начинки']/following-sibling::ul//a[contains(@class, 'BurgerIngredient_ingredient__1TVf6')]");
    public MainPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Получить локатор кнопки 'Конструктор'")
    public By getConstructorButton() {
        return constructorButton;
    }

    @Step("Получить локатор логотипа")
    public By getLogo() {
        return logo;
    }

     @Step("Получить локатор текста 'Соберите бургер'")
    public By getAssembleBurgerText() {
        return assembleBurgerText;
    }

    @Step("Получить локатор таба 'Булки'")
    public By getBunsTab() {
        return bunsTab;
    }

    @Step("Получить локатор таба 'Соусы'")
    public By getSaucesTab() {
        return saucesTab;
    }

    @Step("Получить локатор таба 'Начинки'")
    public By getFillingsTab() {
        return fillingsTab;
    }

    @Step("Получить локатор заголовка 'Булки'")
    public By getBunsHeader() {
        return bunsHeader;
    }

    @Step("Получить локатор заголовка 'Соусы'")
    public By getSaucesHeader() {
        return saucesHeader;
    }

    @Step("Получить локатор заголовка 'Начинки'")
    public By getFillingsHeader() {
        return fillingsHeader;
    }

    @Step("Получить локатор ингредиента 'Булки'")
    public By getBunsIngredient() {
        return bunsIngredient;
    }

    @Step("Получить локатор ингредиента 'Соусы'")
    public By getSaucesIngredient() {
        return saucesIngredient;
    }

    @Step("Получить локатор ингредиента 'Начинки'")
    public By getFillingsIngredient() {
        return fillingsIngredient;
    }
}