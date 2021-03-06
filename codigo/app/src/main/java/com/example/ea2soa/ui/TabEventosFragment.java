package com.example.ea2soa.ui;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.BatteryManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.example.ea2soa.R;
import com.example.ea2soa.data.RegisterEventService;
import com.example.ea2soa.data.model.LoggedInData;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import static android.content.Context.SENSOR_SERVICE;

public class TabEventosFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    TableLayout tabla;
    SwipeRefreshLayout swipeRefreshLayout;
    JSONObject jsonObject;
    View view;
    SharedPreferences sharedPreferences;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.eventos_fragment,container,false);

        tabla = view.findViewById(R.id.tabla_eventos);
        swipeRefreshLayout = view.findViewById(R.id.swipe_refresh_layout);
        swipeRefreshLayout.setOnRefreshListener(this);

        sharedPreferences = getContext().getSharedPreferences("eventos",Context.MODE_PRIVATE);
        updateTable();

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        swipeRefreshLayout.setOnRefreshListener(null);
    }

    public void updateTable() {
        String datosGuardados = sharedPreferences.getString("datosEventosGuardados","{eventos : []}");
        try {
            jsonObject = new JSONObject(datosGuardados);
            JSONArray eventos = jsonObject.getJSONArray("eventos");

            if(eventos!=null && eventos.length()>0){
                tabla.removeViews(1,tabla.getChildCount()-1);
                for (int i = (eventos.length() - 1); i >= 0; i--) {
                    JSONObject evento = eventos.optJSONObject(i);
                    String typeEvent = evento.getString("type_event");
                    String description = evento.getString("description");
                    String hora = evento.getString("hora");

                    TableRow newRow = new TableRow(view.getContext());
                    TextView horaTextView = new TextView(view.getContext());
                    TextView typeEventTextView = new TextView(view.getContext());
                    TextView descriptionTextView = new TextView(view.getContext());
                    horaTextView.setPadding(7,0,7,0);
                    typeEventTextView.setPadding(7,0,7,0);
                    descriptionTextView.setPadding(7,0,7,0);

                    horaTextView.setText(hora);
                    descriptionTextView.setText(description);
                    typeEventTextView.setText(typeEvent);
                    newRow.addView(horaTextView);
                    newRow.addView(typeEventTextView);
                    newRow.addView(descriptionTextView);

                    tabla.addView(newRow);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRefresh() {
        this.updateTable();
        swipeRefreshLayout.setRefreshing(false);
    }
}
