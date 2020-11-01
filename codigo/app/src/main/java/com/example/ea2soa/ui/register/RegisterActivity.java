package com.example.ea2soa.ui.register;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ea2soa.R;
import com.example.ea2soa.data.NetworkService;
import com.example.ea2soa.data.RegisterEventService;
import com.example.ea2soa.data.RegisterService;
import com.example.ea2soa.data.model.LoggedInData;
import com.example.ea2soa.data.model.User;
import com.example.ea2soa.ui.MainActivity;
import com.example.ea2soa.ui.login.LoginActivity;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    private EditText nombre;
    private EditText apellido;
    private EditText dni;
    private EditText comision;
    private EditText username;
    private EditText password;
    private EditText password_repeat;
    private TextView error_en_register;
    private Button registerButton;
    private Button verificarInternetButton;

    private RegisterEventService registerEventService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        registerEventService = new RegisterEventService(this);
        nombre = (EditText)findViewById(R.id.nombre);
        apellido = (EditText)findViewById(R.id.apellido);
        dni = (EditText)findViewById(R.id.dni);
        comision = (EditText)findViewById(R.id.comision);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        password_repeat = (EditText)findViewById(R.id.password_repeat);
        error_en_register = (TextView) findViewById(R.id.error_en_register);
        registerButton = (Button) findViewById(R.id.register);
        verificarInternetButton = (Button) findViewById(R.id.verificar_internet);

        if(!NetworkService.isOnline(this)){
            registerButton.setEnabled(false);
            verificarInternetButton.setVisibility(View.VISIBLE);
            error_en_register.setText("No posee conexión a internet");

            verificarInternetButton.setOnClickListener(v -> {
                if(NetworkService.isOnline(this)){
                    verificarInternetButton.setVisibility(View.GONE);
                    registerButton.setEnabled(true);
                    error_en_register.setText("");
                }
            });
        }
    }

    public void goBackToLoginActivity(View view) {
        finish();
    }

    public void register(View view){
        cleanErrors();

        final User user = new User(
            nombre.getText().toString(),
            apellido.getText().toString(),
            dni.getText().toString(),
            comision.getText().toString(),
            username.getText().toString(),
            password.getText().toString(),
            password_repeat.getText().toString()
        );
        if(!user.isValid()){
            showErrors(user.getErrors());
            return;
        }

        registerButton.setEnabled(false);
        RegisterService registerService = new RegisterService(this){
            @Override
            protected void onFailedRegister(String errorMsg) {
                super.onFailedRegister(errorMsg);

                error_en_register.setText(errorMsg);
                registerButton.setEnabled(true);
            }

            @Override
            protected void onRegistered(JSONObject response) {
                super.onRegistered(response);

                try {
                    LoggedInData.getInstance().setToken(response.get("token").toString());
                    LoggedInData.getInstance().setRefreshToken(response.get("token_refresh").toString());
                    LoggedInData.getInstance().setUser(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    registerEventService.registerEvent(getResources().getString(R.string.enviroment),"register","Se registró un nuevo usuario correctamente");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                Intent intent = new Intent(RegisterActivity.this, MainActivity.class);

                startActivity(intent);
                finish();
            }
        };
        registerService.registerUser(user);

    }

    private void cleanErrors() {
        nombre.setError(null);
        apellido.setError(null);
        dni.setError(null);
        comision.setError(null);
        username.setError(null);
        password.setError(null);
        password_repeat.setError(null);
        error_en_register.setText("");
    }

    private void showErrors(HashMap<String,String> errors){
        Iterator it = errors.entrySet().iterator();
        boolean alreadyFocusSetted = false;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            int resID = getResources().getIdentifier(pair.getKey().toString(), "id", getPackageName());
            EditText editText = (EditText) findViewById(resID);
            editText.setError(pair.getValue().toString());
            if(alreadyFocusSetted == false){
                editText.requestFocus();
                alreadyFocusSetted = true;
            }
            it.remove(); // avoids a ConcurrentModificationException
        }
    }
}
