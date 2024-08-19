import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.CourierCredentials;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierLoginParametrizedTest {
    private final String login;
    private final String password;
    private CourierClient courierClient;
    private CourierCredentials courierCredentials;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courierCredentials = new CourierCredentials(login, password);
    }

    public CourierLoginParametrizedTest(String login, String password) {
        this.login = login;
        this.password = password;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getCourierData() {
        return Arrays.asList(new Object[][]{
                {null, null},
                {null, "1234"},
                {"hahasce", null},
                {"", "3454"},
                {"sasake", ""},
                {"", ""}
        });
    }

    // При попытке залогинится без пароля возникает ошибка 504. bug.
    @Test
    @DisplayName("Ошибки валидации при логине курьера без обязательных полей в запросе")
    public void errorValidationLoginCourierTest() {
        ValidatableResponse loginErrorResponse = courierClient.login(courierCredentials);
        int loginStatusCode = loginErrorResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_BAD_REQUEST, loginStatusCode);
        String errorMessage = loginErrorResponse.extract().path("message");
        assertEquals("Недостаточно данных для входа", errorMessage);
    }

}