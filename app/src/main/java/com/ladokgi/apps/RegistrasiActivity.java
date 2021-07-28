package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiActivity extends AppCompatActivity {

    Button btnDaftar;
    EditText etUsername, etPassword, etNama, etPhone, etAddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        etUsername = findViewById(R.id.username);
        etPassword = findViewById(R.id.password);
        etNama = findViewById(R.id.nama);
        etPhone = findViewById(R.id.nomor_tlp);
        etAddress = findViewById(R.id.alamat);
        btnDaftar = findViewById(R.id.btn_daftar);
        btnDaftar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNama.getText().toString().equals("")){
                    Toaster("Nama Lengkap Tidak Boleh Kosong");
                }else if(etPhone.getText().toString().equals("")){
                    Toaster("Nomor Telepon Tidak Boleh Kosong");
                }else if(etAddress.getText().toString().equals("")){
                    Toaster("Alamat Tidak Boleh Kosong");
                }else if(etUsername.getText().toString().equals("")){
                    Toaster("Username Tidak Boleh Kosong");
                }else if(etPassword.getText().toString().equals("")){
                    Toaster("Password Tidak Boleh Kosong");
                }else{
                    Map<String, Object> user = new HashMap<>();
                    user.put("nama", etNama.getText().toString());
                    user.put("telepon", etPhone.getText().toString());
                    user.put("alamat", etAddress.getText().toString());
                    user.put("username", etUsername.getText().toString());
                    user.put("password", etPassword.getText().toString());
                    user.put("role", "pasien");
                    db.collection("users").document(etUsername.getText().toString()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                            if(task.isComplete()){
                                if(task.getResult().exists()){
                                    Toaster("Username Sudah Digunakan");
                                }else{
                                    db.collection("users").document(etUsername.getText().toString()).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                                            if(task.isComplete()){
                                                Toaster("Berhasil Buat Akun");
                                                new android.os.Handler(Looper.getMainLooper()).postDelayed(
                                                        new Runnable() {
                                                            public void run() {
                                                                Intent i = new Intent(RegistrasiActivity.this,LoginActivity.class);
                                                                startActivity(i);
                                                            }
                                                        },
                                                        1000);
                                            }
                                        }
                                    });
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
}