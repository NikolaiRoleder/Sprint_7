import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import pojo.Courier;
import pojo.CourierCredentials;

import static org.junit.Assert.assertEquals;

public class NegativeCourierLoginTest {
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

    // Пытаемся залогинить курьера, предварительно его не создав.
    @Test
    @DisplayName("Невозможность залогиниться с несуществующими логином и пролем")
    public void loginCourierWithoutCreateCourier() {
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_NOT_FOUND, loginStatusCode);
        String errorMessage = loginResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", errorMessage);
    }

    @Test
    @DisplayName("Невозможность залогиниться с некорректным логином")
    public void loginCourierWithNotCorrectLogin() {
        //Создаем курьера
        courierClient.create(courier);
        //Логинимся чтобы получить айди созданного курьера
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        // Заменяем значение в поле login
        courier.setLogin("sobacasutulaya");
        //Проверяем логин с другими данными
        ValidatableResponse loginErrrorResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginErrrorResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_NOT_FOUND, loginStatusCode);
        String errorMessage = loginErrrorResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", errorMessage);
    }

    @Test
    @DisplayName("Невозможность залогиниться с некорректным паролем")
    public void loginCourierWithNotCorrectPassword() {
        //Создаем курьера
        courierClient.create(courier);
        //Логинимся чтобы получить айди созданного курьера
        ValidatableResponse loginResponse = courierClient.login(CourierCredentials.from(courier));
        courierId = loginResponse.extract().path("id");
        // Заменяем значение в поле password
        courier.setPassword("0987");
        //Проверяем логин с другими данными
        ValidatableResponse loginErrorResponse = courierClient.login(CourierCredentials.from(courier));
        int loginStatusCode = loginErrorResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_NOT_FOUND, loginStatusCode);
        String errorMessage = loginErrorResponse.extract().path("message");
        assertEquals("Учетная запись не найдена", errorMessage);
    }
}