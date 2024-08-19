import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Courier;
import pojo.CourierCredentials;

import static org.junit.Assert.*;

public class PositiveLoginCourierTest {
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

    @Test
    @DisplayName("Курьер может залогиниться")
    public void courierCanBeLogin() {
        //Создаем курьера
        courierClient.create(courier);
        //Проверяем, что можем залогинится под кредами ранее созданного курьера
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, loginStatusCode);
        //Проверяем что полученный айди не равен 0 и не равен null
        courierId = loginResponse.extract().path("id");
        assertNotEquals(courierId, 0);
        assertNotNull(courierId);
    }
}
