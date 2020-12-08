package com.example.tubes_kelompok_d;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.tubes_kelompok_d.databaseUser.UserHelperDatabaseClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Login extends AppCompatActivity {
    private TextView register;
    private Switch switchers;
    private EditText edtEmail,edtPass;
    private Button btnSignIn;
    private FirebaseAuth mAuth;
    private String CHANNEL_ID = "Channel 1";
    private ProgressDialog progressDialog;
    Theme theme;
    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        theme = new Theme(this);
        if(theme.loadNightModeState()==true){
            setTheme(R.style.DarkTheme);
        }
        else setTheme(R.style.AppTheme);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        edtEmail = findViewById(R.id.Email);
        edtPass = findViewById(R.id.password);
        btnSignIn = findViewById(R.id.Login);
        register = findViewById(R.id.register);
        switchers = findViewById(R.id.switcher);
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);

        if(theme.loadNightModeState()==true){
            switchers.setChecked(true);
        }
        switchers.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked){
                if(isChecked) {
                    theme.setNightModeState(true);
                    startActivity(new Intent(Login.this,Login.class));
                } else{
                    theme.setNightModeState(false);
                    startActivity(new Intent(Login.this,Login.class));

                }
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Login.this,Register.class));
            }
        });

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Login();
            }
        });
    }


    private void Login() {

        String email = edtEmail.getText().toString();
        String pass = edtPass.getText().toString();
        if(email.equals("admin") && pass.equals("admin")){
            Intent intent = new Intent(getApplicationContext(), Admin.class);
            startActivity(intent);
        }
        else if (TextUtils.isEmpty(email)){
            Toast.makeText(getApplicationContext(), "Enter Your Email", Toast.LENGTH_SHORT)
                    .show();
            edtEmail.setError("Enter Your Email");
            return;
        } else if(!isValidEmail(email)){
            Toast.makeText(getApplicationContext(), "Email Invalid", Toast.LENGTH_SHORT)
                    .show();
            edtEmail.setError("Email Invalid");
            return;
        } else if (TextUtils.isEmpty(pass)){
            Toast.makeText(getApplicationContext(), "Enter Your Password", Toast.LENGTH_SHORT)
                    .show();
            edtPass.setError("Enter Your Password");
            return;
        }else {
            progressDialog.setMessage("Please wait...");
            progressDialog.show();
            progressDialog.setCanceledOnTouchOutside(false);
            mAuth.signInWithEmailAndPassword(email, pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        user = FirebaseAuth.getInstance().getCurrentUser();
                        if (!user.isEmailVerified()) {
                            Toast.makeText(Login.this, "Akun belum diverifikasi", Toast.LENGTH_SHORT).show();
                        } else {
                            getUser();
                            Intent intent = new Intent(getApplicationContext(), Navbar.class);
                            startActivity(intent);
                            Toast.makeText(getApplicationContext(), "Login Successfull", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT).show();
                        clearText();
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

    private void clearText(){
        edtEmail.setText("");
        edtPass.setText("");
    }

    private void getUser(){
        FirebaseDatabase.getInstance().getReference("user")
                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        GlobalVariableDatabase.user = snapshot.getValue(UserHelperDatabaseClass.class);
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
    }
}