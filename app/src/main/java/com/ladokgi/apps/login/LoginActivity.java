package com.ladokgi.apps.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladokgi.apps.homepage.DokterActivity;
import com.ladokgi.apps.homepage.MainActivity;
import com.ladokgi.apps.R;
import com.ladokgi.apps.register.RegistrasiActivity;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

public class LoginActivity extends AppCompatActivity {

    Button btnLogin, btnDaftar;
    EditText etUsername, etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        Hawk.init(getApplicationContext()).build();
        CheckLogin();

        btnLogin = findViewById(R.id.btn_login);
        btnDaftar = findViewById(R.id.btn_daftar);
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegistrasiActivity.class);
                startActivity(i);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etUsername.getText().toString().equals("")){
                    Toaster("Username Tidak Boleh Kosong");
                }else if(etPassword.getText().toString().equals("")){
                    Toaster("Password Tidak Boleh Kosong");
                }else{
                    db.collection("users").document(etUsername.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if(task.isComplete()){
                                if(task.getResult()!=null){
                                    if(!etPassword.getText().toString().equals(task.getResult().get("password"))){
                                        Toaster("Password Salah");
                                    }else{
                                        Toaster("Login Berhasil");
                                        Hawk.put("username", task.getResult().get("username"));
                                        Hawk.put("role", task.getResult().get("role"));
                                        new android.os.Handler(Looper.getMainLooper()).postDelayed(
                                                new Runnable() {
                                                    public void run() {
                                                        if(task.getResult().get("role").equals("dokter")){
                                                            Intent i = new Intent(LoginActivity.this, DokterActivity.class);
                                                            startActivity(i);
                                                        }else{
                                                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                                                            startActivity(i);
                                                        }
                                                    }
                                                },
                                                1000);
                                    }
                                }else{

                                }
                            }
                        }
                    });
                }
            }
        });
    }

    public void Toaster(String text){
        Toast.makeText(getApplicationContext(),text,Toast.LENGTH_SHORT).show();
    }

    public void CheckLogin(){
        String session = Hawk.get("role");
        if(session!=null){
            if(session.equals("dokter")){
                Intent i = new Intent(LoginActivity.this,DokterActivity.class);
                startActivity(i);
                finish();
            }else{
                Intent i = new Intent(LoginActivity.this,MainActivity.class);
                startActivity(i);
                finish();
            }
        }
    }
}