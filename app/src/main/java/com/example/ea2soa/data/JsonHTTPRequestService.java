package com.example.ea2soa.data;

import android.content.Intent;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class JsonHTTPRequestService extends HTTPRequestService {

    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);

        try{
            HttpURLConnection connection = makeRequest();
            JSONObject response;
            if(connection.getResponseCode() < 400){
                response = obtainResponse(connection.getInputStream());
            }else{
                response = obtainResponse(connection.getErrorStream()); //todo hacer lo mismo en register
            }
            Log.i("REGISTEREVENT",response.toString());

            Intent responseIntent = new Intent("com.ea2soa.intentservice.intent.action.RESPUESTA_OPERACION");
            responseIntent.putExtra("response",response.toString());

            sendBroadcast(responseIntent);
        }catch (Exception e){
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
}
