package com.example.tubes_kelompok_d;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class FavoritesFragment extends Fragment {
    Button TentangKami,CustomerService;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_favorites, container, false);
        TentangKami = v.findViewById(R.id.tentangKami);

        CustomerService = v.findViewById(R.id.customerService);

        TentangKami.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), AboutUs.class));
            }
        });
        CustomerService.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), CustomerService.class));
            }
        });



        return v;
    }
}