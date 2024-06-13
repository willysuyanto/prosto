package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
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

    CustomSeekbar sb1, sb2, sb3, sb4, sb5, sb6, sb7, sb8, sb9, sb10;
    LinearLayout p1, p2, p3, p4, p5, p6, p7, p8, p9, p10;

    Button btnKirim;

    String docPath = "";

    public void initializeSeekbar(Context context) {
        // Instantiate Custom Seekbar
        sb1 = new CustomSeekbar(context, 7, R.color.black, "Sering", "Tidak Pernah");
        sb2 = new CustomSeekbar(context, 7, R.color.black, "Sering", "Tidak Pernah");
        sb3 = new CustomSeekbar(context, 7, R.color.black, "Sering", "Tidak Pernah");
        sb4 = new CustomSeekbar(context, 7, R.color.black, "Sering", "Tidak Pernah");
        sb5 = new CustomSeekbar(context, 7, R.color.black, "Sering", "Tidak Pernah");
        sb6 = new CustomSeekbar(context, 7, R.color.black, "Sering", "Tidak Pernah");
        sb7 = new CustomSeekbar(context, 7, R.color.black, "Sering", "Tidak Pernah");
        sb8 = new CustomSeekbar(context, 7, R.color.black, "Banyak", "Tidak Ada");
        sb9 = new CustomSeekbar(context, 7, R.color.black, "Banyak", "Tidak Ada");
        sb10 = new CustomSeekbar(context, 7, R.color.black, "Banyak", "Tidak Ada");

        // Bind seekbar parent view (LinearLayout)
        p1 = findViewById(R.id.pertanyaan1);
        p2 = findViewById(R.id.pertanyaan2);
        p3 = findViewById(R.id.pertanyaan3);
        p4 = findViewById(R.id.pertanyaan4);
        p5 = findViewById(R.id.pertanyaan5);
        p6 = findViewById(R.id.pertanyaan6);
        p7 = findViewById(R.id.pertanyaan7);
        p8 = findViewById(R.id.pertanyaan8);
        p9 = findViewById(R.id.pertanyaan9);
        p10 = findViewById(R.id.pertanyaan10);


        // add seekbar to its view
        sb1.addSeekBar(p1);
        sb2.addSeekBar(p2);
        sb3.addSeekBar(p3);
        sb4.addSeekBar(p4);
        sb5.addSeekBar(p5);
        sb6.addSeekBar(p6);
        sb7.addSeekBar(p7);
        sb8.addSeekBar(p8);
        sb9.addSeekBar(p9);
        sb10.addSeekBar(p10);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_aspek_non_perilaku);

        btnKirim = findViewById(R.id.btn_kirim);
        initializeSeekbar(this.getApplicationContext());

        Bundle extras = getIntent().getExtras();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Hawk.init(getApplicationContext()).build();
        String currentUser = Hawk.get("username");
        CollectionReference konsultasi = db.collection("users").document(currentUser).collection("konsultasi");
        if (extras != null) {
            docPath = extras.getString("docPath");
            Log.d( "Non-Perilaku", docPath);
        }

        btnKirim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int totalBobot = 0;
                Map<String,Object> nonPerilaku = new HashMap<>();
                Map<String,Object> aspek = new HashMap<>();

                Map<String, Object> pertanyaan1 = new HashMap<>();
                pertanyaan1.put("Bobot", sb1.getValue());
                nonPerilaku.put("pertanyaan1", pertanyaan1);
                totalBobot += sb1.getValue();

                Map<String, Object> pertanyaan2 = new HashMap<>();
                pertanyaan2.put("Bobot", sb2.getValue());
                nonPerilaku.put("pertanyaan2", pertanyaan2);
                totalBobot += sb2.getValue();

                Map<String, Object> pertanyaan3 = new HashMap<>();
                pertanyaan3.put("Bobot", sb3.getValue());
                nonPerilaku.put("pertanyaan3", pertanyaan3);
                totalBobot += sb3.getValue();

                Map<String, Object> pertanyaan4 = new HashMap<>();
                pertanyaan4.put("Bobot", sb4.getValue());
                nonPerilaku.put("pertanyaan4", pertanyaan4);
                totalBobot += sb4.getValue();

                Map<String, Object> pertanyaan5 = new HashMap<>();
                pertanyaan5.put("Bobot", sb5.getValue());
                nonPerilaku.put("pertanyaan5", pertanyaan5);
                totalBobot += sb5.getValue();

                Map<String, Object> pertanyaan6 = new HashMap<>();
                pertanyaan6.put("Bobot", sb6.getValue());
                nonPerilaku.put("pertanyaan6", pertanyaan6);
                totalBobot += sb6.getValue();

                Map<String, Object> pertanyaan7 = new HashMap<>();
                pertanyaan7.put("Bobot", sb7.getValue());
                nonPerilaku.put("pertanyaan7", pertanyaan7);
                totalBobot += sb7.getValue();

                Map<String, Object> pertanyaan8 = new HashMap<>();
                pertanyaan8.put("Bobot", sb8.getValue());
                nonPerilaku.put("pertanyaan8", pertanyaan8);
                totalBobot += sb8.getValue();

                Map<String, Object> pertanyaan9 = new HashMap<>();
                pertanyaan9.put("Bobot", sb6.getValue());
                nonPerilaku.put("pertanyaan9", pertanyaan9);
                totalBobot += sb9.getValue();

                Map<String, Object> pertanyaan10 = new HashMap<>();
                pertanyaan10.put("Bobot", sb10.getValue());
                nonPerilaku.put("pertanyaan10", pertanyaan10);
                totalBobot += sb10.getValue();

                nonPerilaku.put("total-bobot", totalBobot);
                nonPerilaku.put("hasil",getHasilKonsultasi(totalBobot));
                aspek.put("status", "pending");
                aspek.put("non-perilaku", nonPerilaku);
                konsultasi.document(docPath).set(aspek, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Intent i = new Intent(AspekNonPerilaku.this, UploadGambarGigi.class);
                            i.putExtra("docPath",docPath);
                            startActivity(i);
                            finish();
                        }
                    }
                });
            }
        });


    }
    public boolean getHasilKonsultasi(int bobot){
        return bobot < 36;
    }
}