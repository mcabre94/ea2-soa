package com.example.ea2soa.data;

import android.os.AsyncTask;
import android.util.Log;

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

public abstract class LoginAsyncTask extends AsyncTask<User, Void, Boolean> {
    String url = "http://so-unlam.net.ar/api/api/login";

    String errorMsg = null;

//    RegisterActivity registerActivity;
//
//    public LoginAsyncTask(RegisterActivity registerActivity){
//        this.registerActivity = registerActivity;
//    }

    @Override
    protected Boolean doInBackground(User[] users) {
        try {
            User user = users[0];
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject data = obtainJsonObject(user);
            Log.i("LoginAsync",data.toString());

            OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data.toString());
            wr.close();

            Log.i("LoginAsync",urlConnection.getHeaderFields().toString());

            urlConnection.connect();

            Log.i("LoginAsync",String.valueOf(urlConnection.getResponseCode()));

            JSONObject response;
            if(urlConnection.getResponseCode() < 400){
                response = obtainResponse(urlConnection.getInputStream());
            }else{
                response = obtainResponse(urlConnection.getErrorStream()); //todo hacer lo mismo en register
            }

            if((Boolean) response.get("success") == true){
                LoggedInData.getInstance().setToken(response.get("token").toString());
                LoggedInData.getInstance().setToken(response.get("token_refresh").toString());
                return true;
            }else{
                errorMsg = response.get("msg").toString();
                return false;
            }
        }catch (Exception e) {
            Log.e("LoginAsync",e.toString());
            e.printStackTrace();

            return false;
        }
    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.i("LoginAsync",result.toString());

        if(result == true){
            onLoggedIn();
        }else{
            onFailedLogin(errorMsg);
        }
    }

    protected abstract void onFailedLogin(String errorMsg);

    protected abstract void onLoggedIn();

    private JSONObject obtainResponse(InputStream stream) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        Log.i("LoginAsync",response.toString());

        JSONObject json = new JSONObject(response.toString());

        Log.i("LoginAsync",json.toString());
        return json;
    }

    private JSONObject obtainJsonObject(User user) throws JSONException {
        JSONObject data = new JSONObject();
        data.put("email",user.getUsername());
        data.put("password",user.getPassword());

        return data;
    }
}
