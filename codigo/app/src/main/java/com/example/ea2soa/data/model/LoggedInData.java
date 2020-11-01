package com.example.ea2soa.data.model;

public class LoggedInData {
    private String token;
    private String refreshToken;
    private User user;
    public String getToken() {return token;}
    public String getRefreshToken() {return refreshToken;}
    public User getUser() {return user;}
    public void setToken(String token) {this.token = token;}
    public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}
    public void setUser(User user) {this.user = user;}

    private static final LoggedInData loggedInData = new LoggedInData();
    public static LoggedInData getInstance() {return loggedInData;}
}