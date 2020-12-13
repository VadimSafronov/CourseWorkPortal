package com.safronov.courseworksapp.Models;

public class User {

    public int id;
    public String username;
    public String password;
    public String role;
    public String token;

    public User(String username, String password, String role) {
        this.username = username;
        this.password = password;
        this.role = role;
    }
}
