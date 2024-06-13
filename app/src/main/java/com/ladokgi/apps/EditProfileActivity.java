package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class EditProfileActivity extends AppCompatActivity {

    Button btnSimpan;
    EditText etNama, etPhone, etAddress;
    Spinner spinner, spTahun, spJenkel;

    RelativeLayout layot1, layot2, layot3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

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

        layot1 = findViewById(R.id.layot1);
        layot2 = findViewById(R.id.layot2);
        layot3 = findViewById(R.id.layot3);

        etNama = findViewById(R.id.nama);
        etPhone = findViewById(R.id.nomor_tlp);
        etAddress = findViewById(R.id.alamat);
        btnSimpan = findViewById(R.id.btn_simpan);

        if(Hawk.get("role").equals("dokter")){
            layot1.setVisibility(View.GONE);
            layot2.setVisibility(View.GONE);
            layot3.setVisibility(View.GONE);
        }

        db.collection("users").document(Hawk.get("username")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                etNama.setText(value.getString("nama"));
                etPhone.setText(value.getString("telepon"));
                etAddress.setText(value.getString("alamat"));
                spinner.setSelection(adapter.getPosition(value.getString("pendidikan")));
                spTahun.setSelection(adapter2.getPosition(value.getString("lama-menggunakan")));
                spJenkel.setSelection(adapter3.getPosition(value.getString("jenis-kelamin")));
            }
        });

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(etNama.getText().toString().equals("")){
                    Toaster("Nama Lengkap Tidak Boleh Kosong");
                }else if(etPhone.getText().toString().equals("")){
                    Toaster("Nomor Telepon Tidak Boleh Kosong");
                }else if(etAddress.getText().toString().equals("")){
                    Toaster("Alamat Tidak Boleh Kosong");
                }else if(Hawk.get("role").equals("pasien") && spJenkel.getSelectedItem().toString().equals("Jenis Kelamin")){
                    Toaster("Pilih Jenis Kelamin");
                }else if(Hawk.get("role").equals("pasien") && spinner.getSelectedItem().toString().equals("Pendidikan Terakhir")){
                    Toaster("Pilih Pendidikan Terakhir");
                }else if(Hawk.get("role").equals("pasien") && spTahun.getSelectedItem().toString().equals("Menggunakan gigi tiruan sejak")){
                    Toaster("Pilih Tahun Menggunakan Gigi Tiruan");
                }else {
                    Map<String, Object> user = new HashMap<>();
                    user.put("nama", etNama.getText().toString());
                    user.put("telepon", etPhone.getText().toString());
                    user.put("alamat", etAddress.getText().toString());
                    if(Hawk.get("role").equals("pasien")){
                        user.put("pendidikan", spinner.getSelectedItem().toString());
                        user.put("lama-menggunakan", spTahun.getSelectedItem().toString());
                        user.put("jenis-kelamin", spJenkel.getSelectedItem().toString());
                    }

                    db.collection("users").document(Hawk.get("username")).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toaster("Berhasil Ubah Profil");
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