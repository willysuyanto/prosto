package com.ladokgi.apps;

import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.ladokgi.apps.data.ApiCallback;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.data.UserRepository;
import com.ladokgi.apps.databinding.ActivityEditProfileBinding;
import com.ladokgi.apps.util.ProfileSpinners;

import java.util.HashMap;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private ActivityEditProfileBinding binding;
    private UserRepository userRepository;
    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionManager.init(this);
        userRepository = new UserRepository();
        ProfileSpinners.bind(binding.spn3, binding.spn, binding.spn2);

        boolean isDokter = SessionManager.isDokter();
        int spinnerVisibility = isDokter ? View.GONE : View.VISIBLE;
        binding.layot1.setVisibility(spinnerVisibility);
        binding.layot2.setVisibility(spinnerVisibility);
        binding.layot3.setVisibility(spinnerVisibility);

        String username = SessionManager.getUsername();
        registration = userRepository.observe(username, (value, error) -> {
            if (value == null || !value.exists()) {
                return;
            }
            binding.nama.setText(value.getString("nama"));
            binding.nomorTlp.setText(value.getString("telepon"));
            binding.alamat.setText(value.getString("alamat"));
            if (!isDokter) {
                ProfileSpinners.select(binding.spn, value.getString("pendidikan"));
                ProfileSpinners.select(binding.spn2, value.getString("lama-menggunakan"));
                ProfileSpinners.select(binding.spn3, value.getString("jenis-kelamin"));
            }
        });

        binding.btnSimpan.setOnClickListener(v -> {
            String nama = text(binding.nama);
            String telepon = text(binding.nomorTlp);
            String alamat = text(binding.alamat);

            if (nama.isEmpty()) {
                Toaster("Nama Lengkap Tidak Boleh Kosong");
                return;
            }
            if (telepon.isEmpty()) {
                Toaster("Nomor Telepon Tidak Boleh Kosong");
                return;
            }
            if (alamat.isEmpty()) {
                Toaster("Alamat Tidak Boleh Kosong");
                return;
            }
            if (!isDokter) {
                if (ProfileSpinners.isPlaceholder(binding.spn3)) {
                    Toaster("Pilih Jenis Kelamin");
                    return;
                }
                if (ProfileSpinners.isPlaceholder(binding.spn)) {
                    Toaster("Pilih Pendidikan Terakhir");
                    return;
                }
                if (ProfileSpinners.isPlaceholder(binding.spn2)) {
                    Toaster("Pilih Tahun Menggunakan Gigi Tiruan");
                    return;
                }
            }

            Map<String, Object> user = new HashMap<>();
            user.put("nama", nama);
            user.put("telepon", telepon);
            user.put("alamat", alamat);
            if (!isDokter) {
                user.put("pendidikan", binding.spn.getText().toString());
                user.put("lama-menggunakan", binding.spn2.getText().toString());
                user.put("jenis-kelamin", binding.spn3.getText().toString());
            }

            binding.btnSimpan.setEnabled(false);
            userRepository.updateProfile(username, user, new ApiCallback.SuccessError() {
                @Override
                public void onSuccess() {
                    Toaster("Berhasil Ubah Profil");
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 800);
                }

                @Override
                public void onFailure(Exception e) {
                    binding.btnSimpan.setEnabled(true);
                    Toaster("Gagal mengubah profil");
                }
            });
        });
    }

    @Override
    protected void onDestroy() {
        if (registration != null) {
            registration.remove();
        }
        super.onDestroy();
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private void Toaster(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
