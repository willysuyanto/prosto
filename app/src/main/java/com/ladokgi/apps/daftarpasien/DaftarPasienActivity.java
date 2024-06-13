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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daftar_pasien);

        rv = findViewById(R.id.rv_pasien);
        fab = findViewById(R.id.fab);
        toolbar = findViewById(R.id.toolbars);

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                if(item.getItemId()==R.id.download){
                    try {
                        exportDataPasienInCSV();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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

    public  static  File commonDocumentDirPath(String FolderName){
        File dir = null ;

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            dir = new File (Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOCUMENTS)+ "/"+FolderName );
        } else {
            dir = new File(Environment.getExternalStorageDirectory() + "/"+FolderName);
        }
        return  dir ;

    }

    void exportDataPasienInCSV() throws IOException {
        {

            File folder = new File(getExternalFilesDir(Environment.DIRECTORY_DOCUMENTS).getAbsolutePath());

            boolean var = false;
            if (!folder.exists()) {
                if(!folder.mkdir()){
                    Log.d("exportDataPasienInCSV: ", "gagal gaes");
                }
            }
            String timestamp = Calendar.getInstance().get(Calendar.YEAR) +String.valueOf(Calendar.getInstance().get(Calendar.MONTH))+Calendar.getInstance().get(Calendar.DATE);

            final String filename = folder.getAbsolutePath() + "/"+timestamp+"-data-pasien.csv";

            // show waiting screen
            CharSequence contentTitle = getString(R.string.app_name);
            dialog = ProgressDialog.show(
                    DaftarPasienActivity.this, contentTitle, "Mohon Tunggu...",
                    true);
            final Handler handler = new Handler(Looper.getMainLooper()) {
                @Override
                public void handleMessage(Message msg) {
                    if(msg.arg1==1){
                        Toast.makeText(getApplicationContext(), "Berhasil Download Data",Toast.LENGTH_SHORT).show();
                    }else{
                        Toast.makeText(getApplicationContext(), "Gagal Download Data: "+msg.obj.toString(),Toast.LENGTH_LONG).show();

                    }
                }
            };

            new Thread() {
                public void run() {
                    try {

                        FileWriter fw = new FileWriter(filename);
                        Log.d("run: ", filename);

                        fw.append("Nama Pasien");
                        fw.append(',');

                        fw.append("Jenis Kelamin");
                        fw.append(',');

                        fw.append("Alamat");
                        fw.append(',');

                        fw.append("Nomor Telepon");
                        fw.append(',');

                        fw.append("Pendidikan Terakhir");
                        fw.append(',');

                        fw.append("Lama Menggunakan");
                        fw.append(',');

                        fw.append('\n');

                        // fw.flush();
                        for (Pasien pasien:pasiens) {
                            fw.append(pasien.nama);
                            fw.append(',');

                            fw.append(pasien.jenisK);
                            fw.append(',');

                            fw.append(pasien.alamat);
                            fw.append(',');

                            fw.append(pasien.telepon);
                            fw.append(',');

                            fw.append(pasien.pendidikan);
                            fw.append(',');

                            fw.append(pasien.lama);
                            fw.append(',');

                            fw.append('\n');
                        }
                        fw.close();

                    } catch (Exception e) {
                        Message msg = new Message();
                        msg.arg1 = 2;
                        msg.obj = e;
                        handler.sendMessage(msg);
                    }
                    Message msg = new Message();
                    msg.arg1 = 1;

                    handler.sendMessage(msg);
                    dialog.dismiss();
                }
            }.start();

        }

    }

}