package order;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static configuration.Configuration.*;
import static order.OrderCreator.newOrder;
import static user.UserActions.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Test;

public class GetOrdersTest {
    private static String token;

    @Test
    @DisplayName("Проверяем авторизацию корректного пользователя")
    @Description("Ожидаем, что вернется код ответа 200 и список заказов")
    public void getCorrectUserOrdersTest() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        newOrder(CREATE_ORDER);
        Response response = OrderCreator.getOrder(token);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("orders", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Проверяем авторизацию некорректного пользователя")
    @Description("Ожидаем, что вернется код ответа 401 и сообщение об ошибке")
    public void getWrongUserTest() {
        Response response = OrderCreator.getOrder(token);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @AfterClass
    public static void cleanUp() {
        deleteUser(token);
    }
}