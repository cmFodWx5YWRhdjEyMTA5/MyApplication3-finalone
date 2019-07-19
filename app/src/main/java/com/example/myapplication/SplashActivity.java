package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;

public class SplashActivity extends AppCompatActivity {
    private FirebaseAuth firebaseAuth;
    TinyDB tinyDB;
    int Logged;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);



        firebaseAuth = FirebaseAuth.getInstance();

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                tinyDB = new TinyDB(SplashActivity.this);

                Logged = tinyDB.getInt("Logged");


                if (firebaseAuth.getCurrentUser()!=null && Logged!=1)
                {
                    finish();
                    startActivity(new Intent(SplashActivity.this,MainActivity.class));
                }else {
                    Intent intent = new Intent(SplashActivity.this, WelcomeActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        }, 2000);


    }
}
