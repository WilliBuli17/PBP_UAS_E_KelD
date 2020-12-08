package com.example.tubes_kelompok_d;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

public class MainActivity extends AppCompatActivity {
    Theme theme;

    private static int SPLASH_TIME_OUT = 3000;

    protected void onCreate(Bundle savedInstanceState) {
        theme = new Theme(this);
        if(theme.loadNightModeState()==true){
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent splash = new Intent(MainActivity.this, Login.class);
                startActivity(splash);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}