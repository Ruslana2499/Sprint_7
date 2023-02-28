package api.client;

import common.constants.Constants;
import common.entities.Courier;
import common.entities.CourierAuth;
import common.entities.IdResult;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class CourierClient extends BaseApiClient {
    @Step("Авторизация курьера")
    public Response authCourier(CourierAuth body) {
        return this.post(Constants.LOGIN_URI, body);
    }

    @Step("Создание курьера")
    public Response createCourier(Object body) {
        return this.post(Constants.COURIER_URI, body);
    }

    @Step("Удаление курьера")
    public void deleteCourier(Courier courier) {
        CourierAuth courierAuth = new CourierAuth(courier);
        Response response = authCourier(courierAuth);
        IdResult idResult = response.body().as(IdResult.class);
        this.delete(Constants.COURIER_URI + "/" + idResult.getId());
    }

}