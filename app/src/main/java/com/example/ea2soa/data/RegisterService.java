package com.example.ea2soa.data;

//import com.android.volley.Request;
//import com.android.volley.RequestQueue;
//import com.android.volley.Response;
//import com.android.volley.VolleyError;
//import com.android.volley.toolbox.JsonObjectRequest;
//import com.android.volley.toolbox.JsonRequest;
//import com.android.volley.toolbox.Volley;
import com.example.ea2soa.ui.register.RegisterActivity;
import com.example.ea2soa.data.model.User;

public class RegisterService {

    RegisterActivity registerActivity;

    public RegisterService(RegisterActivity activity){
        this.registerActivity = activity;
    }

    public void registerUser(User user) throws IllegalArgumentException {
        RegisterAsyncTask asyncTask = new RegisterAsyncTask() {
            @Override
            protected void onFailedRegister(String errorMsg) { RegisterService.this.onFailedRegister(errorMsg); }

            @Override
            protected void onRegistered() { RegisterService.this.onRegistered(); }
        };
        asyncTask.execute(user);
    }


    protected void onRegistered(){}
    protected void onFailedRegister(String errorMsg){}
}
