import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.notNullValue;

public class ListOrdersTests {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.resourceUrl + "/api/v1/orders";
    }

    @Test
    @DisplayName("В тело ответа возвращается список заказов")
    public void listOrders() {
        given()
                .auth().oauth2(Constants.bearerToken)
                .get()
                .then().assertThat().body("orders", notNullValue());
    }
}
