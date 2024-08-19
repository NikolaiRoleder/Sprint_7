import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;

public class GetOrderListTest {

    private OrderClient orderClient;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
    }

    @Test
    @DisplayName("Получение списка заказов, и проверка что тело ответа не пустое")
    public void canGetOrderListTest() {
        ValidatableResponse orderListResponse = orderClient.getOrderList();
        int createStatusCode = orderListResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, createStatusCode);
        orderListResponse.assertThat().body("orders", notNullValue());
    }
}