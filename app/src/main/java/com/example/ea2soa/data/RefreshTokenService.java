package com.example.ea2soa.data;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.SystemClock;
import android.util.Log;

import com.example.ea2soa.data.model.LoggedInData;

import org.json.JSONObject;

import java.util.HashMap;

public class RefreshTokenService {
    AlarmManager alarmManager;
    //    Long intervaloTiempo = (long) 30000; // 30segundos en milisegundos
    Long intervaloTiempo = (long) (1000 * 60 * 20); // 20 minutos representados en milisegundos
    Context context;
    String endpoint = "http://so-unlam.net.ar/api/api/refresh";
    String method = "PUT";
    BroadcastReceiver receiver;
    PendingIntent pendingIntent;

    public RefreshTokenService(Context context){
        this.context = context;
        alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
    }

    public void startRefreshTokenAlarm(){
        Intent intent = new Intent(context, RefreshTokenReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(context, 1, intent, 0);
        alarmManager.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP,
                SystemClock.elapsedRealtime() + intervaloTiempo,
                intervaloTiempo, pendingIntent);
    }

    public void refreshToken(){
        HashMap<String,String> requestProperties = new HashMap<String,String>();
        requestProperties.put("Authorization","Bearer "+ LoggedInData.getInstance().getRefreshToken());
        requestProperties.put("Content-Type","application/json");
        requestProperties.put("Accept", "application/json");

        Intent requestIntent = new Intent(context, JsonHTTPRequestService.class);
        requestIntent.putExtra("method",method);
        requestIntent.putExtra("endpoint",endpoint);
        requestIntent.putExtra("requestProperties",requestProperties);
        requestIntent.putExtra("broadcastCode","com.ea2soa.intentservice.intent.action.RESPUESTA_REFRESH_TOKEN");

        this.registerReceiver();

        context.startService(requestIntent);
    }

    private void registerReceiver(){
        IntentFilter filtro = new IntentFilter("com.ea2soa.intentservice.intent.action.RESPUESTA_REFRESH_TOKEN");
        filtro.addCategory(Intent.CATEGORY_DEFAULT);
        receiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                String response = intent.getExtras().getString("response");
                try{
                    JSONObject responseJSON = new JSONObject(response);

                    if(responseJSON.getBoolean("success") == true){
                        LoggedInData.getInstance().setToken(responseJSON.getString("token"));
                        LoggedInData.getInstance().setRefreshToken(responseJSON.getString("token_refresh"));
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

            }
        };
        context.registerReceiver(receiver,filtro);
    }

    public void stopRefreshTokenAlarm() {
        alarmManager.cancel(pendingIntent);
    }
}
