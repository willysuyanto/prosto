package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class KonsultasiReviewActivity extends AppCompatActivity {

    TextView jawaban1, jawaban2, jawaban3, jawaban4, jawaban5, jawaban6, jawaban7, jawaban8, jawaban9, jawabanCb1, jawabanCb2;
    TextView bobotP, bobotNp, kesimpulan;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultasi_review);

        jawaban1 = findViewById(R.id.jawaban_1);
        jawaban2 = findViewById(R.id.jawaban_2);
        jawaban3 = findViewById(R.id.jawaban_3);
        jawaban4 = findViewById(R.id.jawaban_4);
        jawaban5 = findViewById(R.id.jawaban_5);
        jawaban6 = findViewById(R.id.jawaban_6);
        jawaban7 = findViewById(R.id.jawaban_7);
        jawaban8 = findViewById(R.id.jawaban_8);
        jawaban9 = findViewById(R.id.jawaban_9);
        jawabanCb1 = findViewById(R.id.jawabancb_1);
        jawabanCb2 = findViewById(R.id.jawabancb_2);
        bobotP = findViewById(R.id.bobot_p);
        bobotNp = findViewById(R.id.bobot_np);
        kesimpulan = findViewById(R.id.kesimpulan);
        btnConfirm = findViewById(R.id.btn_cofirm);


        Bundle bundle = getIntent().getExtras();
        Log.d("onCreate: ", bundle.getString("idKonsultasi"));
        Log.d("onCreate: ", bundle.getString("username"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        String idKonsultasi = bundle.getString("idKonsultasi");
        String userName = bundle.getString("username");
        db.collection("users").document(userName).collection("konsultasi").document(idKonsultasi).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    jawaban1.setText(task.getResult().getString("perilaku.pertanyaan1.Jawaban"));
                    jawaban2.setText(task.getResult().getString("perilaku.pertanyaan2.Jawaban"));
                    jawaban3.setText(task.getResult().getString("perilaku.pertanyaan3.Jawaban"));
                    jawaban4.setText(task.getResult().getString("perilaku.pertanyaan4.Jawaban"));
                    jawabanCb1.setText(task.getResult().get("perilaku.pertanyaanCb1.Jawaban").toString());
                    jawabanCb2.setText(task.getResult().get("perilaku.pertanyaanCb2.Jawaban").toString());
                    jawaban5.setText(task.getResult().getString("non-perilaku.pertanyaan1.Jawaban"));
                    jawaban6.setText(task.getResult().getString("non-perilaku.pertanyaan2.Jawaban"));
                    jawaban7.setText(task.getResult().getString("non-perilaku.pertanyaan3.Jawaban"));
                    jawaban8.setText(task.getResult().getString("non-perilaku.pertanyaan4.Jawaban"));
                    jawaban9.setText(task.getResult().getString("non-perilaku.pertanyaan5.Jawaban"));
                    String sBobotP = task.getResult().get("perilaku.total-bobot").toString();
                    String sBobotNp = task.getResult().get("non-perilaku.total-bobot").toString();
                    boolean hasilP = task.getResult().getBoolean("perilaku.hasil");
                    boolean hasilNp = task.getResult().getBoolean("non-perilaku.hasil");
                    if(hasilP){
                        sBobotP+=" (+)";
                    } else {
                        sBobotP+=" (-)";
                    }
                    if(hasilNp){
                        sBobotNp+=" (+)";
                    } else {
                        sBobotNp+=" (-)";
                    }

                    bobotP.setText(sBobotP);
                    bobotNp.setText(sBobotNp);
                    kesimpulan.setText(calculateHasil(hasilP,hasilNp));
                }
            }
        });
        btnConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                db.collection("users").document(userName).collection("konsultasi").document(idKonsultasi).update("status","confirmed").addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull @NotNull Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getApplicationContext(),"Konsultasi dikonfirmasi", Toast.LENGTH_SHORT).show();
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
        });
    }
    private String calculateHasil(boolean h1, boolean h2){
        if(h1 && h2){
            return "Tidak disarankan berobat";
        }else if(h1 || h2){
            return "Disarankan berobat";
        }else {
            return "Harus berobat";
        }
    }
}