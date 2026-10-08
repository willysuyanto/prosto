package com.ladokgi.apps.register;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ladokgi.apps.data.ApiCallback;
import com.ladokgi.apps.data.UserRepository;
import com.ladokgi.apps.databinding.ActivityRegistrasiBinding;
import com.ladokgi.apps.login.LoginActivity;
import com.ladokgi.apps.util.ProfileSpinners;

import java.util.HashMap;
import java.util.Map;

public class RegistrasiActivity extends AppCompatActivity {

    private ActivityRegistrasiBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityRegistrasiBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository();
        ProfileSpinners.bind(binding.spn3, binding.spn, binding.spn2);

        binding.btnDaftar.setOnClickListener(v -> submit());
    }

    private void submit() {
        if (binding.nama.getText().toString().equals("")) {
            Toaster("Nama Lengkap Tidak Boleh Kosong");
            return;
        }
        if (binding.nomorTlp.getText().toString().equals("")) {
            Toaster("Nomor Telepon Tidak Boleh Kosong");
            return;
        }
        if (binding.alamat.getText().toString().equals("")) {
            Toaster("Alamat Tidak Boleh Kosong");
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
            Toaster("Pilih Tahun Menggunakan Gigi Tiruan");
            return;
        }
        if (binding.username.getText().toString().equals("")) {
            Toaster("Username Tidak Boleh Kosong");
            return;
        }
        if (binding.password.getText().toString().equals("")) {
            Toaster("Password Tidak Boleh Kosong");
            return;
        }
        Map<String, Object> user = new HashMap<>();
        user.put("nama", binding.nama.getText().toString());
        user.put("telepon", binding.nomorTlp.getText().toString());
        user.put("alamat", binding.alamat.getText().toString());
        user.put("pendidikan", binding.spn.getText().toString());
        user.put("lama-menggunakan", binding.spn2.getText().toString());
        user.put("jenis-kelamin", binding.spn3.getText().toString());
        user.put("username", binding.username.getText().toString());
        user.put("role", "pasien");

        binding.btnDaftar.setEnabled(false);
        userRepository.register(user, binding.username.getText().toString(), binding.password.getText().toString(),
                new ApiCallback.SuccessError() {
                    @Override
                    public void onSuccess() {
                        binding.btnDaftar.setEnabled(true);
                        Toaster("Berhasil Buat Akun");
                        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                            Intent i = new Intent(RegistrasiActivity.this, LoginActivity.class);
                            startActivity(i);
                            finish();
                        }, 800);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        binding.btnDaftar.setEnabled(true);
                        String msg = e != null ? e.getMessage() : "Gagal mendaftar";
                        if ("exists".equals(msg)) {
                            Toaster("Username Sudah Digunakan");
                        } else {
                            Toaster(msg);
                        }
                    }
                });
    }

    private void Toaster(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
