package user;

import io.qameta.allure.Step;
import io.restassured.response.Response;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static configuration.Configuration.*;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

public class UserActions {
    @Step("Создаем нового пользователя")
    public static Response createNewUser(CreateUser createUser) {
        Response response =
                given()
                        .relaxedHTTPSValidation()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .body(createUser)
                        .when()
                        .post(USER_CREATE);
        return response;
    }

    @Step("Проверяем данные пользователя")
    public static void checkWrongCredential(LoginUser credential) {
        Response response = loginUser(credential);
        response.then().assertThat().body("success", equalTo(false))
                .body("success", notNullValue())
                .and().body("message", equalTo("email or password are incorrect"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Step("Получаем токен пользователя")
    public static String getUserToken(LoginUser loginUser) {
        Response response = loginUser(loginUser);
        String accessToken = response.jsonPath().get("accessToken");
        return accessToken.replace("Bearer ", "");
    }

    @Step("Авторизуем пользователя")
    public static Response loginUser(LoginUser loginUser) {
        return
                given()
                        .relaxedHTTPSValidation()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .body(loginUser)
                        .when()
                        .post(USER_AUTH);
    }

    @Step("Вносим изменения в профиль пользователя")
    public static Response changeUserData(LoginUser userBefore, ChangeUser changeUser, boolean useAuth) {
        String token;
        Response response;
        if (useAuth) {
            token = getUserToken(userBefore);
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .auth().oauth2(token)
                    .body(changeUser)
                    .when()
                    .patch(USER_CHANGE);
        } else {
            response = given()
                    .relaxedHTTPSValidation()
                    .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                    .body(changeUser)
                    .when()
                    .patch(USER_CHANGE);
        }
        return response;
    }

    @Step("Удаляем пользователя")
    public static Response deleteUser(String token) {
        Response response = given()
                .relaxedHTTPSValidation()
                .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                .auth().oauth2(token)
                .when()
                .delete(USER_DELETE);
        return response;
    }

    @Step("Удаляем авторизованного пользователя")
    public static Response deleteUser(LoginUser loginUser) {
        Response response =
                given()
                        .relaxedHTTPSValidation()
                        .header(HEADER_CONTENT_TYPE_NAME, CONTENT_TYPE)
                        .auth().oauth2(getUserToken(loginUser))
                        .when()
                        .delete(USER_DELETE);
        return response;
    }
}