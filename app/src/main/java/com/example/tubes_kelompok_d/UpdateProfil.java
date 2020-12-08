package com.example.tubes_kelompok_d;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.tubes_kelompok_d.databaseUser.UserHelperDatabaseClass;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.HashMap;

import de.hdodenhof.circleimageview.CircleImageView;

public class UpdateProfil extends AppCompatActivity {

    Button updateProfil;
    TextInputEditText nama;
    TextInputEditText phone;

    private DatabaseReference databaseReference, up;
    private FirebaseAuth mAuth;
    private CircleImageView profilImageView;
    private Uri imgUri;
    private String myUri = "";
    private StorageTask uploadTask;
    private StorageReference storageReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_profil);

        nama = findViewById(R.id.updateNamaView);
        phone = findViewById(R.id.updatePhoneView);
        nama.setText(GlobalVariableDatabase.user.getNama());
        phone.setText(GlobalVariableDatabase.user.getPhone());

        mAuth = FirebaseAuth.getInstance();
        databaseReference = FirebaseDatabase.getInstance().getReference("user");
        storageReference = FirebaseStorage.getInstance().getReference().child("ProfilPic");

        profilImageView = findViewById(R.id.imgProfilView);
        updateProfil  = findViewById(R.id.btnUpdateProfile);

        profilImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chosePicture();
            }
        });

        updateProfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfil();
            }
        });

        getUserInfo();
    }


    private void chosePicture(){
        CropImage.activity().setAspectRatio(1, 1)
                .start(UpdateProfil.this);
    }

    public void updateProfil(){
        up = FirebaseDatabase.getInstance().getReference("user");
        up.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("nama").setValue(nama.getText().toString());
        up.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .child("phone").setValue(phone.getText().toString());

        startActivity(new Intent(this, Navbar.class));
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
                && resultCode == RESULT_OK && data != null){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            imgUri = result.getUri();
            profilImageView.setImageURI(imgUri);
            uploadProfilImg();
        }else{
            Toast.makeText(UpdateProfil.this, "Error, Try Again", Toast.LENGTH_SHORT).show();
        }
    }

    private void uploadProfilImg() {

        final ProgressDialog progressDialog = new ProgressDialog(UpdateProfil.this);
        progressDialog.setTitle("Set Your Profile");
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        if (imgUri != null) {
            final StorageReference fileRef = storageReference
                    .child(mAuth.getCurrentUser().getUid() + ".jpg");

            uploadTask = fileRef.putFile(imgUri);

            uploadTask.continueWithTask(new Continuation() {
                @Override
                public Object then(@NonNull Task task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    return fileRef.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        Uri downloadUrl = task.getResult();
                        myUri = downloadUrl.toString();

                        HashMap<String, Object> userMap = new HashMap<>();
                        userMap.put("image", myUri);

                        databaseReference.child(mAuth.getCurrentUser().getUid())
                                .updateChildren(userMap);

                        progressDialog.dismiss();
                    }
                }
            });
        } else {
            progressDialog.dismiss();
            Toast.makeText(UpdateProfil.this, "Image Not Selected", Toast.LENGTH_SHORT)
                    .show();
        }
    }
}