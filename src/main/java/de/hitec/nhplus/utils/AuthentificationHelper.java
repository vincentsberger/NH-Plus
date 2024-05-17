package de.hitec.nhplus.utils;

public class AuthentificationHelper {
    private String username;
    private String password;

    public AuthentificationHelper() {
        this.username = "admin";
        this.password = "admin123";
    }

    public boolean isValidLogin(String username, String password) {
        if(username.equals(this.username) && password.equals(this.password)) {
            return true;
        }
        return false;
    }

}
