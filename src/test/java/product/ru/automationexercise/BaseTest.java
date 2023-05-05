package product.ru.automationexercise;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;
import product.ru.GeneralBaseTest;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static product.ru.apiUtil.createUser;

public class BaseTest extends GeneralBaseTest {

    JSONObject user;

    @BeforeEach
    public void setDriverAndUrl() throws IOException {
        BaseTest.driver = new ChromeDriver();
        baseUrl = "https://automationexercise.com";
        wait = new WebDriverWait(BaseTest.driver, Duration.of(7, ChronoUnit.SECONDS));
        user = createUser();
    }
}
