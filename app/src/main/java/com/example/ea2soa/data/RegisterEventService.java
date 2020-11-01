package com.example.ea2soa.data;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.ea2soa.data.model.LoggedInData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
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
                if(response != null){
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
        requestProperties.put("Content-Type","application/json");
        requestProperties.put("Accept", "application/json");

        Intent intent = new Intent(context, JsonHTTPRequestService.class);
        intent.putExtra("jsonData",json.toString());
        intent.putExtra("method",method);
        intent.putExtra("endpoint",endpoint);
        intent.putExtra("requestProperties",requestProperties);

        this.registerReceiver(null);

        context.startService(intent);

        this.registerEventInSharedPreferences(typeEvent,description);

    }

    public void unregisterReceiver(Context context) {
        if(receiver != null){
            context.unregisterReceiver(receiver);
        }
    }

    private void registerEventInSharedPreferences(String typeEvent,String description) throws JSONException {
        JSONObject sharedPreferencesData = new JSONObject();
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formatted = format1.format(Calendar.getInstance().getTime());
        sharedPreferencesData.put("hora", formatted);
        sharedPreferencesData.put("type_event", typeEvent);
        sharedPreferencesData.put("description", description);

        SharedPreferences sharedPref = context.getApplicationContext().getSharedPreferences("eventos",Context.MODE_PRIVATE);
        String datosGuardados = sharedPref.getString("datosEventosGuardados","{eventos : []}");
        JSONObject datosGuardadosJson = new JSONObject(datosGuardados);
        JSONArray eventos = datosGuardadosJson.getJSONArray("eventos");
        eventos.put(sharedPreferencesData);

        datosGuardadosJson.put("eventos",eventos);

        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("datosEventosGuardados", datosGuardadosJson.toString());
        editor.commit();
    }
}
