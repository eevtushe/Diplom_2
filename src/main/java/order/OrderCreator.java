package order;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import user.LoginUser;
import user.UserActions;

import static io.restassured.RestAssured.given;
import static configuration.Configuration.*;

public class OrderCreator {

    @Step("Создаем новый заказ")
    public static Response newOrder(CreateOrder createOrder) {
        return given()
                .relaxedHTTPSValidation().log().all()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .body(createOrder)
                .when()
                .post(ORDERS);
    }

    @Step("Получаем заказ")
    public static Response getOrder(String token) {
        return given()
                .relaxedHTTPSValidation()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .auth().oauth2(token)
                .when()
                .get(ORDERS);
    }

    @Step("Получаем заказ пользователя, который авторизован")
    public static Response getOrder(LoginUser loginUser) {
        return given()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .auth().oauth2(UserActions.getUserToken(loginUser))
                .when()
                .get(ORDERS);
    }
}