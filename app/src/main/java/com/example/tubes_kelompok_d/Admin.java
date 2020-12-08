package com.example.tubes_kelompok_d;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class Admin extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new AdminHomeFragment()).commit();
    }
}