package com.example.ea2soa.data;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.JsonRequest;
//import com.android.volley.toolbox.Volley;
import com.example.ea2soa.R;
import com.example.ea2soa.ui.register.RegisterActivity;
import com.example.ea2soa.data.model.User;

import org.json.JSONObject;

public class RegisterService {

    RegisterActivity registerActivity;

    public RegisterService(RegisterActivity activity){
        this.registerActivity = activity;
    }

    public void registerUser(User user) throws IllegalArgumentException {
        RegisterAsyncTask asyncTask = new RegisterAsyncTask(registerActivity.getResources().getString(R.string.enviroment)) {
            @Override
            protected void onFailedRegister(String errorMsg) { RegisterService.this.onFailedRegister(errorMsg); }

            @Override
            protected void onRegistered(JSONObject response) { RegisterService.this.onRegistered(response); }
        };
        asyncTask.execute(user);
    }


    protected void onRegistered(JSONObject response){}
    protected void onFailedRegister(String errorMsg){}
}
