package com.example.forgecareer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Button btnOverride = findViewById(R.id.buttonOverride);
        btnOverride.setOnClickListener(item -> {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        });

        Button btnFirebaseTest = findViewById(R.id.buttonFirebaseTest);
        btnFirebaseTest.setOnClickListener(item -> {
            Intent intent = new Intent(this, FirebaseActivityTemp.class);
            startActivity(intent);
        });
    }
}