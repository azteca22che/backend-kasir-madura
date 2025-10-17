package com.example.demo.dto;

public class LoginRequest {
    private String username;
    private String password;

    public LoginRequest() {
        // wajib untuk deserialisasi JSON ke objek
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
