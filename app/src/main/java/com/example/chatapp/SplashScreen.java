package com.example.chatapp;

import static java.lang.Thread.sleep;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {

                    sleep(2000);
                    Intent i = new Intent(SplashScreen.this, Login.class);
                    startActivity(i);
                       finish();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    thread.start();
    }
}