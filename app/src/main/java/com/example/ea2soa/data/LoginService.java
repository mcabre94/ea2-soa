package com.example.ea2soa.data;

import com.example.ea2soa.data.model.User;

public abstract class LoginService {

    public void login(String user, String password){
        LoginAsyncTask loginAsyncTask = new LoginAsyncTask() {
            @Override
            protected void onFailedLogin(String errorMsg) { LoginService.this.onFailedLogin(errorMsg); }

            @Override
            protected void onLoggedIn() {
                 LoginService.this.onLoggedIn();
            }
        };

        loginAsyncTask.execute(new User(user,password));
    }

    protected void onLoggedIn(){}
    protected void onFailedLogin(String errorMsg){}
}
