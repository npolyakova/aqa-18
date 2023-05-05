package product.ru.beseller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;
import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AddProductToCartTest extends BaseTest {

    @BeforeEach
    public void setDriverAndUrl() {
        driver = new ChromeDriver();
        baseUrl = "https://demo.beseller.by";
        wait = new WebDriverWait(driver, Duration.of(7, ChronoUnit.SECONDS));
    }

    @Test
    public void shouldCreateOrder() {
        List<String> productNameAndPrice =
                addProductToCart("/sport-i-turizm/velosipedy/author/author_profile_2013/");

        //оформление заказа
        String fio = faker.name().fullName();
        String address = faker.address().fullAddress();
        String phone = faker.phoneNumber().phoneNumber();
        String comment = faker.lorem().sentence();

        JavascriptExecutor js = driver;
        js.executeScript("", "");

        WebElement fioEl = driver.findElement(By.name("fio"));
        await().atMost(2, TimeUnit.SECONDS).until(fioEl::isEnabled);
        fioEl.sendKeys(fio);
        driver.findElement(By.name("registration")).sendKeys(address);
        driver.findElement(By.name("phone")).sendKeys(phone);
        driver.findElement(By.name("comment")).sendKeys(comment);

        driver.findElement(By.id("terms_btn_cart_fast")).click();
        assertEquals(driver.getCurrentUrl(), "https://demo.beseller.by/shcart/finish");
        assertThat(driver.findElement(By.className("ok-order__title")).getText()).contains("ЗАКАЗ №", "ОФОРМЛЕН");

        WebElement orderedProduct = driver.findElements(By.cssSelector(".ok-table__tbody .ok-table-row")).get(0);

        //checkProductDescriptionAndAlt(orderedProduct, productNameAndPrice.get(0));
        assertEquals(orderedProduct.findElement(By.cssSelector(".ok-order__count")).getText(), "1");
        assertEquals(orderedProduct.findElement(By.cssSelector(".ok-table-el.f-tac.hidden-sm.hidden-xs")).getText(), "шт.");
        assertEquals(orderedProduct.findElement(By.cssSelector("[data-finish-order-value]")).getText(), productNameAndPrice.get(1));
    }

    public void checkProductDescriptionAndAlt(WebElement product, String productName) {
        WebElement productDescription = product.findElement(By.className("ok-order__text"));

        assertThat(productDescription.getText()).contains(productName);
        assertThat(productDescription.getText()).contains("Код: 20102");
        assertThat(product.findElement(By.cssSelector(".ok-order__image img")).getAttribute("alt")).contains(productName);
    }

    public List<String> addProductToCart(String productPath) {
        driver.get(baseUrl + productPath);

        String productName = driver.findElement(By.cssSelector(".page-title.product-name")).getText();
        String productPrice = driver.findElement(By.cssSelector(".ok-product__price-block  .current-price")).getText();

        String formattedPrice = formatPrice(productPrice.substring(0, productPrice.indexOf(",00 руб.")));

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
        //checkProductDescriptionAndAlt(product, productName);
        assertEquals(
                product.findElement(By.cssSelector("[data-product-item-input-quantity]")).getAttribute("value"),
                "1"
        );
        assertEquals(
                product.findElement(By.cssSelector(".ok-table-el.f-tac.-size-half.hidden-xs")).getText(),
                "шт."
        );
        assertEquals(
                formattedPrice,
                product.findElement(By.cssSelector("[data-product-item-sum] [data-price-value]")).getText()
        );

        return List.of(productName, formattedPrice);
    }
}
