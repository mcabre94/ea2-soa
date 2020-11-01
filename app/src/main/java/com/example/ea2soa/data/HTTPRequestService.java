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
public abstract class HTTPRequestService extends IntentService {
    protected String endpoint;
    protected String method;
    protected String json;
    protected HashMap<String,String> requestProperties;
    protected String broadcastCode = "com.ea2soa.intentservice.intent.action.RESPUESTA_OPERACION";


    public HTTPRequestService() {
        super("HTTPRequestService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        endpoint = intent.getExtras().getString("endpoint");
        method = intent.getExtras().getString("method");
        json = intent.getExtras().getString("jsonData");
        requestProperties = (HashMap<String, String>) intent.getSerializableExtra("requestProperties");
        String broadcastCode = intent.getExtras().getString("broadcastCode");
        if(broadcastCode != null){
            this.broadcastCode = broadcastCode;
        }
    }

    protected HttpURLConnection makeRequest() throws Exception{
        URL url = new URL(endpoint);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        if(requestProperties != null){
            for (Map.Entry<String, String> entry : requestProperties.entrySet()) {
                urlConnection.setRequestProperty(entry.getKey(),entry.getValue());
            }
        }

        if(json != null){
            urlConnection.setDoOutput(true);

            Log.i("REGISTEREVENT",json);

            OutputStreamWriter wr= new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write(json);
            wr.close();
        }


        urlConnection.connect();

        Log.i("REGISTEREVENT",String.valueOf(urlConnection.getResponseCode()));

        return urlConnection;
    }
}
