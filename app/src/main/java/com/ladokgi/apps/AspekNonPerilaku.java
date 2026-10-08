package com.ladokgi.apps;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.databinding.ActivityAspekNonPerilakuBinding;
import com.ladokgi.apps.model.QuestionnaireData;
import com.ladokgi.apps.ui.QuestionnaireAdapter;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class AspekNonPerilaku extends AppCompatActivity {

    private ActivityAspekNonPerilakuBinding binding;
    private List<QuestionnaireData.Group> groups;

    private String docPath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAspekNonPerilakuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Hawk.init(getApplicationContext()).build();
        SessionManager.init(this);
        String currentUser = SessionManager.getUsername();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        CollectionReference notifikasi = db.collection("users").document(currentUser).collection("notifikasi");
        CollectionReference konsultasi = db.collection("users").document(currentUser).collection("konsultasi");

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            docPath = extras.getString("docPath", "");
        }

        groups = QuestionnaireData.nonPerilaku();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(new QuestionnaireAdapter(groups));
        binding.recycler.setNestedScrollingEnabled(false);

        binding.btnKirim.setOnClickListener(v -> {
            int totalBobot = 0;
            Map<String, Object> nonPerilaku = new HashMap<>();
            for (QuestionnaireData.Group group : groups) {
                int value = group.values[0];
                Map<String, Object> entry = new HashMap<>();
                entry.put("Bobot", value);
                nonPerilaku.put(QuestionnaireData.nonPerilakuKey(group.titleNumber), entry);
                totalBobot += value;
            }
            nonPerilaku.put("total-bobot", totalBobot);
            nonPerilaku.put("hasil", totalBobot < 36);

            Map<String, Object> aspek = new HashMap<>();
            aspek.put("status", "pending");
            aspek.put("non-perilaku", nonPerilaku);

            binding.btnKirim.setEnabled(false);
            konsultasi.document(docPath).set(aspek, SetOptions.merge()).addOnCompleteListener(task -> {
                binding.btnKirim.setEnabled(true);
                if (task.isSuccessful()) {
                    Map<String, Object> data = new HashMap<>();
                    data.put("title", "Konsultasi Selesai");
                    data.put("message", "Terima kasih telah melakukan konsultasi. Konsultasi anda akan segera ditinjau oleh Dokter.");
                    data.put("date", new SimpleDateFormat("dd MMMM yyyy HH.mm", Locale.getDefault()).format(new Date()));
                    notifikasi.document(UUID.randomUUID().toString()).set(data);

                    Intent i = new Intent(AspekNonPerilaku.this, UploadGambarGigi.class);
                    i.putExtra("docPath", docPath);
                    startActivity(i);
                    finish();
                }
            });
        });
    }
}
