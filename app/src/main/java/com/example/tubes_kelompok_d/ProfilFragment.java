package com.example.tubes_kelompok_d;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.textview.MaterialTextView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class ProfilFragment extends Fragment {

    Button editProfile;
    MaterialTextView logout;

    private DatabaseReference databaseReference;
    private FirebaseAuth mAuth;
    private CircleImageView profilImageView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_profil, container, false);

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");

        final MaterialTextView nama = v.findViewById(R.id.namaView);
        final MaterialTextView email = v.findViewById(R.id.emailView);
        final MaterialTextView phone = v.findViewById(R.id.phoneView);
        logout = v.findViewById(R.id.logout);
        editProfile  = v.findViewById(R.id.btnEditProfile);
        profilImageView = v.findViewById(R.id.imgProfilView);

        nama.setText(GlobalVariableDatabase.user.getNama());
        email.setText(GlobalVariableDatabase.user.getEmail());
        phone.setText(GlobalVariableDatabase.user.getPhone());

        getUserInfo();

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editProfile();
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), Login.class));
            }
        });
        return v;
    }

    public void editProfile(){
        startActivity(new Intent(getActivity(), UpdateProfil.class));
    }

    private void getUserInfo() {
        databaseReference.child(mAuth.getCurrentUser().getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists() && snapshot.getChildrenCount()>0){


                    if(snapshot.hasChild("image")){
                        String image = snapshot.child("image").getValue().toString();
                        Picasso.get().load(image).into(profilImageView);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}
