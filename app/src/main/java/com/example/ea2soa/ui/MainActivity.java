package com.example.ea2soa.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.ea2soa.R;
import com.example.ea2soa.data.model.User;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final TextView userText = findViewById(R.id.nombreUsuario);

//        User user = LoginRepository.getInstance(null).getLoggedInUser();

//        if(user != null){
////            userText.setText();
//        }
    }
}
