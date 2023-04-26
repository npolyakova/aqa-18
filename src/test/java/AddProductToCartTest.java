import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddProductToCartTest extends BaseTest{

    @BeforeEach
    public void setDriverAndUrl() {
        driver = new ChromeDriver();
        baseUrl = "https://demo.beseller.by";
        wait = new WebDriverWait(driver, Duration.of(7, ChronoUnit.SECONDS));
    }

    @Test
    //FIXME поля формы контактов иногда не доступны
    public void addProductToCart() {
        driver.get(baseUrl + "/bytovaya-tehnika/shveynye-mashiny/toyota_esoa_21/");
        String productName = driver.findElement(By.cssSelector(".page-title .product-name")).getText();

        driver.findElement(By.cssSelector("[data-gtm-id='add-to-cart-product']")).click();
        wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.className("cart-alert"))));
        wait.until(ExpectedConditions.textToBe(By.cssSelector(".button-basket .product-counter"), "1"));

        WebElement orderButton = driver.findElement(By.cssSelector(".product-add-block .oformit-v-korzine"));
        assertTrue(orderButton.isDisplayed());

        orderButton.click();
        assertThat(driver.getCurrentUrl()).isEqualTo(baseUrl + "/shcart/");
        assertTrue(driver.findElement(By.className("ok-order__title")).isDisplayed());

        assertThat(driver.findElements(By.cssSelector("[data-product-item]")).size()).isGreaterThan(0);

        WebElement product = driver.findElements(By.cssSelector("[data-product-item]")).get(0);
        assertThat(product.findElement(By.cssSelector(".ok-order__image img")).getAttribute("alt")).contains(productName);
        assertThat(product.findElement(By.className("ok-order__text")).getText()).contains(productName);
        assertThat(product.findElement(By.className("ok-order__text")).getText()).contains("Код: 20102");
        assertEquals(product.findElement(By.cssSelector("[data-product-item-input-quantity]")).getAttribute("value"), "1");
        assertEquals(product.findElement(By.cssSelector(".ok-table-el.f-tac.-size-half.hidden-xs")).getText(), "шт.");
        assertEquals(product.findElement(By.cssSelector("[data-product-item-sum] [data-price-value]")).getText(), "49500");

        //оформление заказа
        driver.findElement(By.name("fio")).sendKeys(faker.name().fullName());
        driver.findElement(By.name("registration")).sendKeys(faker.address().fullAddress());
        driver.findElement(By.name("phone")).sendKeys(faker.phoneNumber().phoneNumber());
        driver.findElement(By.name("comment")).sendKeys(faker.lorem().sentence());

        driver.findElement(By.id("terms_btn_cart_fast")).click();

        assertEquals(driver.getCurrentUrl(), "https://demo.beseller.by/shcart/finish");
        assertThat(driver.findElement(By.className("ok-order__title")).getText()).contains("ЗАКАЗ №", "ОФОРМЛЕН");

        WebElement orderedProduct = driver.findElements(By.cssSelector(".ok-table__tbody .ok-table-row")).get(0);
        assertThat(orderedProduct.findElement(By.cssSelector(".ok-order__image img")).getAttribute("alt")).contains(productName);
        assertThat(orderedProduct.findElement(By.className("ok-order__text")).getText()).contains(productName);
        assertThat(orderedProduct.findElement(By.className("ok-order__text")).getText()).contains("Код: 20102");
        assertEquals(orderedProduct.findElement(By.cssSelector(".ok-order__count")).getText(), "1");
        assertEquals(orderedProduct.findElement(By.cssSelector(".ok-table-el.f-tac.hidden-sm.hidden-xs")).getText(), "шт.");
        assertEquals(orderedProduct.findElement(By.cssSelector("[data-finish-order-value]")).getText(), "49500");

    }

    @Test
    public void implicityWaitTest() {
//        driver.manage().timeouts().implicitlyWait(5000, TimeUnit.MILLISECONDS);
        driver.manage().timeouts().implicitlyWait(Duration.ofMillis(5000));

        driver.manage().timeouts().pageLoadTimeout(Duration.ofMillis(5000));

//        driver.manage().timeouts().setScriptTimeout(5000);
        driver.manage().timeouts().scriptTimeout(Duration.ofMillis(5000));
    }
}
