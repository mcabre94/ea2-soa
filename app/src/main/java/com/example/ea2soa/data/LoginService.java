package com.example.ea2soa.data;

import com.example.ea2soa.data.model.User;

import org.json.JSONObject;

public abstract class LoginService {

    public void login(User user){
        LoginAsyncTask loginAsyncTask = new LoginAsyncTask() {
            @Override
            protected void onFailedLogin(String errorMsg) { LoginService.this.onFailedLogin(errorMsg); }

            @Override
            protected void onLoggedIn(JSONObject response) { LoginService.this.onLoggedIn(response); }
        };

        loginAsyncTask.execute(user);
    }

    protected void onLoggedIn(JSONObject response){}
    protected void onFailedLogin(String errorMsg){}
}
