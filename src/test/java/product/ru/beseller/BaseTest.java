package product.ru.beseller;

import product.ru.GeneralBaseTest;

public class BaseTest extends GeneralBaseTest {

    public static String formatPrice(String productPrice) {
        String formattedPrice = "";
        while (productPrice.contains(" ")) {
            int index = productPrice.indexOf(" ");
            formattedPrice = formattedPrice + productPrice.substring(0, productPrice.indexOf(" "));
            productPrice = productPrice.substring(index + 1);
        }
        formattedPrice += productPrice;
        return formattedPrice;
    }
}
