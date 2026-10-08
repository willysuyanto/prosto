package com.ladokgi.apps.konsultasi;

import android.net.Uri;
import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.ladokgi.apps.data.ApiCallback;
import com.ladokgi.apps.data.ConsultationRepository;
import com.ladokgi.apps.databinding.ActivityKonsultasiReviewBinding;

public class KonsultasiReviewActivity extends AppCompatActivity {

    private ActivityKonsultasiReviewBinding binding;
    private final ConsultationRepository repository = new ConsultationRepository();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityKonsultasiReviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Bundle bundle = getIntent().getExtras();
        String idKonsultasi = bundle != null ? bundle.getString("idKonsultasi") : null;
        String userName = bundle != null ? bundle.getString("username") : null;
        if (idKonsultasi == null || userName == null) {
            finish();
            return;
        }

        repository.getConsultation(userName, idKonsultasi).addOnCompleteListener(task -> {
            if (!task.isSuccessful() || task.getResult() == null || !task.getResult().exists()) {
                return;
            }
            com.google.firebase.firestore.DocumentSnapshot doc = task.getResult();
            String imagePath = doc.getString("gambar_gigi");
            if (imagePath != null) {
                FirebaseStorage.getInstance().getReference().child(imagePath)
                        .getDownloadUrl()
                        .addOnSuccessListener(uri -> Glide.with(getApplicationContext())
                                .load(uri.toString())
                                .into(binding.gambarGigi));
            }

            Object totalP = doc.get("perilaku.total-bobot");
            Object totalNp = doc.get("non-perilaku.total-bobot");
            boolean hasilP = Boolean.TRUE.equals(doc.getBoolean("perilaku.hasil"));
            boolean hasilNp = Boolean.TRUE.equals(doc.getBoolean("non-perilaku.hasil"));

            binding.bobotP.setText((totalP != null ? totalP : "-") + (hasilP ? " (+)" : " (-)"));
            binding.bobotNp.setText((totalNp != null ? totalNp : "-") + (hasilNp ? " (+)" : " (-)"));
            binding.kesimpulan.setText(calculateHasil(hasilP, hasilNp));
        });

        binding.btnCofirm.setOnClickListener(v -> {
            binding.btnCofirm.setEnabled(false);
            repository.confirmConsultation(userName, idKonsultasi, new ApiCallback.SuccessError() {
                @Override
                public void onSuccess() {
                    Toast.makeText(getApplicationContext(), "Konsultasi dikonfirmasi", Toast.LENGTH_SHORT).show();
                    finish();
                }

                @Override
                public void onFailure(Exception e) {
                    binding.btnCofirm.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "Gagal mengonfirmasi konsultasi", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }

    private String calculateHasil(boolean h1, boolean h2) {
        if (h1 && h2) {
            return "Tidak disarankan berobat";
        } else if (h1 || h2) {
            return "Disarankan berobat";
        }
        return "Harus berobat";
    }
}
