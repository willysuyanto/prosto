package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class KonsultasiReviewActivity extends AppCompatActivity {

    TextView bobotP, bobotNp, kesimpulan;
    ImageView imageView;
    Button btnConfirm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultasi_review);
        bobotP = findViewById(R.id.bobot_p);
        bobotNp = findViewById(R.id.bobot_np);
        kesimpulan = findViewById(R.id.kesimpulan);
        btnConfirm = findViewById(R.id.btn_cofirm);
        imageView = findViewById(R.id.gambar_gigi);
        Bundle bundle = getIntent().getExtras();
        Log.d("onCreate: ", bundle.getString("idKonsultasi"));
        Log.d("onCreate: ", bundle.getString("username"));

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseStorage storage = FirebaseStorage.getInstance();

        String idKonsultasi = bundle.getString("idKonsultasi");
        String userName = bundle.getString("username");
        db.collection("users").document(userName).collection("konsultasi").document(idKonsultasi).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    StorageReference imageref = storage.getReference();
                    //Log.d( "onComplete: ", task.getResult().getString("gambar_gigi"));
                    if(task.getResult().getString("gambar_gigi")!=null){
                        imageref.child(task.getResult().getString("gambar_gigi"))
                                .getDownloadUrl()
                                .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                Glide.with(getApplicationContext())
                                        .load(uri.toString())
                                        .into(imageView);
                            }
                        });
                    }

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
                            finish();
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