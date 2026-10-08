package com.ladokgi.apps;

import android.os.Bundle;
import android.os.Looper;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ladokgi.apps.data.ApiCallback;
import com.ladokgi.apps.data.UserRepository;
import com.ladokgi.apps.databinding.ActivityCreatePasienBinding;
import com.ladokgi.apps.util.ProfileSpinners;

import java.util.HashMap;
import java.util.Map;

public class CreatePasienActivity extends AppCompatActivity {

    private ActivityCreatePasienBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityCreatePasienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository();
        ProfileSpinners.bind(binding.spn3, binding.spn, binding.spn2);

        binding.btnSimpan.setOnClickListener(v -> {
            String nama = text(binding.nama);
            String alamat = text(binding.alamat);
            String telepon = text(binding.nomorTlp);

            if (nama.isEmpty()) {
                Toaster("Nama tidak boleh kosong");
                return;
            }
            if (alamat.isEmpty()) {
                Toaster("Alamat tidak boleh kosong");
                return;
            }
            if (telepon.isEmpty()) {
                Toaster("Nomor Telepon tidak boleh kosong");
                return;
            }
            if (ProfileSpinners.isPlaceholder(binding.spn3)) {
                Toaster("Pilih Jenis Kelamin");
                return;
            }
            if (ProfileSpinners.isPlaceholder(binding.spn)) {
                Toaster("Pilih Pendidikan Terakhir");
                return;
            }
            if (ProfileSpinners.isPlaceholder(binding.spn2)) {
                Toaster("Pilih tahun menggunakan gigi tiruan");
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("nama", nama);
            data.put("alamat", alamat);
            data.put("telepon", telepon);
            data.put("pendidikan", binding.spn.getText().toString());
            data.put("jenis-kelamin", binding.spn3.getText().toString());
            data.put("lama-menggunakan", binding.spn2.getText().toString());
            data.put("username", telepon);
            data.put("role", "pasien");

            binding.btnSimpan.setEnabled(false);
            userRepository.register(data, telepon, telepon, new ApiCallback.SuccessError() {
                @Override
                public void onSuccess() {
                    Toaster("Berhasil Buat Data Pasien");
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 800);
                }

                @Override
                public void onFailure(Exception e) {
                    binding.btnSimpan.setEnabled(true);
                    String msg = e != null ? e.getMessage() : "";
                    if ("exists".equals(msg)) {
                        Toaster("Nomor telepon sudah terdaftar");
                    } else {
                        Toaster("Gagal membuat data pasien");
                    }
                }
            });
        });
    }

    private String text(android.widget.EditText editText) {
        return editText.getText() != null ? editText.getText().toString().trim() : "";
    }

    private void Toaster(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
