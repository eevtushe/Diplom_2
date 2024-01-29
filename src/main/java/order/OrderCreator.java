package order;

import user.LoginUser;
import user.UserActions;
import io.qameta.allure.Step;
import static io.restassured.RestAssured.given;
import io.restassured.response.Response;
import static configuration.Configuration.*;

public class OrderCreator {
    @Step("Создаем новый заказ")
    public static Response newOrder(CreateOrder createOrder) {

        Response response =
                given()
                        .relaxedHTTPSValidation().log().all()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .body(createOrder)
                        .when()
                        .post(ORDERS);
        return response;
    }

    @Step("Получаем заказ")
    public static Response getOrder(String token, boolean useAuth) {
        Response response;
        if (useAuth) {
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .auth().oauth2(token)
                    .when()
                    .get(ORDERS);
        } else {
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .when()
                    .get(ORDERS);
        }
        return response;
    }

    @Step("Получаем заказ пользователя, который авторизован")
    public static Response getOrder(LoginUser loginUser) {
        Response response =
                given()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .auth().oauth2(UserActions.getUserToken(loginUser))
                        .when()
                        .get(ORDERS);
        return response;
    }
}