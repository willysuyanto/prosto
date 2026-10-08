package com.ladokgi.apps;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.databinding.ActivityAspekPerilakuBinding;
import com.ladokgi.apps.model.QuestionnaireData;
import com.ladokgi.apps.ui.QuestionnaireAdapter;
import com.orhanobut.hawk.Hawk;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class AspekPerilakuActivity extends AppCompatActivity {

    private ActivityAspekPerilakuBinding binding;
    private List<QuestionnaireData.Group> groups;
    private CollectionReference konsultasi;
    private String currentUser;
    private int currentId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityAspekPerilakuBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Hawk.init(getApplicationContext()).build();
        SessionManager.init(this);
        currentUser = SessionManager.getUsername();

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        konsultasi = db.collection("users").document(currentUser).collection("konsultasi");

        groups = QuestionnaireData.perilaku();
        binding.recycler.setLayoutManager(new LinearLayoutManager(this));
        binding.recycler.setAdapter(new QuestionnaireAdapter(groups));
        binding.recycler.setNestedScrollingEnabled(false);

        checkInitialData();

        binding.btnKirim.setOnClickListener(v -> submit());
    }

    private void submit() {
        int totalBobot = 0;
        Map<String, Object> perilaku = new HashMap<>();
        for (QuestionnaireData.Group group : groups) {
            for (int i = 0; i < group.sliders.size(); i++) {
                Map<String, Object> entry = new HashMap<>();
                entry.put("Bobot", group.values[i]);
                perilaku.put(QuestionnaireData.perilakuKey(group.titleNumber, i), entry);
                totalBobot += group.values[i];
            }
        }
        perilaku.put("total-bobot", totalBobot);
        perilaku.put("hasil", totalBobot > 78);

        Map<String, Object> aspek = new HashMap<>();
        aspek.put("perilaku", perilaku);
        aspek.put("tanggal-konsultasi",
                new SimpleDateFormat("dd MMMM yyyy", Locale.getDefault()).format(new Date()));

        binding.btnKirim.setEnabled(false);
        String docPath = "konsultasi" + (currentId + 1);
        konsultasi.document(docPath).set(aspek).addOnCompleteListener(task -> {
            binding.btnKirim.setEnabled(true);
            if (task.isSuccessful()) {
                Intent i = new Intent(AspekPerilakuActivity.this, AspekNonPerilaku.class);
                i.putExtra("docPath", docPath);
                startActivity(i);
                finish();
            }
        });
    }

    private void checkInitialData() {
        konsultasi.get().addOnCompleteListener((Task<QuerySnapshot> task) -> {
            if (!task.isSuccessful() || task.getResult() == null) {
                return;
            }
            currentId = task.getResult().size();
            String docPath = "konsultasi" + (currentId + 1);
            konsultasi.document(docPath).get().addOnCompleteListener(d -> {
                if (!d.isSuccessful() || d.getResult() == null || !d.getResult().exists()) {
                    return;
                }
                boolean hasPerilaku = d.getResult().get("perilaku") != null;
                boolean hasNonPerilaku = d.getResult().get("non-perilaku") != null;
                if (hasPerilaku && !hasNonPerilaku) {
                    Intent i = new Intent(AspekPerilakuActivity.this, AspekNonPerilaku.class);
                    i.putExtra("docPath", docPath);
                    startActivity(i);
                    finish();
                }
            });
        });
    }
}
