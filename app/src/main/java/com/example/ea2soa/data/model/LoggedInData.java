package com.example.ea2soa.data.model;

public class LoggedInData {
    private String token;
    private String refreshToken;
    public String getToken() {return token;}
    public String getRefreshToken() {return refreshToken;}
    public void setToken(String token) {this.token = token;}
    public void setRefreshToken(String refreshToken) {this.refreshToken = refreshToken;}

    private static final LoggedInData loggedInData = new LoggedInData();
    public static LoggedInData getInstance() {return loggedInData;}
}