import io.qameta.allure.junit4.DisplayName;
import pojo.Courier;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.CourierCredentials;

import static org.junit.Assert.*;

public class PositiveCourierCreateTest {

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
    @DisplayName("Успешное создание курьера")
    public void courierCanBeCreated() {

        //Успешное создание курьера
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CREATED, createStatusCode);
        boolean created = createResponse.extract().path("ok");
        assertTrue(created);

        //Проверяем, что можем залогинится под кредами ранее созданного курьера
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, loginStatusCode);
        //Вычисляем айди курьера, для того, чтобы потом его удалить
        courierId = loginResponse.extract().path("id");
        assertNotEquals(courierId, 0);
    }

    @Test
    @DisplayName("Создание курьера без имени")
    public void courierCanBeCreatedWithoutFirstName() {

        //Успешное создание курьера без имени
        Courier courierWithoutFirstName = CourierData.getCourierWithoutFirstName();
        ValidatableResponse createResponse = courierClient.create(courier);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CREATED, createStatusCode);
        boolean created = createResponse.extract().path("ok");
        assertTrue(created);
        //Проверяем, что можем залогиниться под курьером(значит он создан)
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, loginStatusCode);
        //Вычисляем айди курьера, для того, чтобы потом его удалить
        courierId = loginResponse.extract().path("id");
        assertNotEquals(courierId, 0);
    }
    @Test
    @DisplayName("Создание курьера с пустым полем FirstName")
    public void courierWithEmptyFirstName() {
    Courier getCourierWithEmptyFirstName = CourierData.getCourierWithEmptyFirstName();
    ValidatableResponse createResponse = courierClient.create(courier);
    int createStatusCode = createResponse.extract().statusCode();
    assertEquals(HttpStatus.SC_CREATED, createStatusCode);
    boolean created = createResponse.extract().path("ok");
    assertTrue(created);
    //Проверяем, что можем залогиниться под курьером(значит он создан)
    ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
    int loginStatusCode = loginResponse.extract().statusCode();
    assertEquals(HttpStatus.SC_OK, loginStatusCode);
    //Вычисляем айди курьера, для того, чтобы потом его удалить
    courierId = loginResponse.extract().path("id");
    assertNotEquals(courierId, 0);
}
}