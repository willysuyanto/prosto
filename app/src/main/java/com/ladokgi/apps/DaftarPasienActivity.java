package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
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

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class DaftarPasienActivity extends AppCompatActivity implements PasienAdapter.PasienAdapterCallback {

    LinearLayoutManager linearLayoutManager;
    PasienAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    List<Pasien> pasiens = new ArrayList<>();
    RecyclerView rv;
    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pasien);

        rv = findViewById(R.id.rv_pasien);
        fab = findViewById(R.id.fab);
        

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(DaftarPasienActivity.this, CreatePasienActivity.class);
                startActivity(i);
            }
        });

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        ProgressDialog dialog = ProgressDialog.show(this, "Mohon Tunggu",
                "Sedang Memuat Data", true);

        db.collection("users").addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable QuerySnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                pasiens.removeAll(pasiens);
                for (DocumentSnapshot ds : value){
                    if(ds.getString("role").equals("pasien")){
                        pasiens.add(new Pasien(ds.getString("nama"),ds.getString("alamat"), ds.getString("telepon"),ds.getId()));
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
}