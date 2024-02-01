package user;

import static org.apache.http.HttpStatus.SC_OK;
import static configuration.Configuration.*;
import static user.UserActions.*;
import static org.hamcrest.Matchers.*;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class UserAuthorizationTest {
    private static String token;

    @Before
    public void setUp() {
        createNewUser(CREATE_USER);
        token = getUserToken(LOGIN_USER);
    }

    @Test
    @DisplayName("Check status code 200")
    @Description("Successful current user login response with body values")
    public void validUserLoginTest() {
        Response response = loginUser(LOGIN_USER);
        response.then().assertThat().body("success", equalTo(true))
                .and().body("accessToken", startsWith("Bearer"))
                .and().body("refreshToken", notNullValue())
                .and().body("user.email", equalTo(LOGIN_USER.getEmail()))
                .and().body("user.name", equalTo(CREATE_USER.getName()))
                .and()
                .statusCode(SC_OK);
    }

    @Test
    @DisplayName("Check status code 401 for name")
    @Description("Invalid user name should return 401 status code")
    public void invalidLoginTest() {
        LoginUser invalidLogin = new LoginUser(faker.internet().emailAddress(), PASSWORD);
        checkWrongCredential(invalidLogin);
    }

    @Test
    @DisplayName("Check status code 401 for password")
    @Description("Invalid password should return 401 status code")
    public void invalidPasswordTest() {
        LoginUser invalidPassword = new LoginUser(EMAIL, faker.internet().password());
        checkWrongCredential(invalidPassword);
    }

    @After
    public void cleanUp() {
        deleteUser(token);
    }
}