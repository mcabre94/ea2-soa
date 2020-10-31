package com.example.ea2soa.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.example.ea2soa.data.model.LoggedInData;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisterEventService {
    private String endpoint = "http://so-unlam.net.ar/api/api/event";
    private String method = "POST";

    private Context context;
    private BroadcastReceiver receiver = null;

    public RegisterEventService(Context context){
        this.context = context;
    }

    public void registerReceiver(RegisterEventServiceCallbackInterface callback){
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
                    //todo, ver que devuelve una vez vencido el token, hay que enviar la peticion de que refresque el token
                    //todo, y además hay que volver a enviar la request de registrar evento

                    if(callback != null){
                        callback.handle(responseJSON);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        context.registerReceiver(receiver,filtro);
    }

    public void registerEvent(String env, String typeEvent, String description) throws Exception {
        JSONObject json = new JSONObject();
        try{
            json.put("env",env);
            json.put("type_events",typeEvent);
            json.put("description",description);
        }catch (JSONException e){
            throw new Exception("argumentos con formato inválido");
        }

        HashMap<String,String> requestProperties = new HashMap<String,String>();
        requestProperties.put("Authorization","Bearer "+ LoggedInData.getInstance().getToken());

        Intent intent = new Intent(context, HTTPRequestService.class);
        intent.putExtra("jsonData",json.toString());
        intent.putExtra("method",method);
        intent.putExtra("endpoint",endpoint);
        intent.putExtra("requestProperties",requestProperties);

        this.registerReceiver(null);

        context.startService(intent);
    }

    public void unregisterReceiver(Context context) {
        if(receiver != null){
            context.unregisterReceiver(receiver);
        }
    }
}
