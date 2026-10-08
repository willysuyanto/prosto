package com.ladokgi.apps.daftarpasien;

import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.ladokgi.apps.R;
import com.ladokgi.apps.data.ApiCallback;
import com.ladokgi.apps.data.UserRepository;
import com.ladokgi.apps.databinding.ActivityResetPasswordBinding;

public class ResetPasswordActivity extends AppCompatActivity {

    private ActivityResetPasswordBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityResetPasswordBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        userRepository = new UserRepository();
        Pasien editData = (Pasien) getIntent().getSerializableExtra("pasien");

        binding.btnSimpanPassword.setOnClickListener(v -> {
            String password = binding.password.getText() != null ? binding.password.getText().toString() : "";
            String confirm = binding.passwordConfirm.getText() != null ? binding.passwordConfirm.getText().toString() : "";
            if (password.isEmpty()) {
                Toaster("Password Tidak Boleh Kosong");
            } else if (confirm.isEmpty()) {
                Toaster("Password Konfirmasi Tidak Boleh Kosong");
            } else if (!password.equals(confirm)) {
                Toaster("Password dan Password Konfirmasi Tidak Sama");
            } else {
                binding.btnSimpanPassword.setEnabled(false);
                userRepository.resetPassword(editData.getId(), password, new ApiCallback.SuccessError() {
                    @Override
                    public void onSuccess() {
                        Toaster("Berhasil Ubah Password");
                        new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> finish(), 800);
                    }

                    @Override
                    public void onFailure(Exception e) {
                        binding.btnSimpanPassword.setEnabled(true);
                        Toaster("Gagal mengubah password");
                    }
                });
            }
        });
    }

    private void Toaster(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
