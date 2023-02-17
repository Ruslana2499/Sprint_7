package api.client;

import common.constants.Constants;
import common.entities.Order;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class OrderClient {
    public Response createOrder(Order order){
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.BEARER_TOKEN)
                .and()
                .body(order)
                .when()
                .post("/api/v1/orders");
    }
}
