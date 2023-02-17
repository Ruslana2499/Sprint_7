package api.client;

import common.constants.Constants;
import common.entities.Courier;
import common.entities.CourierAuth;
import common.entities.IdResult;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class CourierClient {
    public Response authCourier(CourierAuth body) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.BEARER_TOKEN)
                .and()
                .body(body)
                .when()
                .post("/api/v1/courier/login");
    }

    public Response createCourier(Object body) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.BEARER_TOKEN)
                .and()
                .body(body)
                .when()
                .post("/api/v1/courier");
    }

    public void deleteCourier(Courier courier) {
        CourierAuth courierAuth = new CourierAuth(courier);
        Response response = authCourier(courierAuth);
        IdResult idResult = response.body().as(IdResult.class);
        given()
                .auth().oauth2(Constants.BEARER_TOKEN)
                .delete("/api/v1/courier/" + idResult.getId());
    }

}