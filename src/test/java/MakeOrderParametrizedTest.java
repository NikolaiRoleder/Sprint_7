import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.ValidatableResponse;
import org.apache.http.HttpStatus;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import pojo.Order;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@RunWith(Parameterized.class)
public class MakeOrderParametrizedTest {

    private OrderClient orderClient;
    private Order order;
    private List<String> color;
    private int trackId;

    @Before
    public void setUp() {
        orderClient = new OrderClient();
        order = OrderData.getRandomOrder(color);
    }

    //После каждого теста вызываем отмену заказа, чтобы очистить тестовые данные. Но в этой ручке баг и из-за этого всегда падает ошибка 400
    @After
    public void cleanUp() {
        orderClient.cancel(trackId);
    }

    public MakeOrderParametrizedTest(List<String> color) {
        this.color = color;
    }

    @Parameterized.Parameters
    public static Collection<Object[]> getData() {
        return Arrays.asList(new Object[][]{
                {List.of("BLACK")},
                {List.of("GREY")},
                {List.of("BLACK", "GREY")},
                {List.of()}
        });
    }

    @Test
    @DisplayName("Проверка возможности создания заказа")
    public void orderCanBeCreatedTest() {
        // Создать заказ
        ValidatableResponse createResponse = orderClient.makeOrder(order);
        int createStatusCode = createResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_CREATED, createStatusCode);

        // Сохранить track из ответа и проверить, что track не пустое
        trackId = createResponse.extract().path("track");
        assertNotNull(trackId);

        //Проверяем, что заказ сохранился, запросив заказ по track (статус код 200 и order не null)
        //Иногда при выгрузке заказа по track выгружается чужой заказ и из-за этого тест падает. Баг при создании заказа, разным заказам присваивается одинаковое значение track
        ValidatableResponse getOrderResponse = orderClient.getOrder(trackId);
        int getOrderStatusCode = getOrderResponse.extract().statusCode();
        assertEquals(HttpStatus.SC_OK, getOrderStatusCode);

        //Проверяем, что цвет в заказе соответствует отправленному в запросе
        List<String> actualColor = getOrderResponse.extract().path("order.color");
        assertEquals(color, actualColor);
    }
}