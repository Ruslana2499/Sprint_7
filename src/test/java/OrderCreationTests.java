import api.client.OrderClient;
import common.constants.Constants;
import common.entities.Order;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests {
    private OrderClient orderClient = new OrderClient();

    private final String[] color;

    public OrderCreationTests(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Iterable<Object[]> getColor() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.RESOURCE_URL;
    }

    @Test
    @DisplayName("Можно указать один из цветов — BLACK или GREY")
    public void color() {
        Order order = new Order(Constants.ORDER, color);
        orderClient.createOrder(order)
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Можно указать оба цвета")
    public void colorGreyAndBlack() {
        Order order = new Order(Constants.ORDER, new String[]{"GREY", "BLACK"});
        orderClient.createOrder(order)
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Можно совсем не указывать цвет")
    public void doNotSpecifyColor() {
        orderClient.createOrder(Constants.ORDER)
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Тело ответа содержит track")
    public void orderWillReturnTrack() {
        orderClient.createOrder(Constants.ORDER)
                .then().assertThat().body("track", notNullValue());
    }
}
