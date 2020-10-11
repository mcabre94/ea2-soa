package com.example.ea2soa;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ea2soa.data.LoginRepository;
import com.example.ea2soa.data.model.LoggedInUser;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView userText = findViewById(R.id.nombreUsuario);

        LoggedInUser loggedInUser = LoginRepository.getInstance(null).getLoggedInUser();

        if(loggedInUser != null){
            userText.setText(loggedInUser.getDisplayName());
        }
    }
}
