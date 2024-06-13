package com.ladokgi.apps.register;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladokgi.apps.R;
import com.ladokgi.apps.login.LoginActivity;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class RegistrasiActivity extends AppCompatActivity {

    Button btnDaftar;
    EditText etUsername, etPassword, etNama, etPhone, etAddress;
    Spinner spinner, spTahun, spJenkel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrasi);

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        spinner = (Spinner) findViewById(R.id.spn);
        spTahun = (Spinner) findViewById(R.id.spn2);
        spJenkel = (Spinner) findViewById(R.id.spn3);
        ArrayList<String> years = new ArrayList<>();
        ArrayList<String> strings = new ArrayList<>();
        ArrayList<String> jenkels = new ArrayList<>();

        jenkels.add("Jenis Kelamin");
        jenkels.add("Laki-Laki");
        jenkels.add("Perempuan");

        years.add("Menggunakan gigi tiruan sejak");
        Calendar calendar = Calendar.getInstance(TimeZone.getDefault());
        int currentYear = calendar.get(Calendar.YEAR);
        Log.d( "calendar: ", String.valueOf(currentYear));
        for (int i=currentYear; i>=1990; i--){
            Log.d( "onCreate: ", String.valueOf(i));
            years.add(String.valueOf(i));
        }
        strings.add("Pendidikan Terakhir");
        strings.add("SMA") ;
        strings.add("D3");
        strings.add("S1");
        strings.add("S2");
        strings.add("S3");
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, strings);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, years);
        ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, jenkels);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        adapter3.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spTahun.setAdapter(adapter2);
        spJenkel.setAdapter(adapter3);

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
                }else if(spJenkel.getSelectedItem().toString().equals("Jenis Kelamin")){
                    Toaster("Pilih Jenis Kelamin");
                }else if(spinner.getSelectedItem().toString().equals("Pendidikan Terakhir")){
                    Toaster("Pilih Pendidikan Terakhir");
                }else if(spTahun.getSelectedItem().toString().equals("Menggunakan gigi tiruan sejak")){
                    Toaster("Pilih Tahun Menggunakan Gigi Tiruan");
                }else if(etUsername.getText().toString().equals("")){
                    Toaster("Username Tidak Boleh Kosong");
                }else if(etPassword.getText().toString().equals("")){
                    Toaster("Password Tidak Boleh Kosong");
                }else{
                    Map<String, Object> user = new HashMap<>();
                    user.put("nama", etNama.getText().toString());
                    user.put("telepon", etPhone.getText().toString());
                    user.put("alamat", etAddress.getText().toString());
                    user.put("pendidikan", spinner.getSelectedItem().toString());
                    user.put("lama-menggunakan", spTahun.getSelectedItem().toString());
                    user.put("jenis-kelamin", spJenkel.getSelectedItem().toString());
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
                                                                Intent i = new Intent(RegistrasiActivity.this, LoginActivity.class);
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