package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class AspekNonPerilaku extends AppCompatActivity {

    RadioGroup rg1, rg2, rg3, rg4, rg5;
    Button btnKirim;

    int BobotP1 = 0;
    int BobotP2 = 0;
    int BobotP3 = 0;
    int BobotP4 = 0;
    int BobotP5 = 0;
    String SelectedP1 = "";
    String SelectedP2 = "";
    String SelectedP3 = "";
    String SelectedP4 = "";
    String SelectedP5 = "";
    String docPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspek_non_perilaku);

        rg1 = findViewById(R.id.pertanyaanRadio1);
        rg2 = findViewById(R.id.pertanyaanRadio2);
        rg3 = findViewById(R.id.pertanyaanRadio3);
        rg4 = findViewById(R.id.pertanyaanRadio4);
        rg5 = findViewById(R.id.pertanyaanRadio5);
        btnKirim = findViewById(R.id.btn_kirim);

        Bundle extras = getIntent().getExtras();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Hawk.init(getApplicationContext()).build();
        String currentUser = Hawk.get("username");
        CollectionReference konsultasi = db.collection("users").document(currentUser).collection("konsultasi");
        if (extras != null) {
            docPath = extras.getString("docPath");
            Log.d( "Non-Perilaku", docPath);
        }

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d( "ID checkbox: ", ""+checkedId);
                switch (checkedId){
                    case 21:
                        BobotP1=1;
                        SelectedP1 = "Air Mineral Kemasan";
                        break;
                    case 22:
                        BobotP1=2;
                        SelectedP1 = "Air Ledeng";
                        break;
                    case 23:
                        BobotP1=3;
                        SelectedP1 = "Air Sumur";
                        break;
                }
                Log.d( "onCheckedChanged: ", ""+BobotP1);
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case 24:
                        BobotP2=0;
                        SelectedP2 = "Pulau Sumatra";
                        break;
                    case 25:
                        BobotP2=0;
                        SelectedP2 = "Pulau Jawa";
                        break;
                    case 26:
                        BobotP2=0;
                        SelectedP2 = "Pulau Kalimantan";
                        break;
                    case 27:
                        BobotP2=0;
                        SelectedP2 = "Pulau Sulawesi";
                        break;
                    case 28:
                        BobotP2=0;
                        SelectedP2 = "Papua";
                        break;
                }
                Log.d( "onCheckedChanged: ", ""+BobotP2);
            }
        });

        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case 29:
                        BobotP3=3;
                        SelectedP3 = "Bercuka";
                        break;
                    case 30:
                        BobotP3=2;
                        SelectedP3 = "Asin";
                        break;
                    case 31:
                        BobotP3=1;
                        SelectedP3 = "Manis";
                        break;
                }
                Log.d( "onCheckedChanged: ", ""+BobotP3);
            }
        });

        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case 32:
                        BobotP4=5;
                        SelectedP4 = "Sangat Banyak";
                        break;
                    case 33:
                        BobotP4=4;
                        SelectedP4 = "Banyak";
                        break;
                    case 34:
                        BobotP4=3;
                        SelectedP4 = "Ada";
                        break;
                    case 35:
                        BobotP4=2;
                        SelectedP4 = "Sedikit";
                        break;
                    case 36:
                        BobotP4=1;
                        SelectedP4 = "Tidak Ada";
                        break;
                }
                Log.d( "onCheckedChanged: ", ""+BobotP4);
            }
        });

        rg5.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case 37:
                        BobotP5=5;
                        SelectedP5 = "Sangat Banyak";
                        break;
                    case 38:
                        BobotP5=4;
                        SelectedP5 = "Banyak";
                        break;
                    case 39:
                        BobotP5=3;
                        SelectedP5 = "Ada";
                        break;
                    case 40:
                        BobotP5=2;
                        SelectedP5 = "Sedikit";
                        break;
                    case 41:
                        BobotP5=1;
                        SelectedP5 = "Tidak Ada";
                        break;
                }
                Log.d( "onCheckedChanged: ", ""+BobotP5);
            }
        });

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int total = BobotP1+BobotP2+BobotP3+BobotP4+BobotP5;
                Log.d("Submit","total "+total);

                Map<String,Object> nonPerilaku = new HashMap<>();
                Map<String,Object> aspek = new HashMap<>();
                Map<String, Object> p1 = new HashMap<>();
                Map<String, Object> p2 = new HashMap<>();
                Map<String, Object> p3 = new HashMap<>();
                Map<String, Object> p4 = new HashMap<>();
                Map<String, Object> p5 = new HashMap<>();
                p1.put("Bobot", BobotP1);
                p1.put("Jawaban", SelectedP1);
                nonPerilaku.put("pertanyaan1", p1);

                p2.put("Bobot", BobotP2);
                p2.put("Jawaban", SelectedP2);
                nonPerilaku.put("pertanyaan2", p2);

                p3.put("Bobot", BobotP3);
                p3.put("Jawaban", SelectedP3);
                nonPerilaku.put("pertanyaan3", p3);

                p4.put("Bobot", BobotP4);
                p4.put("Jawaban", SelectedP4);
                nonPerilaku.put("pertanyaan4", p4);

                p5.put("Bobot", BobotP4);
                p5.put("Jawaban", SelectedP4);
                nonPerilaku.put("pertanyaan5", p5);
                nonPerilaku.put("total-bobot", total);
                nonPerilaku.put("hasil",getHasilKonsultasi(total));
                aspek.put("status", "pending");
                aspek.put("non-perilaku", nonPerilaku);



                konsultasi.document(docPath).set(aspek, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(AspekNonPerilaku.this, MainActivity.class);
                            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                        }
                    }
                });
            }
        });


    }
    public boolean getHasilKonsultasi(int bobot){
        if(bobot<8){
            return true;
        }else{
            return false;
        }
    }
}