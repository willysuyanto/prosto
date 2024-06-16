package com.ladokgi.apps.daftarpasien;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.ladokgi.apps.CreatePasienActivity;
import com.ladokgi.apps.EditPasienActivity;
import com.ladokgi.apps.R;
import com.ladokgi.apps.utils.ExportCSV;

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DaftarPasienActivity extends AppCompatActivity implements PasienAdapter.PasienAdapterCallback {

    LinearLayoutManager linearLayoutManager;
    PasienAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    List<Pasien> pasiens = new ArrayList<>();
    RecyclerView rv;
    FloatingActionButton fab;
    Toolbar toolbar;
    ProgressDialog dialog;
    private ExportCSV exportCSV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pasien);

        exportCSV = new ExportCSV(this);

        rv = findViewById(R.id.rv_pasien);
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbars);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.download){
                    exportDataPasienInCSV();
                }
                return false;
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DaftarPasienActivity.this, CreatePasienActivity.class);
                startActivity(i);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        dialog = ProgressDialog.show(this, "Mohon Tunggu",
                "Sedang Memuat Data", true);

        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                pasiens.removeAll(pasiens);
                for (DocumentSnapshot ds : value){
                    if(ds.getString("role").equals("pasien")){
                        pasiens.add(new Pasien(ds.getId(), ds.getString("nama"),ds.getString("alamat"), ds.getString("telepon"), ds.getString("jenis-kelamin"), ds.getString("pendidikan"),ds.getString("lama-menggunakan")));
                    }
                }
                if(pasiens.size()>0){
                    dialog.hide();
                    adapter.notifyDataSetChanged();
                }
                dialog.hide();
            }
        });


        mLayoutManager = new LinearLayoutManager(this);
        adapter = new PasienAdapter(getApplicationContext(),pasiens,this);
        rv.setLayoutManager(mLayoutManager);
        rv.setAdapter(adapter);

    }


    @Override
    public void onEditClicked(int position) {
        Intent intent = new Intent(DaftarPasienActivity.this, EditPasienActivity.class);
        intent.putExtra("pasien", pasiens.get(position));
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(int position) {
        AlertDialog.Builder alert = new AlertDialog.Builder(DaftarPasienActivity.this);
        alert.setTitle("Konfirmasi");
        alert.setMessage("Hapus data pasien "+pasiens.get(position).getNama()+"?");
        alert.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               FirebaseFirestore db = FirebaseFirestore.getInstance();
               db.collection("users").document(pasiens.get(position).getId()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                   @Override
                   public void onComplete(@NonNull @NotNull Task<Void> task) {
                       if(task.isSuccessful()){
                           Toast.makeText(DaftarPasienActivity.this, "Berhasil hapus data pasien", Toast.LENGTH_SHORT).show();
                       }
                   }
               });
            }
        });
        alert.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        alert.show();
    }

    void exportDataPasienInCSV() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Nama Pasien", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Pendidikan Terakhir", "Lama Menggunakan"});
        for (Pasien pasien : pasiens) {
            data.add(new String[]{pasien.nama, pasien.jenisK, pasien.alamat, pasien.telepon, pasien.pendidikan, pasien.lama});
        }

        String timestamp = Calendar.getInstance().get(Calendar.YEAR)+String.valueOf(Calendar.getInstance().get(Calendar.MONTH))+Calendar.getInstance().get(Calendar.DATE);
        exportCSV.exportCSV(data, "data_pasien_"+timestamp+".csv");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        exportCSV.onRequestPermissionsResult(requestCode, grantResults);
    }

}