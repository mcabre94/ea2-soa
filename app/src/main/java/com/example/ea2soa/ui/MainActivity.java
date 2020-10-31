package com.example.ea2soa.ui;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.example.ea2soa.R;
import com.example.ea2soa.data.RegisterEventService;
import com.example.ea2soa.data.model.LoggedInData;
import com.example.ea2soa.data.model.User;

import java.time.Duration;
import java.time.Instant;

public class MainActivity extends AppCompatActivity implements SensorEventListener {

    private BroadcastReceiver infoBateriaReceiver;
    private TextView nivelBateria;
    private CardView bateriaCardView;
    private Spinner  selectSensor;
    private LinearLayout containerAcelerometro;
    private LinearLayout containerSensorLuz;
    private SensorManager sensorManager;
    private TextView valorAcelX;
    private TextView valorAcelY;
    private TextView valorAcelZ;
    private TextView valorIluminacion;

    private RegisterEventService registerEventService;
    private Long ultimoRegistroEventoAcelerometro = null;
    private Long ultimoRegistroEventoLuz = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("bateria","arranca oncreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER),sensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),sensorManager.SENSOR_DELAY_NORMAL);

        nivelBateria = findViewById(R.id.nivel_bateria);
        bateriaCardView = findViewById(R.id.bateria_card_view);
        selectSensor = findViewById(R.id.sensores);

        //acelerometro
        containerAcelerometro = findViewById(R.id.container_acelerometro);
        valorAcelX = findViewById(R.id.valorX);
        valorAcelY = findViewById(R.id.valorY);
        valorAcelZ = findViewById(R.id.valorZ);

        //sensor luz
        containerSensorLuz = findViewById(R.id.container_sensor_luz);
        valorIluminacion = findViewById(R.id.valorIluminacion);

        registerEventService = new RegisterEventService(this);

        Log.i("MainActivity", LoggedInData.getInstance().getUser().toString());

        setSelectSensorListener();
        setBateriaListener();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterSensors();
    }

    private void setBateriaListener() {
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

    private void setSelectSensorListener() {
        selectSensor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateSensorView(parent.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void updateSensorView(String sensor) {
        //escondo todos los contenedores
        containerAcelerometro.setVisibility(View.GONE);
        containerSensorLuz.setVisibility(View.GONE);

        //chequeo que sensor fue seleccionado
        switch (sensor.toLowerCase()){
            case "acelerómetro":
                containerAcelerometro.setVisibility(View.VISIBLE);
                break;
            case "sensor luz":
                containerSensorLuz.setVisibility(View.VISIBLE);
                break;
        }

    }

    private void unregisterSensors() {
        Log.i("sensores","unregister");
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER));
        sensorManager.unregisterListener(this,sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT));
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        synchronized (this){
            String typeEvent = null;
            String env = getResources().getString(R.string.enviroment);
            String descriptionEvent = null;
            switch (event.sensor.getType()){
                case (Sensor.TYPE_ACCELEROMETER):
//                    Log.i("sensores","acelerometro");
                    Float x = event.values[0];
                    Float y = event.values[1];
                    Float z = event.values[2];

                    valorAcelX.setText(String.format("%.2f m/seg²",x));
                    valorAcelY.setText(String.format("%.2f m/seg²",y));
                    valorAcelZ.setText(String.format("%.2f m/seg²",z));

                    typeEvent = "Sensor acelerómetro";
                    descriptionEvent = String.format("valor x : %.2f m/seg², valor y : %.2f m/seg², valor z : %.2f m/seg²",x,y,z);

                    if(ultimoRegistroEventoAcelerometro == null || System.currentTimeMillis() - ultimoRegistroEventoAcelerometro > 60000){
                        ultimoRegistroEventoAcelerometro = System.currentTimeMillis();
                        Log.i("registroEvento","ya paso un minuto (ACELERÓMETRO)");
                        try {
                            registerEventService.registerEvent(env,typeEvent,descriptionEvent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    break;
                case (Sensor.TYPE_LIGHT):
//                    Log.i("sensores","luz");
                    Float valorLuz = event.values[0];
                    valorIluminacion.setText(String.format("%.2f lx",valorLuz));

                    typeEvent = "Sensor luz";
                    descriptionEvent = String.format("valor iluminacion : %.2f lx",valorLuz);

                    if(ultimoRegistroEventoLuz == null || System.currentTimeMillis() - ultimoRegistroEventoLuz > 60000){
                        ultimoRegistroEventoLuz = System.currentTimeMillis();
                        Log.i("registroEvento","ya paso un minuto (LUZ)");
                        try {
                            registerEventService.registerEvent(env,typeEvent,descriptionEvent);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) { }
}
