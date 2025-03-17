package config;

public class TestConfig {
    public static final String BASE_URI = "https://stellarburgers.nomoreparties.site/";

    // Выбор браузера (CHROME или YANDEX)
    public static final String BROWSER = System.getProperty("browser", "CHROME");
}
