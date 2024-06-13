package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

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
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladokgi.apps.daftarpasien.Pasien;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

public class EditPasienActivity extends AppCompatActivity {

    EditText etNama, etAlamat, etTelepon;
    Button btnSimpan;
    Spinner spinner, spTahun, spJenkel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_pasien);

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
        etNama = findViewById(R.id.nama);
        etAlamat = findViewById(R.id.alamat);
        etTelepon = findViewById(R.id.nomor_tlp);
        btnSimpan = findViewById(R.id.btn_simpan);

        Pasien editData = (Pasien) getIntent().getSerializableExtra("pasien");

        etNama.setText(editData.getNama());
        etAlamat.setText(editData.getAlamat());
        etTelepon.setText(editData.getTelepon());
        spinner.setSelection(adapter.getPosition(editData.getPendidikan()));
        spTahun.setSelection(adapter2.getPosition(editData.getLama()));
        spJenkel.setSelection(adapter3.getPosition(editData.getJenisK()));

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
                }else if(spJenkel.getSelectedItem().toString().equals("Jenis Kelamin")){
                    Toaster("Pilih Jenis Kelamin");
                }else if(spinner.getSelectedItem().toString().equals("Pendidikan Terakhir")){
                    Toaster("Pilih Pendidikan Terakhir");
                }else if(spTahun.getSelectedItem().toString().equals("Pilih Tahun Menggunakan Gigi Tiruan")){
                    Toaster("Pilih Tahun");
                }else{
                    Map<String, Object> data = new HashMap<>();
                    data.put("nama", etNama.getText().toString());
                    data.put("alamat",etAlamat.getText().toString());
                    data.put("telepon", etTelepon.getText().toString());
                    data.put("jenis-kelamin", spJenkel.getSelectedItem().toString());
                    data.put("pendidikan", spinner.getSelectedItem().toString());
                    data.put("lama-menggunakan", spTahun.getSelectedItem().toString());

                    db.collection("users").document(editData.getId()).update(data).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toaster("Berhasil Update Data Pasien");
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