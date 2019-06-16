package com.example.login.model;

public class Account {
    String Username;
    String Password;
    String Email;

    public Account(String username, String password, String email) {
        Username = username;
        Password = password;
        Email = email;
    }

    public String getUsername()
    {
        return Username;
    }

    public String getPassword() {
        return Password;
    }

    public String getEmail() {
        return Email;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public void setEmail(String email) {
        Email = email;
    }
}
