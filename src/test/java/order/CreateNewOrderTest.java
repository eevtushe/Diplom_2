package order;

import static org.apache.http.HttpStatus.*;
import static configuration.Configuration.*;
import static order.OrderCreator.newOrder;
import static user.UserActions.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;

public class CreateNewOrderTest {
    private static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
    }

    @Test
    @DisplayName("Проверяем создание заказа с корректными ингредиентами и авторизованным пользователем")
    @Description("Ожидаем, что вернется код ответа 200")
    public void createValidOrderTest() {
        Response response = newOrder(CREATE_ORDER);
        response.then().log().all().assertThat().body("name", notNullValue())
                .and().body("order.number", notNullValue())
                .and().body("success", equalTo(true))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Проверяем создание заказа с пустыми ингредиентами и авторизованным пользователем")
    @Description("Ожидаем, что вернется код ответа 400 и сообщение об ошибке")
    public void createOrderLoginTrueEmptyIngredientsTest() {

        CreateOrder createOrder = new CreateOrder(new String[]{});
        Response response = newOrder(createOrder);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверяем создание заказа с пустыми ингредиентами и неавторизованным пользователем")
    @Description("Ожидаем, что вернется код ответа 400 и сообщение об ошибке")
    public void createOrderLoginFalseNoIngredientsTest() {
        Response response = newOrder(EMPTY_ORDER);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Ingredient ids must be provided"))
                .and()
                .statusCode(SC_BAD_REQUEST);
    }

    @Test
    @DisplayName("Проверяем создание заказа с некорректными ингредиентами и авторизованным пользователем")
    @Description("Ожидаем, что вернется код ответа 500")
    public void createOrderLoginTrueWrongIngredientsTest() {
        Response response = newOrder(INVALID_INGREDIENT_ORDER);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @Test
    @DisplayName("Проверяем создание заказа с некорректными ингредиентами и неавторизованным пользователем")
    @Description("Ожидаем, что вернется код ответа 500")
    public void createOrderLoginFalseWrongIngredientsTest() {
        Response response = newOrder(INVALID_INGREDIENT_ORDER);
        response.then().assertThat().statusCode(SC_INTERNAL_SERVER_ERROR);
    }

    @AfterClass
    public static void cleanUp() {
        deleteUser(token);
    }
}