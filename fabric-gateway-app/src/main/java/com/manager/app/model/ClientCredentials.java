package com.manager.app.model;

public class ClientCredentials {
    public String email;
    public String password;

    public ClientCredentials(String emailId, String password) {
        this.email = emailId;
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
