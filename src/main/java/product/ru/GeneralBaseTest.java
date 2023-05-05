package product.ru;

import com.github.javafaker.Faker;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

public abstract class GeneralBaseTest {

    public static ChromeDriver driver;

    public String baseUrl;

    public WebDriverWait wait;

    public Faker faker = new Faker();

    @BeforeAll
    public static void setProperty() {
        System.setProperty("webdriver.http.factory", "jdk-http-client");
    }

    @AfterEach
    public void closeAfterEach() {
        driver.close();
    }

}
