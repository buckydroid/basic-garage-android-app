package com.sahil.garage;

public class UserHolder {
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;

    private static final UserHolder holder = new UserHolder();
    public static UserHolder getInstance() {return holder;}
}