package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firestore.v1.Document;
import com.google.type.DateTime;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AspekPerilakuActivity extends AppCompatActivity {

    RadioGroup rg1, rg2, rg3, rg4;
    CheckBox cb_pagi1, cb_siang1, cb_malam1, cb_siang2, cb_sore2,cb_tidur2, cb_makan2, cb_bangun2;
    Button btnSubmit;

    int BobotP1 = 0;
    int BobotP2 = 0;
    int BobotP3 = 0;
    int BobotP4 = 0;
    String SelectedP1 = "";
    String SelectedP2 = "";
    String SelectedP3 = "";
    String SelectedP4 = "";
    List<String> selectedCB1 = new ArrayList();
    List<String> selectedCB2 = new ArrayList();

    CollectionReference konsultasi;
    String currentUser;
    int currentId;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspek_perilaku);

        Hawk.init(getApplicationContext()).build();
        currentUser = Hawk.get("username");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        konsultasi = db.collection("users").document(currentUser).collection("konsultasi");

        checkInitialData();

        btnSubmit = findViewById(R.id.btn_kirim);
        rg1 = findViewById(R.id.pertanyaanRadio1);
        rg2 = findViewById(R.id.pertanyaanRadio2);
        rg3 = findViewById(R.id.pertanyaanRadio3);
        rg4 = findViewById(R.id.pertanyaanRadio4);
        cb_pagi1 = findViewById(R.id.cb_pagi_1);
        cb_siang1 = findViewById(R.id.cb_siang_1);
        cb_malam1 = findViewById(R.id.cb_malam_1);
        //cb_pagi2 = findViewById(R.id.cb_pagi_2);
        cb_siang2 = findViewById(R.id.cb_siang_2);
        cb_sore2 = findViewById(R.id.cb_sore_2);
        //cb_malam2 = findViewById(R.id.cb_malam_2);
        cb_bangun2 = findViewById(R.id.cb_bangun_2);
        cb_makan2 = findViewById(R.id.cb_makan_2);
        cb_tidur2 = findViewById(R.id.cb_tidur_2);

        rg1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                switch (checkedId){
                    case 1:
                        BobotP1=5;
                        SelectedP1 = "Sangat Sering";
                        break;
                    case 2:
                        BobotP1=4;
                        SelectedP1 = "Sering";
                        break;
                    case 3:
                        BobotP1=3;
                        SelectedP1 = "Cukup Sering";
                        break;
                    case 4:
                        BobotP1=2;
                        SelectedP1 = "Jarang";
                        break;
                    case 5:
                        BobotP1=1;
                        SelectedP1 = "Sangat Jarang";
                        break;
                }
                Log.d( "onCheckedChanged: ", ""+BobotP1);
            }
        });

        cb_pagi1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB1.add("Pagi");
                }else{
                    selectedCB1.remove("Pagi");
                }
            }
        });

        cb_siang1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB1.add("Siang");
                }else{
                    selectedCB1.remove("Siang");
                }
            }
        });

        cb_malam1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB1.add("Malam");
                }else{
                    selectedCB1.remove("Malam");
                }
            }
        });

        rg2.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d("onCheckedChanged: ",""+checkedId);
                switch (checkedId){
                    case 6:
                        BobotP2=5;
                        SelectedP2="Sangat Sering";
                        break;
                    case 7:
                        BobotP2=4;
                        SelectedP2="Sering";
                        break;
                    case 8:
                        BobotP2=3;
                        SelectedP2="Cukup Sering";
                        break;
                    case 9:
                        BobotP2=2;
                        SelectedP2="Jarang";
                        break;
                    case 10:
                        BobotP2=1;
                        SelectedP2="Sangat Jarang";
                        break;
                }
                Log.d( "onCheckedChanged: ", ""+BobotP2);
            }
        });

        cb_siang2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB2.add("Siang");
                }else{
                    selectedCB2.remove("Siang");
                }
            }
        });

        cb_sore2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB2.add("Sore");
                }else{
                    selectedCB2.remove("Sore");
                }
            }
        });

        cb_makan2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB2.add("Sesudah Makan");
                }else{
                    selectedCB2.remove("Sesudah Makan");
                }
            }
        });

        cb_bangun2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB2.add("Bangun Pagi");
                }else{
                    selectedCB2.remove("Bangun Pagi");
                }
            }
        });

        cb_tidur2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectedCB2.add("Sebelum Tidur");
                }else{
                    selectedCB2.remove("Sebelum Tidur");
                }
            }
        });

        rg3.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId){
                    case 11:
                        BobotP3=5;
                        SelectedP3="Sangat Sering";
                        break;
                    case 12:
                        BobotP3=4;
                        SelectedP3="Cukup Sering";
                        break;
                    case 13:
                        BobotP3=3;
                        SelectedP3="Sering";
                        break;
                    case 14:
                        BobotP3=2;
                        SelectedP3="Jarang";
                        break;
                    case 15:
                        BobotP3=1;
                        SelectedP3="Sangat Jarang";
                        break;
                }
            }
        });

        rg4.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Log.d( "checkID: ", ""+checkedId);
                switch (checkedId){
                    case 16:
                        BobotP4=5;
                        SelectedP4="Sangat Sering";
                        break;
                    case 17:
                        BobotP4=4;
                        SelectedP4="Cukup Sering";
                        break;
                    case 18:
                        BobotP4=3;
                        SelectedP4="Sering";
                        break;
                    case 19:
                        BobotP4=2;
                        SelectedP4="Jarang";
                        break;
                    case 20:
                        BobotP4=1;
                        SelectedP4="Sangat Jarang";
                        break;
                }
                Log.d( "hasilnya: ", ""+SelectedP4);
            }
        });

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int hasil = BobotP1+BobotP2+BobotP3+BobotP4+selectedCB2.size()+selectedCB1.size();
                Log.d( "P1",""+BobotP1);
                Log.d( "P2",""+BobotP2);
                Log.d( "P3",""+BobotP3);
                Log.d( "P4",""+BobotP4);
                Log.d( "CB1",""+selectedCB1.size());
                Log.d( "CB2",""+selectedCB2.size());
                Log.d("onClick: ","Total Bobot = "+hasil);
                Map<String, Object> aspek = new HashMap<>();
                Map<String, Object> perilaku = new HashMap<>();
                Map<String, Object> pertanyaan1 = new HashMap<>();
                Map<String, Object> pertanyaan2 = new HashMap<>();
                Map<String, Object> pertanyaan3 = new HashMap<>();
                Map<String, Object> pertanyaan4 = new HashMap<>();
                Map<String, Object> pertanyaanCb1 = new HashMap<>();
                Map<String, Object> pertanyaanCb2 = new HashMap<>();

                pertanyaan1.put("Jawaban", SelectedP1);
                pertanyaan1.put("Bobot", BobotP1);
                perilaku.put("pertanyaan1", pertanyaan1);

                pertanyaan2.put("Jawaban", SelectedP2);
                pertanyaan2.put("Bobot", BobotP2);
                perilaku.put("pertanyaan2", pertanyaan2);

                pertanyaan3.put("Jawaban", SelectedP3);
                pertanyaan3.put("Bobot", BobotP3);
                perilaku.put("pertanyaan3", pertanyaan3);

                pertanyaan4.put("Jawaban", SelectedP4);
                pertanyaan4.put("Bobot", BobotP4);
                perilaku.put("pertanyaan4", pertanyaan4);

                pertanyaanCb1.put("Jawaban", selectedCB1);
                pertanyaanCb1.put("Bobot", selectedCB1.size());
                perilaku.put("pertanyaanCb1", pertanyaanCb1);

                pertanyaanCb2.put("Jawaban", selectedCB2);
                pertanyaanCb2.put("Bobot", selectedCB2.size());
                perilaku.put("pertanyaanCb2",pertanyaanCb2);
                perilaku.put("total-bobot", hasil);
                perilaku.put("hasil",getHasilKonsultasi(hasil));
                aspek.put("perilaku",perilaku);
                SimpleDateFormat dateFormat = new SimpleDateFormat();
                dateFormat.applyPattern("dd MMMM yyyy");
                dateFormat.format(new Date());
                aspek.put("tanggal-konsultasi",dateFormat.format(new Date()));

                if(hasil<8){
                    Toast.makeText(getApplicationContext(),"Jawab semua pertanyaan terlebih dahulu", Toast.LENGTH_SHORT).show();
                }else{
                    String docPath = "konsultasi"+(currentId+1);
                    konsultasi.document(docPath).set(aspek).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull @NotNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Intent i = new Intent(AspekPerilakuActivity.this, AspekNonPerilaku.class);
                                i.putExtra("docPath", docPath);
                                startActivity(i);
                            }
                        }
                    });
                }
            }
        });
    }

    public void checkInitialData(){
        konsultasi.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                String docPath = "konsultasi"+(task.getResult());
                Log.d( "docPaths: ",docPath);
                currentId = task.getResult().size();
                konsultasi.document(docPath).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if(task.getResult().get("perilaku")!=null && task.getResult().get("non-perilaku")==null){
                            Log.d("onComplete: ","Ada");
                            Intent i = new Intent(AspekPerilakuActivity.this,AspekNonPerilaku.class);
                            i.putExtra("docPath",docPath);
                            startActivity(i);
                            finish();
                        }else{
                            Log.d("onComplete: ","Ga Ada, lanjut");
                        }
                    }
                });
            }
        });
    }

    public boolean getHasilKonsultasi(int bobot){
        if(bobot>15){
            return true;
        }else{
            return false;
        }
    }
}