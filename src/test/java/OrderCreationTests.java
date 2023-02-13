import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

@RunWith(Parameterized.class)
public class OrderCreationTests {

    private final String[] color;

    public OrderCreationTests(String[] color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Iterable<Object[]> getColor() {
        return Arrays.asList(new Object[][]{
                {new String[]{"BLACK"}},
                {new String[]{"GREY"}},
        });
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.resourceUrl+ "/api/v1/orders";
    }

    @Test
    @DisplayName("Можно указать один из цветов — BLACK или GREY")
    public void color() {
        Order order = new Order(Constants.order, color);
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(order)
                .when()
                .post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Можно указать оба цвета")
    public void colorGreyAndBlack() {
        Order order = new Order(Constants.order, new String[]{"GREY", "BLACK"});
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(order)
                .when()
                .post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Можно совсем не указывать цвет")
    public void doNotSpecifyColor() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.order)
                .when()
                .post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Тело ответа содержит track")
    public void orderWillReturnTrack() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.order)
                .when()
                .post()
                .then().assertThat().body("track", notNullValue());
    }
}
