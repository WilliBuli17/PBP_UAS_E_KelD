package com.example.tubes_kelompok_d;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tubes_kelompok_d.databaseUser.UserHelperDatabaseClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.jetbrains.annotations.NotNull;

public class Register extends AppCompatActivity {
    private Button register;
    private EditText Email,Password,Nama,Phone;
    private FirebaseAuth mAuth;
    private FirebaseDatabase firebaseDatabase;
    DatabaseReference databaseReference;
    UserHelperDatabaseClass databaseClass;
    private ProgressDialog progressDialog;
    String email,pass,nama,phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register = findViewById(R.id.register);
        Email = findViewById(R.id.Email);
        Password = findViewById(R.id.Password);
        Nama = findViewById(R.id.Nama);
        Phone = findViewById(R.id.Phone);
        mAuth = FirebaseAuth.getInstance();
        firebaseDatabase = FirebaseDatabase.getInstance();
        databaseReference = firebaseDatabase.getReference("user");

        progressDialog = new ProgressDialog(this);

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Register();
            }
        });

    }

    public void Register(){
        email = Email.getText().toString();
        pass = Password.getText().toString();
        nama = Nama.getText().toString();
        phone = Phone.getText().toString();


        if(nama.isEmpty()) {
            Toast.makeText(Register.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
            Nama.setError("Enter Your Nama");
            return;
        }else if(email.isEmpty() || !isValidEmail(email)) {
            Toast.makeText(Register.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
            Email.setError("Enter Your Email");
            return;
        }else if(pass.isEmpty() || pass.length() < 6) {
            Toast.makeText(Register.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
            Password.setError("Enter Your Password");
            return;
        }else if(phone.isEmpty()) {
            Toast.makeText(Register.this,"Authentication Failed",Toast.LENGTH_SHORT).show();
            Phone.setError("Enter Your Phone");
            return;
        }else{
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            mAuth.createUserWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()){
                        firebaseDatabse(mAuth);
                        startActivity(new Intent(Register.this,Login.class));
                        Toast.makeText(getApplicationContext(),"Sign Up Successfull",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(),"Sign Up Failed",Toast.LENGTH_SHORT).show();
                    }
                    progressDialog.dismiss();
                }
            });
        }
    }

    @NotNull
    private Boolean isValidEmail(CharSequence charSequence){
        return (!TextUtils.isEmpty(charSequence) &&
                Patterns.EMAIL_ADDRESS.matcher(charSequence).matches());
    }

    private void firebaseDatabse(FirebaseAuth mAuth){

        databaseClass = new UserHelperDatabaseClass();
        databaseClass.setEmail(email);
        databaseClass.setPassword(pass);
        databaseClass.setNama(nama);
        databaseClass.setPhone(phone);
        mAuth.getCurrentUser().sendEmailVerification().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                        .setValue(databaseClass).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(getApplicationContext(), "User Register Success", Toast.LENGTH_SHORT)
                                .show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Failed " + e.getMessage(), Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }
}