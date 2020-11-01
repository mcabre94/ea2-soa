package com.example.ea2soa.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;

public class ImageHTTPRequestService extends HTTPRequestService {
    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);

        if(!NetworkService.isOnline(this)){
            Intent responseIntent = new Intent("com.ea2soa.intentservice.intent.action.RESPUESTA_IMAGEN");
            responseIntent.putExtra("responseError","sin conexión a internet");
            sendBroadcast(responseIntent);
            return;
        }

        try{
            HttpURLConnection connection = makeRequest();
            Bitmap response;
            Intent responseIntent = new Intent("com.ea2soa.intentservice.intent.action.RESPUESTA_IMAGEN");

            if(connection.getResponseCode() < 400){
                response = obtainResponse(connection.getInputStream());
                responseIntent.putExtra("response",convertBitmapToByteArray(response));
            }else{
                String responseString = obtainStringResponse(connection.getErrorStream());
                responseIntent.putExtra("responseError",responseString);
            }

            sendBroadcast(responseIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Bitmap obtainResponse(InputStream stream) {
        return BitmapFactory.decodeStream(stream);
    }

    private String obtainStringResponse(InputStream stream) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(stream, "utf-8"));
        StringBuilder response = new StringBuilder();
        String responseLine = null;
        while ((responseLine = br.readLine()) != null) {
            response.append(responseLine.trim());
        }
        return response.toString();
    }

    byte[] convertBitmapToByteArray(Bitmap bitmap) {
        ByteArrayOutputStream baos = null;
        try {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            return baos.toByteArray();
        } finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
