package com.example.ea2soa.data;

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

public abstract class RegisterAsyncTask extends AsyncTask<User, Void, Boolean> {
    String url = "http://so-unlam.net.ar/api/api/register";
    String env = "TEST";
    String errorMsg = null;
//
//    RegisterActivity registerActivity;
//
//    public RegisterAsyncTask(RegisterActivity registerActivity){
//        this.registerActivity = registerActivity;
//    }

    @Override
    protected Boolean doInBackground(User[] users) {
        try {
            User user = users[0];
            URL url = new URL(this.url);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
            urlConnection.setRequestProperty("Accept", "application/json");
            urlConnection.setDoOutput(true);

            JSONObject data = obtainJsonObject(user);
            Log.i("registerAsync",data.toString());

            OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(data.toString());
            wr.close();

            Log.i("registerAsync",urlConnection.getHeaderFields().toString());
//            Log.i("registerAsync",urlConnection.getContent().toString());

            urlConnection.connect();

//            String response = urlConnection.getResponseMessage();
            Log.i("registerAsync",String.valueOf(urlConnection.getResponseCode()));

            JSONObject response;
            if(urlConnection.getResponseCode() < 400){
                response = obtainResponse(urlConnection.getInputStream());
            }else{
                response = obtainResponse(urlConnection.getErrorStream());
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
            Log.e("registerAsync",e.toString());
            e.printStackTrace();

            return false;
        }
    }

//    @Override
//    protected void onPreExecute() {
//        super.onPreExecute();
//
//        Button registerButton = (Button) registerActivity.findViewById(R.id.register);
//        registerButton.setEnabled(false);
//    }

    @Override
    protected void onPostExecute(Boolean result) {
        super.onPostExecute(result);
        Log.i("registerAsync",result.toString());

        if(result == true){
            onRegistered();
        }else{
            onFailedRegister(errorMsg);
        }
    }

    protected abstract void onFailedRegister(String errorMsg);

    protected abstract void onRegistered();

    private JSONObject obtainResponse(InputStream stream) throws IOException, JSONException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }

        JSONObject json = new JSONObject(response.toString());

        Log.i("registerAsync",json.toString());
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
}
