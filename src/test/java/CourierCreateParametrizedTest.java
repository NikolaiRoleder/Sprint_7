import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.Courier;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.assertEquals;

@RunWith(Parameterized.class)
public class CourierCreateParametrizedTest {

    private final String login;
    private final String password;
    private final String firstName;
    private CourierClient courierClient;
    private Courier courier;

    @Before
    public void setUp() {
        courierClient = new CourierClient();
        courier = new Courier(login, password, firstName);
    }

    public CourierCreateParametrizedTest(String login, String password, String firstName) {
        this.login = login;
        this.password = password;
        this.firstName = firstName;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getCourierData() {
        return Arrays.asList(new Object[][]{
                {null, null, "sasake"},
                {null, "1234", null},
                {"hahasce", null, null},
                {"", "", "sasake"},
                {"", "1234", ""},
                {"hahasce", "", ""}
        });
    }

    @Test
    @DisplayName("Ошибки валидации при создании курьера без обязательных полей в запросе")
    public void errorValidationFieldCreateCourierTest() {

        ValidatableResponse createErrorResponse = courierClient.create(courier);
        int createStatusCode = createErrorResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_BAD_REQUEST, createStatusCode);
        String errorMessage = createErrorResponse.extract().path("message");
        assertEquals("Недостаточно данных для создания учетной записи", errorMessage);

    }
}