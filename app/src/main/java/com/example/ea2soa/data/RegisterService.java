package com.example.ea2soa.data;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.ea2soa.data.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterService {

    Context context;
    String url = "http://so-unlam.net.ar/api/api/register";
    String env = "TEST";

    public RegisterService(Context context){
        this.context = context;
    }

    public void registerUser(User user) throws IllegalArgumentException {
//        new RegisterRequest().execute(user);
        RequestQueue queue = Volley.newRequestQueue(this.context);

        JSONObject data = new JSONObject();
        try {
            data.put("env",this.env);
            data.put("name",user.getNombre());
            data.put("lastname",user.getApellido());
            data.put("dni",user.getDni());
            data.put("email",user.getUsername());
            data.put("commission",user.getComision());
            data.put("password",user.getPassword());
        } catch (JSONException e) {
            throw new IllegalArgumentException(e);
        }
        System.out.println(data);
        System.out.println(url);
        // Request a string response from the provided URL.
        JsonRequest jsonRequest = new JsonObjectRequest(
            Request.Method.POST,
            url,
            data,
            new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    System.out.println(response);
                    // Display the first 500 characters of the response string.
//                        textView.setText("Response is: "+ response.substring(0,500));
                }
            },
            new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    System.out.println(error.getCause());
                    //                textView.setText("That didn't work!");
                }
            }
        ){
            @Override
            public Map<String, String> getHeaders() {
                HashMap<String, String> headers = new HashMap<String, String>();
                //headers.put("Content-Type", "application/json");
                headers.put("Content-Type", "application/json");
                return headers;
            }
        };

        queue.add(jsonRequest);
    }
}
