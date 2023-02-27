package api.client;

import common.constants.Constants;
import io.restassured.response.Response;

import static io.restassured.RestAssured.given;

public class BaseApiClient {
    protected Response post(String url, Object body) {
        return given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.BEARER_TOKEN)
                .and()
                .body(body)
                .when()
                .post(url);
    }

    protected Response delete(String url) {
        return given()
                .auth().oauth2(Constants.BEARER_TOKEN)
                .delete(url);
    }

}
