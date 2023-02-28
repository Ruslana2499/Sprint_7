package common.entities;

public class CourierAuth {

    private String login;
    private String password;

    public CourierAuth(Courier courier) {
        this.login = courier.getLogin();
        this.password = courier.getPassword();
    }

    public CourierAuth(String login, String password) {
        this.login = login;
        this.password = password;
    }

    public CourierAuth() {
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
