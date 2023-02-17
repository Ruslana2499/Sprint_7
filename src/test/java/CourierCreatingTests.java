import api.client.CourierClient;
import common.constants.Constants;
import common.entities.Courier;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.equalTo;

public class CourierCreatingTests {
    private static CourierClient apiCourier = new CourierClient();

    @Before
    public void setUp() {
        RestAssured.baseURI = Constants.RESOURCE_URL;
    }

    @After
    public void cleanUp() {
        apiCourier.deleteCourier(Constants.COURIER);
    }

    @Test
    @DisplayName("Курьера можно создать")
    public void courierCreating() {
        apiCourier.createCourier(Constants.COURIER)
                .then().statusCode(201);
    }

    @Test
    @DisplayName("Нельзя создать двух одинаковых курьеров")
    public void creatingExistingCourier() {
        apiCourier.createCourier(Constants.COURIER)
                .then().statusCode(201);
        apiCourier.createCourier(Constants.COURIER)
                .then().statusCode(409);
    }

    @Test
    @DisplayName("Нельзя создать курьера без логина")
    public void courierCreatingWithoutLogin() {
        Courier courier = new Courier("", "pes2,5", "Stewie");
        apiCourier.createCourier(courier)
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Нельзя создать курьера без пароля")
    public void courierCreatingWithoutPassword() {
        Courier courier = new Courier("kuslo", "", "Stewie");
        apiCourier.createCourier(courier)
                .then().statusCode(400);
    }

    @Test
    @DisplayName("Успешный запрос возвращает {ok: true}")
    public void responseCodeTrue() {
        apiCourier.createCourier(Constants.COURIER)
                .then().assertThat().body("ok", equalTo(true));
    }

    @Test
    @DisplayName("Если одного из полей нет, запрос возвращает ошибку;")
    public void courierCreatingWithoutLoginHasErrorMessage() {
        Courier courier = new Courier("", "pes2,5", "Stewie");
        apiCourier.createCourier(courier)
                .then().statusCode(400).and().assertThat().body("message", equalTo("Недостаточно данных для создания учетной записи"));
    }

    @Test
    @DisplayName("Если создать пользователя с логином, который уже есть, возвращается ошибка.")
    public void creatingExistingCourierHasErrorMessage() {
        apiCourier.createCourier(Constants.COURIER)
                .then().statusCode(201);
        apiCourier.createCourier(Constants.COURIER)
                .then().statusCode(409).and().assertThat().body("message", equalTo("Этот логин уже используется. Попробуйте другой."));
    }
}
