import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.CancelOrder;
import pojo.Order;

import static io.restassured.RestAssured.given;

public class OrderClient {
    private static final String ORDER_PATH = "api/v1/orders/";
    private static final String GET_ORDER_PATH = "api/v1/orders/track/";
    private static final String CANCEL_ORDER_PATH = "api/v1/orders/cancel/";

    @Step("Создание заказа")
    public ValidatableResponse makeOrder(Order order) {
        return given()
                .spec(RestClient.getBaseSpec())
                .body(order).log().all()
                .when()
                .post(ORDER_PATH)
                .then().log().all();
    }

    @Step("Получение заказа по trackId")
    public ValidatableResponse getOrder(int trackId) {
        return given()
                .spec(RestClient.getBaseSpec())
                .queryParam("t", trackId).log().all()
                .when()
                .get(GET_ORDER_PATH)
                .then().log().all();
    }

    @Step("Отмена Заказа")
    public ValidatableResponse cancel(int trackId) {
        CancelOrder cancelOrder = new CancelOrder(Integer.toString(trackId));
        return given()
                .spec(RestClient.getBaseSpec())
                .body(cancelOrder).log().all()
                .when()
                .put(CANCEL_ORDER_PATH)
                .then().log().all();
    }

    @Step("Получение списка заказа")
    public ValidatableResponse getOrderList() {
        return given()
                .spec(RestClient.getBaseSpec()).log().all()
                .when()
                .get(ORDER_PATH)
                .then().log().all();
    }
}