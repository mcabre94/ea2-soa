package com.example.ea2soa.ui.login;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.ea2soa.ui.MainActivity;
import com.example.ea2soa.R;
import com.example.ea2soa.ui.register.RegisterActivity;
import com.example.ea2soa.data.LoginService;

public class LoginActivity extends AppCompatActivity {

    private LoginService loginService;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final EditText usernameEditText = findViewById(R.id.username);
        final EditText passwordEditText = findViewById(R.id.password);
        final Button loginButton = findViewById(R.id.login);
        final TextView errorTextView = findViewById(R.id.error_en_login);

        loginService = new LoginService() {
            @Override
            protected void onLoggedIn() {
                super.onLoggedIn();

                Log.i("Login","logged in");
                goToMainActivity();
            }

            @Override
            protected void onFailedLogin(String errorMsg) {
                super.onFailedLogin(errorMsg);

                Log.i("Login","Fail log in");

                loginButton.setEnabled(true);
                errorTextView.setText(errorMsg);
//                todo : mostrar mensaje de error
            }
        };
//        passwordEditText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
//
//            @Override
//            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
//                if (actionId == EditorInfo.IME_ACTION_DONE) {
//                    loginViewModel.login(
//                            usernameEditText.getText().toString(),
//                            passwordEditText.getText().toString()
//                    );
//                }
//                return false;
//            }
//        });

        loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                loginButton.setEnabled(false);
//                loadingProgressBar.setVisibility(View.VISIBLE);
                loginService.login(
                        usernameEditText.getText().toString(),
                        passwordEditText.getText().toString()
                );
            }
        });
    }

    private void goToMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public void goToRegisterActivity(View view) {
        Intent intent = new Intent(this, RegisterActivity.class);
        startActivity(intent);
    }
}
