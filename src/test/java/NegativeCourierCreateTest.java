import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Courier;
import pojo.CourierCredentials;

import static org.junit.Assert.assertEquals;

public class NegativeCourierCreateTest {

    private CourierClient courierClient;
    private Courier courier;
    private int courierId;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = CourierData.getRandomCourier();
    }

    @After
    public void cleanUp() {
        courierClient.delete(courierId);
    }

    //Текст ошибки несоответствует документации. Bug
    @Test
    @DisplayName("Проверка того что нельзя создать 2 одинаковых курьеров")
    public void impossibleCreateSameCouriers() {
        //Создаем курьера
        courierClient.create(courier);

        //Проверяем, что можем залогиниться под курьером(значит он создан)
        // и вычисляем айди курьера, для того, чтобы потом его удалить
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        // Повторно пытаемся создать курьера с такими же данными и проверяем ответ
        ValidatableResponse createErrorResponse = courierClient.create(courier);
        int createErrorStatusCode = createErrorResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CONFLICT, createErrorStatusCode);
        String errorMessage = createErrorResponse.extract().path("message");
        assertEquals("Этот логин уже используется", errorMessage);

    }

    //Текст ошибки несоответствует документации. Bug
    @Test
    @DisplayName("Проверка что нельзя создать 2 курьеров с одним логином")
    public void impossibleCreateCouriersWithSameLogin() {
        //Создаем курьера
        courierClient.create(courier);

        //Проверяем, что можем залогиниться под курьером(значит он создан)
        //и вычисляем айди курьера, для того, чтобы потом его удалить
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");

        //Проверяем что если создать пользователя с логином, который уже есть, возвращается ошибка.

        Courier courierWithSameLogin = CourierData.getCourierWithSameLogin();
        ValidatableResponse createErrorResponse = courierClient.create(courierWithSameLogin);
        int createErrorStatusCode = createErrorResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CONFLICT, createErrorStatusCode);
        String errorMessage = createErrorResponse.extract().path("message");
        assertEquals("Этот логин уже используется", errorMessage);
    }
}