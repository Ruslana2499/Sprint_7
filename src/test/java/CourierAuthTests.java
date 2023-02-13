import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierAuthTests {
    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = Constants.resourceUrl + "/api/v1/courier";
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(Constants.courier)
                .when()
                .post();
        RestAssured.baseURI = Constants.resourceUrl + "/api/v1/courier/login";
    }

    @AfterClass
    public static void cleanUp() {
        RestAssured.baseURI = Constants.resourceUrl;
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .auth().oauth2(Constants.bearerToken)
                        .and()
                        .body(new CourierAuth(Constants.courier))
                        .when()
                        .post("/api/v1/courier/login");
        IdResult idResult = response.body().as(IdResult.class);
        given()
                .auth().oauth2(Constants.bearerToken)
                .delete("/api/v1/courier/" + idResult.getId());
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void auth() {
        CourierAuth courierAuth = new CourierAuth(Constants.courier);
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля")
    public void authWithoutLogin() {
        CourierAuth courierAuth = new CourierAuth("", "1234");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля")
    public void authWithoutPassword() {
        CourierAuth courierAuth = new CourierAuth("1234", "");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Система вернёт ошибку, если неправильно указать логин или пароль")
    public void passwordEnteredIncorrectly() {
        CourierAuth courierAuth = new CourierAuth("kuslo", "1234");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку")
    public void authWithoutLoginHasErrorMessage() {
        CourierAuth courierAuth = new CourierAuth("", "1234");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку")
    public void authWithoutPasswordHasErrorMessage() {
        CourierAuth courierAuth = new CourierAuth("1234", "");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;")
    public void passwordEnteredIncorrectlyHasErrorMessage() {
        CourierAuth courierAuth = new CourierAuth("kuslo", "1234");
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    public void authWillReturnId() {
        CourierAuth courierAuth = new CourierAuth(Constants.courier);
        given()
                .header("Content-type", "application/json")
                .auth().oauth2(Constants.bearerToken)
                .and()
                .body(courierAuth)
                .when()
                .post()
                .then().assertThat().body("id", notNullValue());
    }
}
