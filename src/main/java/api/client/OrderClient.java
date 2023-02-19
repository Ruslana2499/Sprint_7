package api.client;

import common.constants.Constants;
import common.entities.Order;
import io.qameta.allure.Step;
import io.restassured.response.Response;

public class OrderClient extends BaseApiClient {
    @Step("Создание заказа")
    public Response createOrder(Order order) {
        return this.post(Constants.ORDERS_URI, order);
    }
}
