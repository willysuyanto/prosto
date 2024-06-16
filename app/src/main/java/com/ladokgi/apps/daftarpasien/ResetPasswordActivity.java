package com.ladokgi.apps.daftarpasien;

import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladokgi.apps.R;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ResetPasswordActivity extends AppCompatActivity {

    Button btnSimpan;
    EditText etPassword, etPasswordConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reset_password);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        etPassword = findViewById(R.id.password);
        etPasswordConfirm = findViewById(R.id.passwordConfirm);
        btnSimpan = findViewById(R.id.btn_simpan_password);

        Pasien editData = (Pasien) getIntent().getSerializableExtra("pasien");

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("onClick: ", etPassword.getText().toString());
                Log.d("onClick: ", etPasswordConfirm.getText().toString());
                if(etPassword.getText().toString().equals("")){
                    Toaster("Password Tidak Boleh Kosong");
                } else if(etPasswordConfirm.getText().toString().equals("")){
                    Toaster("Password Konfirmasi Tidak Boleh Kosong");
                } else if(!etPassword.getText().toString().equals(etPasswordConfirm.getText().toString())){
                    Toaster("Password dan Password Konfirmasi Tidak Sama");
                }  else {
                    Map<String, Object> user = new HashMap<>();
                    user.put("password", etPassword.getText().toString());

                    db.collection("users").document(editData.getId()).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toaster("Berhasil Ubah Password");
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