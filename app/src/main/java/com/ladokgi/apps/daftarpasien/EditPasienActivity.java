package com.ladokgi.apps.daftarpasien;

import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ladokgi.apps.R;
import com.ladokgi.apps.data.ApiCallback;
import com.ladokgi.apps.data.UserRepository;
import com.ladokgi.apps.databinding.ActivityEditPasienBinding;
import com.ladokgi.apps.util.ProfileSpinners;

import java.util.HashMap;
import java.util.Map;

public class EditPasienActivity extends AppCompatActivity {

    private ActivityEditPasienBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityEditPasienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository();
        ProfileSpinners.bind(binding.spn3, binding.spn, binding.spn2);

        Pasien editData = (Pasien) getIntent().getSerializableExtra("pasien");

        binding.nama.setText(editData.getNama());
        binding.alamat.setText(editData.getAlamat());
        binding.nomorTlp.setText(editData.getTelepon());
        ProfileSpinners.select(binding.spn, editData.getPendidikan());
        ProfileSpinners.select(binding.spn2, editData.getLama());
        ProfileSpinners.select(binding.spn3, editData.getJenisK());

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
                Toaster("Pilih Tahun");
                return;
            }

            Map<String, Object> data = new HashMap<>();
            data.put("nama", nama);
            data.put("alamat", alamat);
            data.put("telepon", telepon);
            data.put("jenis-kelamin", binding.spn3.getText().toString());
            data.put("pendidikan", binding.spn.getText().toString());
            data.put("lama-menggunakan", binding.spn2.getText().toString());

            binding.btnSimpan.setEnabled(false);
            userRepository.updateProfile(editData.getId(), data, new ApiCallback.SuccessError() {
                @Override
                public void onSuccess() {
                    Toaster("Berhasil Update Data Pasien");
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 800);
                }

                @Override
                public void onFailure(Exception e) {
                    binding.btnSimpan.setEnabled(true);
                    Toaster("Gagal update data pasien");
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
