package user;

import static org.apache.http.HttpStatus.SC_OK;
import static org.apache.http.HttpStatus.SC_UNAUTHORIZED;
import static configuration.Configuration.*;
import static user.UserActions.*;
import static org.hamcrest.Matchers.equalTo;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ChangeUserDataTest {
    private static String token;

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
        loginUser(LOGIN_USER);

    }

    @Test
    @DisplayName("Меняем e-mail авторизованного пользователя")
    @Description("Ожидаем, что вернется код ответа 200")
    public void changeEmailWithLoginUserTest() {
        ChangeUser changeUserEmail = new ChangeUser(EMAIL, CREATE_USER.getName());
        Response response = changeUserData(LOGIN_USER, changeUserEmail, true);
        response.then().statusCode(SC_OK)
                .assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(changeUserEmail.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()));

    }

    @Test
    @DisplayName("Меняем имя авторизованного пользователя")
    @Description("Ожидаем, что вернется код ответа 200")
    public void changeNameWithLoginUserTest() {
        Response response = changeUserData(LOGIN_USER, CHANGE_USER_NAME, true);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(CREATE_USER.getEmail()))
                .and().body("user.name", equalTo(CHANGE_USER_NAME.getName()))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Меняем e-mail неавторизованного пользователя")
    @Description("Ожидаем, что вернется код ответа 401 и сообщение об ошибке")
    public void changeEmailNotLoginUserTest() {
        Response response = changeUserData(LOGIN_USER, CHANGE_USER_EMAIL, false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @Test
    @DisplayName("Меняем имя неавторизованного пользователя")
    @Description("\"Ожидаем, что вернется код ответа 401 и сообщение об ошибке")
    public void changeNameNotLoginTest() {
        Response response = changeUserData(LOGIN_USER, CHANGE_USER_NAME, false);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("You should be authorised"))
                .and()
                .statusCode(SC_UNAUTHORIZED);
    }

    @After
    public void cleanUp() {
        deleteUser(token);
    }
}