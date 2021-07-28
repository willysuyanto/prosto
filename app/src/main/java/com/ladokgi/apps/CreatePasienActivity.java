package com.ladokgi.apps;

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
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class CreatePasienActivity extends AppCompatActivity {

    EditText etNama, etAlamat, etTelepon;
    Button btnSimpan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_pasien);
        etNama = findViewById(R.id.nama);
        etAlamat = findViewById(R.id.alamat);
        etTelepon = findViewById(R.id.nomor_tlp);
        btnSimpan = findViewById(R.id.btn_simpan);

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNama.getText().toString().equals("")){
                    Toaster("Nama tidak boleh kosong");
                }else if(etAlamat.getText().toString().equals("")){
                    Toaster("Alamat tidak boleh kosong");
                }else if(etTelepon.getText().toString().equals("")){
                    Toaster("Nomor Telepon tidak boleh kosong");
                }else{
                    Map<String, Object> data = new HashMap<>();
                    data.put("nama", etNama.getText().toString());
                    data.put("alamat",etAlamat.getText().toString());
                    data.put("telepon", etTelepon.getText().toString());
                    data.put("username", etTelepon.getText().toString());
                    data.put("password", etTelepon.getText().toString());
                    data.put("role", "pasien");

                    db.collection("users").document(etTelepon.getText().toString()).set(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toaster("Berhasil Buat Data Pasien");
                                new android.os.Handler(Looper.getMainLooper()).postDelayed(
                                        new Runnable() {
                                            public void run() {
                                                finish();
                                            }
                                        },
                                        1000);
                            }
                        }
                    });
                }
            }
        });
    }

    void Toaster(String text){
        Toast.makeText(getApplicationContext(),text, Toast.LENGTH_SHORT).show();
    }
}