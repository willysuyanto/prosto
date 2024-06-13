package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AspekPerilakuActivity extends AppCompatActivity {
    CustomSeekbar sb1_1, sb1_2, sb1_3, sb2_1, sb2_2, sb2_3, sb3, sb4, sb5, sb6, sb7, sb8, sb9, sb10, sb11, sb12, sb13, sb14, sb15, sb16, sb17, sb18;
    LinearLayout p1_1, p1_2, p1_3, p2_1, p2_2, p2_3, p3, p4, p5, p6, p7, p8, p9, p10, p11, p12, p13, p14, p15, p16, p17, p18;
    Button btnSubmit;

    CollectionReference konsultasi;
    String currentUser;
    int currentId;


    public void initializeSeekbar(Context context) {
        // Instantiate Custom Seekbar
        sb1_1 = new CustomSeekbar(context, 7, R.color.black, "Merugikan", "Menguntungkan");
        sb1_2 = new CustomSeekbar(context, 7, R.color.black, "Merepotkan", "Memudahkan");
        sb1_3 = new CustomSeekbar(context, 7, R.color.black, "Membosankan", "Menyenangkan");
        sb2_1 = new CustomSeekbar(context, 7, R.color.black, "Buruk", "Baik");
        sb2_2 = new CustomSeekbar(context, 7, R.color.black, "Membuang Waktu", "Bermanfaat");
        sb2_3 = new CustomSeekbar(context, 7, R.color.black, "Membosankan", "Menyenangkan");
        sb3 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb4 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb5 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb6 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb7 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb8 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb9 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb10 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb11 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb12 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb13 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb14 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb15 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb16 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb17 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");
        sb18 = new CustomSeekbar(context, 7, R.color.black, "Tidak Setuju", "Setuju");

        // Bind seekbar parent view (LinearLayout)
        p1_1 = findViewById(R.id.pertanyaan1);
        p1_2 = findViewById(R.id.pertanyaan1_2);
        p1_3 = findViewById(R.id.pertanyaan1_3);
        p2_1 = findViewById(R.id.pertanyaan2);
        p2_2 = findViewById(R.id.pertanyaan2_2);
        p2_3 = findViewById(R.id.pertanyaan2_3);
        p3 = findViewById(R.id.pertanyaan3);
        p4 = findViewById(R.id.pertanyaan4);
        p5 = findViewById(R.id.pertanyaan5);
        p6 = findViewById(R.id.pertanyaan6);
        p7 = findViewById(R.id.pertanyaan7);
        p8 = findViewById(R.id.pertanyaan8);
        p9 = findViewById(R.id.pertanyaan9);
        p10 = findViewById(R.id.pertanyaan10);
        p11 = findViewById(R.id.pertanyaan11);
        p12 = findViewById(R.id.pertanyaan12);
        p13 = findViewById(R.id.pertanyaan13);
        p14 = findViewById(R.id.pertanyaan14);
        p15 = findViewById(R.id.pertanyaan15);
        p16 = findViewById(R.id.pertanyaan16);
        p17 = findViewById(R.id.pertanyaan17);
        p18 = findViewById(R.id.pertanyaan18);


        // add seekbar to its view
        sb1_1.addSeekBar(p1_1);
        sb1_2.addSeekBar(p1_2);
        sb1_3.addSeekBar(p1_3);
        sb2_1.addSeekBar(p2_1);
        sb2_2.addSeekBar(p2_2);
        sb2_3.addSeekBar(p2_3);
        sb3.addSeekBar(p3);
        sb4.addSeekBar(p4);
        sb5.addSeekBar(p5);
        sb6.addSeekBar(p6);
        sb7.addSeekBar(p7);
        sb8.addSeekBar(p8);
        sb9.addSeekBar(p9);
        sb10.addSeekBar(p10);
        sb11.addSeekBar(p11);
        sb12.addSeekBar(p12);
        sb13.addSeekBar(p13);
        sb14.addSeekBar(p14);
        sb15.addSeekBar(p15);
        sb16.addSeekBar(p16);
        sb17.addSeekBar(p17);
        sb18.addSeekBar(p18);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspek_perilaku);

        Hawk.init(getApplicationContext()).build();
        currentUser = Hawk.get("username");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        konsultasi = db.collection("users").document(currentUser).collection("konsultasi");

        initializeSeekbar(this.getApplicationContext());

        checkInitialData();

        btnSubmit = findViewById(R.id.btn_kirim);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalBobot = 0;
                Map<String, Object> aspek = new HashMap<>();
                Map<String, Object> perilaku = new HashMap<>();

                Map<String, Object> pertanyaan1_1 = new HashMap<>();
                pertanyaan1_1.put("Bobot", sb1_1.getValue());
                perilaku.put("pertanyaan1_1", pertanyaan1_1);
                totalBobot += sb1_1.getValue();

                Map<String, Object> pertanyaan1_2 = new HashMap<>();
                pertanyaan1_2.put("Bobot", sb1_2.getValue());
                perilaku.put("pertanyaan1_2", pertanyaan1_2);
                totalBobot += sb1_2.getValue();

                Map<String, Object> pertanyaan1_3 = new HashMap<>();
                pertanyaan1_3.put("Bobot", sb1_3.getValue());
                perilaku.put("pertanyaan1_3", pertanyaan1_3);
                totalBobot += sb1_3.getValue();

                Map<String, Object> pertanyaan2_1 = new HashMap<>();
                pertanyaan2_1.put("Bobot", sb2_1.getValue());
                perilaku.put("pertanyaan2_1", pertanyaan2_1);
                totalBobot += sb2_1.getValue();

                Map<String, Object> pertanyaan2_2 = new HashMap<>();
                pertanyaan2_2.put("Bobot", sb2_2.getValue());
                perilaku.put("pertanyaan2_2", pertanyaan2_2);
                totalBobot += sb2_2.getValue();

                Map<String, Object> pertanyaan2_3 = new HashMap<>();
                pertanyaan2_3.put("Bobot", sb2_3.getValue());
                perilaku.put("pertanyaan2_3", pertanyaan2_3);
                totalBobot += sb2_3.getValue();

                Map<String, Object> pertanyaan3 = new HashMap<>();
                pertanyaan3.put("Bobot", sb3.getValue());
                perilaku.put("pertanyaan3", pertanyaan3);
                totalBobot += sb3.getValue();

                Map<String, Object> pertanyaan4 = new HashMap<>();
                pertanyaan4.put("Bobot", sb4.getValue());
                perilaku.put("pertanyaan4", pertanyaan4);
                totalBobot += sb4.getValue();

                Map<String, Object> pertanyaan5 = new HashMap<>();
                pertanyaan5.put("Bobot", sb5.getValue());
                perilaku.put("pertanyaan5", pertanyaan5);
                totalBobot += sb5.getValue();

                Map<String, Object> pertanyaan6 = new HashMap<>();
                pertanyaan6.put("Bobot", sb6.getValue());
                perilaku.put("pertanyaan6", pertanyaan6);
                totalBobot += sb6.getValue();

                Map<String, Object> pertanyaan7 = new HashMap<>();
                pertanyaan7.put("Bobot", sb7.getValue());
                perilaku.put("pertanyaan7", pertanyaan7);
                totalBobot += sb7.getValue();

                Map<String, Object> pertanyaan8 = new HashMap<>();
                pertanyaan8.put("Bobot", sb8.getValue());
                perilaku.put("pertanyaan8", pertanyaan8);
                totalBobot += sb8.getValue();

                Map<String, Object> pertanyaan9 = new HashMap<>();
                pertanyaan9.put("Bobot", sb6.getValue());
                perilaku.put("pertanyaan9", pertanyaan9);
                totalBobot += sb9.getValue();

                Map<String, Object> pertanyaan10 = new HashMap<>();
                pertanyaan10.put("Bobot", sb10.getValue());
                perilaku.put("pertanyaan10", pertanyaan10);
                totalBobot += sb10.getValue();

                Map<String, Object> pertanyaan11 = new HashMap<>();
                pertanyaan11.put("Bobot", sb11.getValue());
                perilaku.put("pertanyaan11", pertanyaan11);
                totalBobot += sb11.getValue();

                Map<String, Object> pertanyaan12 = new HashMap<>();
                pertanyaan12.put("Bobot", sb12.getValue());
                perilaku.put("pertanyaan12", pertanyaan12);
                totalBobot += sb12.getValue();

                Map<String, Object> pertanyaan13 = new HashMap<>();
                pertanyaan13.put("Bobot", sb13.getValue());
                perilaku.put("pertanyaan13", pertanyaan13);
                totalBobot += sb13.getValue();

                Map<String, Object> pertanyaan14 = new HashMap<>();
                pertanyaan14.put("Bobot", sb14.getValue());
                perilaku.put("pertanyaan14", pertanyaan14);
                totalBobot += sb14.getValue();

                Map<String, Object> pertanyaan15 = new HashMap<>();
                pertanyaan15.put("Bobot", sb15.getValue());
                perilaku.put("pertanyaan15", pertanyaan15);
                totalBobot += sb15.getValue();

                Map<String, Object> pertanyaan16 = new HashMap<>();
                pertanyaan16.put("Bobot", sb16.getValue());
                perilaku.put("pertanyaan16", pertanyaan16);
                totalBobot += sb16.getValue();

                Map<String, Object> pertanyaan17 = new HashMap<>();
                pertanyaan17.put("Bobot", sb17.getValue());
                perilaku.put("pertanyaan17", pertanyaan17);
                totalBobot += sb17.getValue();

                Map<String, Object> pertanyaan18 = new HashMap<>();
                pertanyaan18.put("Bobot", sb18.getValue());
                perilaku.put("pertanyaan18", pertanyaan18);
                totalBobot += sb18.getValue();


                perilaku.put("total-bobot", totalBobot);
                perilaku.put("hasil", getHasilKonsultasi(totalBobot));
                aspek.put("perilaku", perilaku);
                SimpleDateFormat dateFormat = new SimpleDateFormat();
                dateFormat.applyPattern("dd MMMM yyyy");
                aspek.put("tanggal-konsultasi", dateFormat.format(new Date()));

                String docPath = "konsultasi" + (currentId + 1);
                konsultasi.document(docPath).set(aspek).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Intent i = new Intent(AspekPerilakuActivity.this, AspekNonPerilaku.class);
                            i.putExtra("docPath", docPath);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        });
    }

    public void checkInitialData() {
        konsultasi.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                String docPath = "konsultasi" + (task.getResult());
                Log.d("docPaths: ", docPath);
                currentId = task.getResult().size();
                konsultasi.document(docPath).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                        if (task.getResult().get("perilaku") != null && task.getResult().get("non-perilaku") == null) {
                            Log.d("onComplete: ", "Ada");
                            Intent i = new Intent(AspekPerilakuActivity.this, AspekNonPerilaku.class);
                            i.putExtra("docPath", docPath);
                            startActivity(i);
                            finish();
                        } else {
                            Log.d("onComplete: ", "Ga Ada, lanjut");
                        }
                    }
                });
            }
        });
    }

    public boolean getHasilKonsultasi(int bobot) {
        return bobot > 78;
    }
}