import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.with;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddProductToCartAETest extends BaseTest {

    JSONObject user;

    @BeforeEach
    public void setDriverAndUrl() throws IOException {
        driver = new ChromeDriver();
        baseUrl = "https://automationexercise.com";
        wait = new WebDriverWait(driver, Duration.of(7, ChronoUnit.SECONDS));
        user = createUser();
    }

    @Test
    public void shouldAddToCart() {
        driver.get(baseUrl + "/login");
        driver.findElement(By.cssSelector("[data-qa='login-email']")).sendKeys(user.get("email").toString());
        driver.findElement(By.cssSelector("[data-qa='login-password']")).sendKeys(user.get("password").toString());
        driver.findElement(By.cssSelector("[data-qa='login-button']")).click();
        driver.findElement(By.cssSelector(".navbar-nav .fa.fa-lock")).isDisplayed();

        driver.get(baseUrl + "/product_details/1");
        driver.findElement(By.className("product-information")).isDisplayed();
        driver.findElement(By.cssSelector(".product-information .cart")).click();
        driver.findElement(By.cssSelector("#cartModal button.close-modal"));

        driver.get(baseUrl + "/view_cart");
        driver.findElement(By.id("cart_items")).isDisplayed();

        with().pollDelay(200, TimeUnit.MILLISECONDS).await().atMost
                (10, TimeUnit.SECONDS).until(driver.findElement(By.cssSelector("#product-1 .cart_description h4"))::isDisplayed);

        assertEquals(driver.findElement(By.cssSelector("#product-1 .cart_description h4")).getText(), "Blue Top");
    }

    private JSONObject createUser() throws IOException {
        OkHttpClient client = new OkHttpClient();

        final String name = faker.name().username();
        final String email = faker.internet().emailAddress();
        final String pass = faker.internet().password();

        FormBody formBody = new FormBody(
                List.of("name", "email", "password", "firstname", "lastname", "address1", "country", "state", "city", "zipcode", "mobile_number"),
                List.of(name, email, pass, "1", "4", "2", "1", "1", "1", "1", "1")
        );

        Request postRequest = new Request.Builder()
                .url(baseUrl + "/api/createAccount")
                .post(formBody)
                .build();

        Call call = client.newCall(postRequest);

        Response r = call.execute();
        assertTrue(r.isSuccessful());

        return new JSONObject()
                .put("email", email)
                .put("password", pass);
    }
}
