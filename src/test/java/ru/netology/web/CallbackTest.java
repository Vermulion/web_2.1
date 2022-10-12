package ru.netology.web;


import io.github.bonigarcia.wdm.WebDriverManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

class CallbackTest {
    private WebDriver driver;

    @BeforeAll
    static void setUpAll() {

        WebDriverManager.chromedriver().setup();
    }

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--disable-dev-shm-usage");
        options.addArguments("--no-sandbox");
        options.addArguments("--headless");
        driver = new ChromeDriver(options);
        driver.get("http://localhost:9999");
    }

    @AfterEach
    void tearDown() {
        driver.quit();
        driver = null;
    }

    @Test
    void withoutLastName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Арсентий");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                text.trim());
    }

    @Test
    void phoneNumberIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Арсентий Петров");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения",
                text.trim());
    }

    @Test
    void nameIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Поле обязательно для заполнения",
                text.trim());
    }

    @Test
    void nameInLatinLetters() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Arsentiy Petrov");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='name'].input_invalid .input__sub")).getText();
        assertEquals("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы.",
                text.trim());
    }

    @Test
    void doubleName() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кириллов-Андреев Иван");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                text.trim());
    }

    @Test
    void nameWithPatronymic() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кириллов Андрей Иванович");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id=order-success]")).getText();
        assertEquals("Ваша заявка успешно отправлена! Наш менеджер свяжется с вами в ближайшее время.",
                text.trim());
    }

    @Test
    void phoneNumberHasElevenSymbols() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Арсентий Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("89990000000");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                text.trim());
    }

    @Test
    void phoneNumberHasThirteenSymbols() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Арсентий Петров");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+799912345678");
        driver.findElement(By.cssSelector("[data-test-id=agreement]")).click();
        driver.findElement(By.className("button__text")).click();
        String text = driver.findElement(By.cssSelector("[data-test-id='phone'].input_invalid .input__sub")).getText();
        assertEquals("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678.",
                text.trim());
    }

    @Test
    void checkBoxIsEmpty() {
        driver.findElement(By.cssSelector("[data-test-id=name] input")).sendKeys("Кириллов Андрей");
        driver.findElement(By.cssSelector("[data-test-id=phone] input")).sendKeys("+79990000000");
        driver.findElement(By.className("button__text")).click();
        assertFalse(!driver.findElement(By.cssSelector("[data-test-id=agreement].input_invalid .checkbox__text")).isDisplayed());
    }
}
