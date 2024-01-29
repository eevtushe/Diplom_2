package configuration;

import com.github.javafaker.Faker;
import user.ChangeUser;
import order.CreateOrder;
import user.CreateUser;
import user.LoginUser;

public class Configuration {

    //URL
    public final static String URL = "https://stellarburgers.nomoreparties.site";

    //USER
    public final static String USER_AUTH = "/api/auth/login ";
    public final static String USER_CREATE = "/api/auth/register";
    public final static String USER_CHANGE = "/api/auth/user";
    public final static String USER_DELETE = "/api/auth/user";

    //ORDERS
    public final static String ORDERS = "/api/orders";

    //OTHER
    public final static String HEADER_CONTENT_TYPE_NAME = "Content-type";
    public final static String CONTENT_TYPE = "application/json";

    //FAKER
    public static Faker faker = new Faker();

    //USER DATA FAKER
    public final static String NAME = faker.name().username();
    public final static String EMAIL =  faker.internet().emailAddress();
    public final static String PASSWORD = faker.internet().password();
    public final static CreateUser CREATE_USER = new CreateUser(EMAIL,PASSWORD,NAME);
    public final static LoginUser LOGIN_USER = new LoginUser(EMAIL,PASSWORD);
    public final static ChangeUser CHANGE_USER_NAME = new ChangeUser(CREATE_USER.getEmail(), faker.name().fullName());
    public final static ChangeUser CHANGE_USER_EMAIL = new ChangeUser(EMAIL, CREATE_USER.getName());

    //ORDER DATA FAKER
    public final static CreateOrder CREATE_ORDER = new CreateOrder(new String[]{"61c0c5a71d1f82001bdaaa6d","61c0c5a71d1f82001bdaaa6c"});
    public final static CreateOrder INVALID_INGREDIENT_ORDER = new CreateOrder(new String[]{"invalid23n0c5a71d1f83565bdaaa6d",});
    public final static CreateOrder EMPTY_ORDER = new CreateOrder(new String[]{});
}