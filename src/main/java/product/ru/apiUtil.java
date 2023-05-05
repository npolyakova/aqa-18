package product.ru;

import com.github.javafaker.Faker;
import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class apiUtil {

    private static final Faker faker = new Faker();

    private final static String baseUrl = "https://automationexercise.com";

    public static JSONObject createUser() throws IOException {
        OkHttpClient client = new OkHttpClient();

        final String name = faker.name().username();
        final String email = faker.internet().emailAddress();
        final String pass = faker.internet().password();
        final String firstName = faker.name().firstName();
        final String lastName = faker.name().lastName();
        final String address1 = faker.address().fullAddress();
        final String country = faker.address().country();
        final String zipcode = faker.address().zipCode();
        final String state = faker.address().state();
        final String city = faker.address().city();
        final String mobile_number = faker.phoneNumber().phoneNumber();

        FormBody formBody = new FormBody(
                List.of(
                        "name",
                        "email",
                        "password",
                        "firstname",
                        "lastname",
                        "address1",
                        "country",
                        "state",
                        "city",
                        "zipcode",
                        "mobile_number"),
                List.of(
                        name,
                        email,
                        pass,
                        firstName,
                        lastName,
                        address1,
                        country,
                        state,
                        city,
                        zipcode,
                        mobile_number)
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
