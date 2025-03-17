package config;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

public class BrowserFactory {

        // Путь к драйверам
        private static final String CHROME_DRIVER_PATH = System.getenv("CHROME_DRIVER_PATH");
        private static final String YANDEX_DRIVER_PATH = System.getenv("YANDEX_DRIVER_PATH");

        // Путь к исполняемым файлам браузеров
        private static final String YANDEX_BROWSER_PATH = System.getenv("YANDEX_BROWSER_PATH");

    public static WebDriver getWebDriver() {
        switch (TestConfig.BROWSER.toUpperCase()) {
            case "CHROME":
                return startChrome();
            case "YANDEX":
                return startYandexBrowser();
            default:
                throw new IllegalArgumentException("Не распознан браузер: " + TestConfig.BROWSER);
        }
    }


    // Метод для запуска Google Chrome
        private static WebDriver startChrome() {
            // Указываем путь к chromedriver
            System.setProperty("webdriver.chrome.driver", CHROME_DRIVER_PATH);

            // Создаем экземпляр ChromeOptions (опционально, можно настроить браузер)
            ChromeOptions options = new ChromeOptions();

            // Возвращаем новый экземпляр ChromeDriver
            return new ChromeDriver(options);
        }

        // Метод для запуска Яндекс Браузера
        private static WebDriver startYandexBrowser() {
            // Указываем путь к yandexdriver
            System.setProperty("webdriver.chrome.driver", YANDEX_DRIVER_PATH);

            // Создаем экземпляр ChromeOptions
            ChromeOptions options = new ChromeOptions();

            // Указываем путь к исполняемому файлу Яндекс Браузера
            options.setBinary(YANDEX_BROWSER_PATH);

            // Возвращаем новый экземпляр ChromeDriver с настройками для Яндекс Браузера
            return new ChromeDriver(options);
        }
    }