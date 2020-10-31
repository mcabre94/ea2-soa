package com.example.ea2soa.data;

import android.app.IntentService;
import android.content.Intent;
import android.content.Context;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class HTTPRequestService extends IntentService {
    private String endpoint;
    private String method;
    private String json;
    private HashMap<String,String> requestProperties;


    public HTTPRequestService() {
        super("HTTPRequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        endpoint = intent.getExtras().getString("endpoint");
        method = intent.getExtras().getString("method");
        json = intent.getExtras().getString("jsonData");
        requestProperties = (HashMap<String, String>) intent.getSerializableExtra("requestProperties");

        Log.i("REGISTEREVENT",requestProperties.toString());

        try{
            JSONObject response = makeRequest();

            Intent responseIntent = new Intent("com.ea2soa.intentservice.intent.action.RESPUESTA_OPERACION");
            responseIntent.putExtra("response",response.toString());

            sendBroadcast(responseIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private JSONObject makeRequest() throws Exception{
        URL url = new URL(endpoint);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setRequestProperty("Content-Type", "application/json");
        urlConnection.setRequestProperty("Accept", "application/json");
        if(requestProperties != null){
            for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }

        urlConnection.setDoOutput(true);

        Log.i("REGISTEREVENT",json);

        OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
        wr.write(json);
        wr.close();

        urlConnection.connect();

        Log.i("REGISTEREVENT",String.valueOf(urlConnection.getResponseCode()));

        JSONObject response;
        if(urlConnection.getResponseCode() < 400){
            response = obtainResponse(urlConnection.getInputStream());
        }else{
            response = obtainResponse(urlConnection.getErrorStream()); //todo hacer lo mismo en register
        }
        Log.i("REGISTEREVENT",response.toString());
        return response;
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
