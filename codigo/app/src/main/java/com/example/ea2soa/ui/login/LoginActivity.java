package com.example.ea2soa.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ea2soa.data.NetworkService;
import com.example.ea2soa.data.RegisterEventService;
import com.example.ea2soa.data.model.LoggedInData;
import com.example.ea2soa.data.model.User;
import com.example.ea2soa.ui.MainActivity;
import com.example.ea2soa.R;
import com.example.ea2soa.ui.register.RegisterActivity;
import com.example.ea2soa.data.LoginService;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {

    private LoginService loginService;
    private User user;
    private EditText usernameEditText;
    private EditText passwordEditText;
    private Button loginButton;
    private TextView errorTextView;
    private Button verificarInternetButton;
    private RegisterEventService registerEventService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        loginButton = findViewById(R.id.login);
        errorTextView = findViewById(R.id.error_en_login);
        verificarInternetButton = findViewById(R.id.verificar_internet);

        loginService = createLoginService();

        registerEventService = createRegisterEventService();

        loginButton.setOnClickListener(v -> {
            loginButton.setEnabled(false);
            user = new User(
                    usernameEditText.getText().toString(),
                    passwordEditText.getText().toString()
            );
            loginService.login(user);
        });

        if(!NetworkService.isOnline(this)){
            loginButton.setEnabled(false);
            verificarInternetButton.setVisibility(View.VISIBLE);
            errorTextView.setText("No posee conexión a internet");

            verificarInternetButton.setOnClickListener(v -> {
                if(NetworkService.isOnline(this)){
                    verificarInternetButton.setVisibility(View.GONE);
                    loginButton.setEnabled(true);
                    errorTextView.setText("");
                }
            });
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        registerEventService.unregisterReceiver(this);
    }

    private RegisterEventService createRegisterEventService() {
        return new RegisterEventService(LoginActivity.this);
    }

    private LoginService createLoginService() {
        return new LoginService() {
            @Override
            protected void onLoggedIn(JSONObject response) {
                super.onLoggedIn(response);

                try {
                    LoggedInData.getInstance().setToken(response.get("token").toString());
                    LoggedInData.getInstance().setRefreshToken(response.get("token_refresh").toString());
                    LoggedInData.getInstance().setUser(user);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                try {
                    registerEventService.registerEvent(getResources().getString(R.string.enviroment),"log in","usuario logueado correctamente");
                } catch (Exception e) {
                    e.printStackTrace();
                }

                goToMainActivity();

            }

            @Override
            protected void onFailedLogin(String errorMsg) {
                super.onFailedLogin(errorMsg);

                loginButton.setEnabled(true);
                errorTextView.setText(errorMsg);
            }
        };
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }

}
