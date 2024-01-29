package user;

public class ChangeUser {
    private final String email;
    private final String name;

    public ChangeUser(String email, String name) {
        this.email = email;
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }
}