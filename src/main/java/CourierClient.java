import io.qameta.allure.Step;
import io.restassured.response.ValidatableResponse;
import pojo.Courier;
import pojo.CourierCredentials;

import static io.restassured.RestAssured.given;

public class CourierClient {
    private static final String COURIER_PATH = "api/v1/courier/";
    private static final String LOGIN_COURIER_PATH = "api/v1/courier/login/";
    private static final String DELETE_COURIER_PATH = "api/v1/courier/";

    @Step("Создание курьера")
    public ValidatableResponse create(Courier courier) {
        return given()
                .spec(RestClient.getBaseSpec())
                .body(courier).log().all()
                .when()
                .post(COURIER_PATH)
                .then().log().all();
    }

    @Step("Логин курьера")
    public ValidatableResponse login(CourierCredentials credentials) {
        return given()
                .spec(RestClient.getBaseSpec())
                .body(credentials).log().all()
                .when()
                .post(LOGIN_COURIER_PATH)
                .then().log().all();
    }

    @Step("Удаление курьера по id")
    public ValidatableResponse delete(int courierId) {
        return given()
                .spec(RestClient.getBaseSpec())
                .delete(DELETE_COURIER_PATH + courierId)
                .then();
    }
}