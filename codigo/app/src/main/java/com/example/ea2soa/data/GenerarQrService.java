package com.example.ea2soa.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.net.URLEncoder;

public class GenerarQrService {
    private Context context;
    private String method = "GET";
    private String endpoint = "https://api.qrserver.com/v1/create-qr-code/";

    private BroadcastReceiver receiver;

    public GenerarQrService(Context context){
        this.context = context;
    }

    public void registerReceiver(GenerarQrServiceCallbackInterface callback){
        if(receiver != null){
            context.unregisterReceiver(receiver);
        }
        IntentFilter filtro = new IntentFilter("com.ea2soa.intentservice.intent.action.RESPUESTA_IMAGEN");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                byte[] byteArray = intent.getByteArrayExtra("response");
                if(byteArray != null){
                    Bitmap qr = BitmapFactory.decodeByteArray(byteArray,0,byteArray.length);
                    callback.handle(qr);
                }
                callback.handle(null);
                context.unregisterReceiver(receiver);
            }
        };
        context.registerReceiver(receiver,filtro);
    }

    public void generarQR(String text){
        Intent intent = new Intent(context, ImageHTTPRequestService.class);
        String finalEndpoint = endpoint;
        finalEndpoint += "?";
        finalEndpoint += String.format("data=%s", URLEncoder.encode(text));
        finalEndpoint += "&size=500x500";

        intent.putExtra("method",method);
        intent.putExtra("endpoint",finalEndpoint);

        context.startService(intent);
    }
}
