import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CourierCreatingTests {

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.resourceUrl + "/api/v1/courier";
    }

    @After
    public void cleanUp() {
        RestAssured.baseURI = Constants.resourceUrl;

        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(Constants.bearerToken)
                        .and()
                        .body(new CourierAuth(Constants.courier))
                        .when()
                        .post("/api/v1/courier/login");
        if (response.getStatusCode() == 200) {
            IdResult idResult = response.body().as(IdResult.class);
            given()
                    .auth().oauth2(Constants.bearerToken)
                    .delete("/api/v1/courier/" + idResult.getId());
        }
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void courierCreating() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.courier)
                .when()
                .post()
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void creatingExistingCourier() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.courier)
                .when()
                .post()
                .then().statusCode(201);
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.courier)
                .when()
                .post()
                .then().statusCode(409);
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void courierCreatingWithoutLogin() {
        Courier courier = new Courier("", "pes2,5", "Stewie");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courier)
                .when()
                .post()
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void courierCreatingWithoutPassword() {
        Courier courier = new Courier("kuslo", "", "Stewie");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courier)
                .when()
                .post()
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Успешный запрос возвращает {ok: true}")
    public void responseCodeTrue() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.courier)
                .when()
                .post()
                .then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Если одного из полей нет, запрос возвращает ошибку;")
    public void courierCreatingWithoutLoginHasErrorMessage() {
        Courier courier = new Courier("", "pes2,5", "Stewie");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courier)
                .when()
                .post()
                .then().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка.")
    public void creatingExistingCourierHasErrorMessage() {
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.courier)
                .when()
                .post()
                .then().statusCode(201);
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.courier)
                .when()
                .post()
                .then().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
