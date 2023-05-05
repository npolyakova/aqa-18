package product.ru.automationexercise;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.with;

public class AddProductToCartAETest extends BaseTest {

    @Test
//    @RepeatedTest(10)
    public void shouldAddToCart() {
        BaseTest.driver.get(baseUrl + "/login");
        BaseTest.driver.findElement(By.cssSelector("[data-qa='login-email']")).sendKeys(user.get("email").toString());
        BaseTest.driver.findElement(By.cssSelector("[data-qa='login-password']")).sendKeys(user.get("password").toString());
        BaseTest.driver.findElement(By.cssSelector("[data-qa='login-button']")).click();
        BaseTest.driver.findElement(By.cssSelector(".navbar-nav .fa.fa-lock")).isDisplayed();

        BaseTest.driver.get(baseUrl + "/product_details/1");
        BaseTest.driver.findElement(By.className("product-information")).isDisplayed();
        BaseTest.driver.findElement(By.cssSelector(".product-information .cart")).click();
        BaseTest.driver.findElement(By.cssSelector("#cartModal button.close-modal"));

        BaseTest.driver.get(baseUrl + "/view_cart");
        BaseTest.driver.findElement(By.id("cart_items")).isDisplayed();

        with().pollDelay(200, TimeUnit.MILLISECONDS).await().atMost
                (10, TimeUnit.SECONDS).until(BaseTest.driver.findElement(By.cssSelector("#product-1 .cart_description h4"))::isDisplayed);

        Assertions.assertEquals(BaseTest.driver.findElement(By.cssSelector("#product-1 .cart_description h4")).getText(), "Blue Top");
    }
}
