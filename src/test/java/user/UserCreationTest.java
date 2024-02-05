package user;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static org.apache.http.HttpStatus.SC_OK;
import static configuration.Configuration.*;
import static user.UserActions.createNewUser;
import static user.UserActions.deleteUser;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Test;

public class UserCreationTest {

    @Test
    @DisplayName("Проверяем регистрацию нового пользователя")
    @Description("Ожидаем, что вернется код ответа 200")
    public void newUserCreationTest() {
        Response response = createNewUser(CREATE_USER);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("user.email", equalTo(CREATE_USER.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and().body("accessToken", startsWith("Bearer"))
                .and().body("refreshToken", notNullValue())
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Проверяем регистрацию пользователя с существующим именем ")
    @Description("Ожидаем, что вернется код ответа 403 и сообщение об ошибке")
    public void existingUserCreationTest() {
        createNewUser(CREATE_USER);
        Response response = createNewUser(CREATE_USER);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("User already exists"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }

    @After
    public void cleanUp() {
        deleteUser(LOGIN_USER);
    }
}