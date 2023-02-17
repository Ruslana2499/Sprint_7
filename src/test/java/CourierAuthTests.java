import api.client.CourierClient;
import common.constants.Constants;
import common.entities.CourierAuth;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class CourierAuthTests {
    private static CourierClient apiCourier = new CourierClient();

    @BeforeClass
    public static void setUp() {
        RestAssured.baseURI = Constants.RESOURCE_URL;
        apiCourier.createCourier(Constants.COURIER);
    }

    @AfterClass
    public static void cleanUp() {
        apiCourier.deleteCourier(Constants.COURIER);
    }

    @Test
    @DisplayName("Курьер может авторизоваться")
    public void auth() {
        CourierAuth courierAuth = new CourierAuth(Constants.COURIER);
        apiCourier.authCourier(courierAuth)
                .then().statusCode(200);
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля")
    public void authWithoutLogin() {
        CourierAuth courierAuth = new CourierAuth("", "1234");
        apiCourier.authCourier(courierAuth)
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Для авторизации нужно передать все обязательные поля")
    public void authWithoutPassword() {
        CourierAuth courierAuth = new CourierAuth("1234", "");
        apiCourier.authCourier(courierAuth)
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Система вернёт ошибку, если неправильно указать логин или пароль")
    public void passwordEnteredIncorrectly() {
        CourierAuth courierAuth = new CourierAuth("kuslo", "1234");
        apiCourier.authCourier(courierAuth)
                .then().statusCode(404);
    }

    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку")
    public void authWithoutLoginHasErrorMessage() {
        CourierAuth courierAuth = new CourierAuth("", "1234");
        apiCourier.authCourier(courierAuth)
                .then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Если какого-то поля нет, запрос возвращает ошибку")
    public void authWithoutPasswordHasErrorMessage() {
        CourierAuth courierAuth = new CourierAuth("1234", "");
        apiCourier.authCourier(courierAuth)
                .then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для входа"));
    }

    @Test
    @DisplayName("Если авторизоваться под несуществующим пользователем, запрос возвращает ошибку;")
    public void passwordEnteredIncorrectlyHasErrorMessage() {
        CourierAuth courierAuth = new CourierAuth("kuslo", "1234");
        apiCourier.authCourier(courierAuth)
                .then().statusCode(404).and().assertThat().body("message", equalTo("Учетная запись не найдена"));
    }

    @Test
    @DisplayName("Успешный запрос возвращает id")
    public void authWillReturnId() {
        CourierAuth courierAuth = new CourierAuth(Constants.COURIER);
        apiCourier.authCourier(courierAuth)
                .then().assertThat().body("id", notNullValue());
    }
}
