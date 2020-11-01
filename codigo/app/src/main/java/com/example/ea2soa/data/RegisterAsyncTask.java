package com.example.ea2soa.data;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Button;

import com.example.ea2soa.ui.MainActivity;
import com.example.ea2soa.R;
import com.example.ea2soa.ui.register.RegisterActivity;
import com.example.ea2soa.data.model.LoggedInData;
import com.example.ea2soa.data.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

public abstract class RegisterAsyncTask extends AsyncTask<User, Void, JSONObject> {
    String url = "http://so-unlam.net.ar/api/api/register";
    String env;
    String errorMsg = null;

    public RegisterAsyncTask(String env){
        this.env = env;
    }

    @Override
    protected JSONObject doInBackground(User[] users) {
        try {
            User user = users[0];
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject data = obtainJsonObject(user);

            OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data.toString());
            wr.close();

            urlConnection.connect();

            JSONObject response;
            if(urlConnection.getResponseCode() < 400){
                response = obtainResponse(urlConnection.getInputStream());
            }else{
                response = obtainResponse(urlConnection.getErrorStream());
            }

            return response;
        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onPostExecute(JSONObject response) {
        super.onPostExecute(response);

        try {
            if(response != null && (Boolean) response.get("success") == true){
                onRegistered(response);
            }else if(response != null){
                onFailedRegister(response.get("msg").toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private JSONObject obtainResponse(InputStream stream) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        JSONObject json = new JSONObject(response.toString());
        return json;
    }

    private JSONObject obtainJsonObject(User user) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("env",this.env);
        data.put("name",user.getNombre());
        data.put("lastname",user.getApellido());
        data.put("dni",user.getDni());
        data.put("email",user.getUsername());
        data.put("commission",user.getComision());
        data.put("password",user.getPassword());

        return data;
    }

    protected abstract void onFailedRegister(String errorMsg);

    protected abstract void onRegistered(JSONObject response);
}
