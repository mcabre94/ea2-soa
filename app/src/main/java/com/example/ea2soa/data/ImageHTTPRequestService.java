package com.example.ea2soa.data;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;

public class ImageHTTPRequestService extends HTTPRequestService {
    @Override
    protected void onHandleIntent(Intent intent) {
        super.onHandleIntent(intent);

        try{
            HttpURLConnection connection = makeRequest();
            Bitmap response;

            if(connection.getResponseCode() < 400){
                response = obtainResponse(connection.getInputStream());
            }else{
                response = obtainResponse(connection.getErrorStream());
            }

            Intent responseIntent = new Intent("com.ea2soa.intentservice.intent.action.RESPUESTA_IMAGEN");
            responseIntent.putExtra("response",convertBitmapToByteArray(response));

            sendBroadcast(responseIntent);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private Bitmap obtainResponse(InputStream stream) {
        return BitmapFactory.decodeStream(stream);
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
