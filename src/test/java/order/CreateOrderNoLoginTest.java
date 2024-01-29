package order;

import static org.apache.http.HttpStatus.SC_OK;
import static configuration.Configuration.URL;
import static configuration.Configuration.CREATE_ORDER;
import static order.OrderCreator.newOrder;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;

public class CreateOrderNoLoginTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }

    @Test
    @DisplayName("Проверяем создание заказа без авторизации пользователя")
    @Description("Ожидаем, что вернется код ответа 200")
    public void createOrderNoLoginValidIngredientsTest() {
        Response response = newOrder(CREATE_ORDER);
        response.then().assertThat().body("name", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }
}