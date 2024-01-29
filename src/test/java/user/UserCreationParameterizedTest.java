package user;

import static org.apache.http.HttpStatus.SC_FORBIDDEN;
import static configuration.Configuration.*;
import static user.UserActions.createNewUser;
import static org.hamcrest.Matchers.equalTo;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

@RunWith(Parameterized.class)
public class UserCreationParameterizedTest {
    private final CreateUser invalidUserCredentials;

    public UserCreationParameterizedTest(CreateUser invalidUserCredentials) {
        this.invalidUserCredentials = invalidUserCredentials;
    }

    @Parameterized.Parameters
    public static Object[][] parametersUserWrongVariable() {
        return new Object[][]{
                {new CreateUser("", PASSWORD, NAME)},
                {new CreateUser(EMAIL, "", NAME)},
                {new CreateUser(EMAIL, PASSWORD, "")},
        };
    }

    @Before
    public void setUp() {
        RestAssured.baseURI = URL;
    }

    @Test
    @DisplayName("Проверяем ввод некорректных данных при регистрации нового пользователя")
    @Description("Ожидаем, что вернется код ответа 403 и сообщение об ошибке")
    public void createWrongUserDataTest() {
        Response response = createNewUser(invalidUserCredentials);
        response.then().assertThat().body("success", equalTo(false))
                .and().body("message", equalTo("Email, password and name are required fields"))
                .and()
                .statusCode(SC_FORBIDDEN);
    }
}