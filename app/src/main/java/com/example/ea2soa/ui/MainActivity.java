package com.example.ea2soa.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.example.ea2soa.R;
import com.example.ea2soa.data.model.LoggedInData;
import com.example.ea2soa.data.model.User;

public class MainActivity extends AppCompatActivity {

    private BroadcastReceiver infoBateriaReceiver;
    private TextView nivelBateria;
    private CardView bateriaCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("bateria","arranca oncreate");
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        nivelBateria = findViewById(R.id.nivel_bateria);
        bateriaCardView = findViewById(R.id.bateria_card_view);

        Log.i("MainActivity", LoggedInData.getInstance().getUser().toString());

        infoBateriaReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Log.i("bateria",String.valueOf(intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0)));
                Integer bateria = intent.getIntExtra(BatteryManager.EXTRA_LEVEL,0);
                nivelBateria.setText(String.valueOf(bateria).concat("%"));

                if(bateria < 30){
                    bateriaCardView.setCardBackgroundColor(Color.RED);
                }else if (bateria < 50){
                    bateriaCardView.setCardBackgroundColor(Color.YELLOW);
                }else{
                    bateriaCardView.setCardBackgroundColor(Color.GREEN);
                }
            }
        };
        this.registerReceiver(infoBateriaReceiver,new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    }
}
