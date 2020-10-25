package com.example.ea2soa.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterEventService {
    private String endpoint = "http://so-unlam.net.ar/api/api/event";
    private String method = "POST";

    private Context context;
    private BroadcastReceiver receiver = null;

    public RegisterEventService(Context context){
        this.context = context;
    }

    public void registerReceiver(RegisterServiceCallbackInterface callback){
        if(receiver != null){
            context.unregisterReceiver(receiver);
        }
        IntentFilter filtro = new IntentFilter("com.ea2soa.intentservice.intent.action.RESPUESTA_OPERACION");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String response = intent.getExtras().getString("response");
                try{
                    JSONObject responseJSON = new JSONObject(response);

                    Log.i("REGISTEREVENT",response);

                    callback.handle(responseJSON);

                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        context.registerReceiver(receiver,filtro);
    }

    public void registerEvent(String env, String typeEvent, String description) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("env",env);
        json.put("type_events",typeEvent);
        json.put("description",description);

        Intent intent = new Intent(context, HTTPRequestService.class);
        intent.putExtra("jsonData",json.toString());
        intent.putExtra("method",method);
        intent.putExtra("endpoint",endpoint);

        Log.i("REGISTEREVENT","antes de mandar");

        context.startService(intent);
    }

    public void unregisterReceiver(Context context) {
        if(receiver != null){
            context.unregisterReceiver(receiver);
        }
    }
}
