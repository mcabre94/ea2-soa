package com.example.ea2soa;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.example.ea2soa.data.RegisterService;
import com.example.ea2soa.data.model.User;
import com.example.ea2soa.ui.login.LoginActivity;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    public void goBackToLoginActivity(View view) {
        finish();
    }

    public void register(View view){
        System.out.println("llegue aca");
        nombre = (EditText)findViewById(R.id.nombre);
        apellido = (EditText)findViewById(R.id.apellido);
        dni = (EditText)findViewById(R.id.dni);
        comision = (EditText)findViewById(R.id.comision);
        username = (EditText)findViewById(R.id.username);
        password = (EditText)findViewById(R.id.password);
        password_repeat = (EditText)findViewById(R.id.password_repeat);
        System.out.println("llegue aca2");
        cleanErrors();

//        todo ver esto
//        if (dni.getText().toString().isEmpty()) {
//            dni.setError("Debe completar campo dni");
//            return;
//        }
//        if (comision.getText().toString().isEmpty()) {
//            comision.setError("Debe completar campo comision");
//            return;
//        }

        User user = new User(
            nombre.getText().toString(),
            apellido.getText().toString(),
            dni.getText().toString(),
            comision.getText().toString(),
            username.getText().toString(),
            password.getText().toString(),
            password_repeat.getText().toString()
        );
        System.out.println("aca toy");
        if(!user.isValid()){
            showErrors(user.getErrors());
            return;
        }

        RegisterService registerService = new RegisterService(this);
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
    }

    private void showErrors(HashMap<String,String> errors){
        System.out.println("show errors");
        Iterator it = errors.entrySet().iterator();
        boolean alreadyFocusSetted = false;
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry)it.next();
            System.out.println(pair.getKey() + " = " + pair.getValue());
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
