package com.ladokgi.apps.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.ladokgi.apps.R;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.data.UserRepository;
import com.ladokgi.apps.databinding.ActivityLoginBinding;
import com.ladokgi.apps.homepage.DokterActivity;
import com.ladokgi.apps.homepage.MainActivity;
import com.ladokgi.apps.register.RegistrasiActivity;
import com.orhanobut.hawk.Hawk;

public class LoginActivity extends AppCompatActivity {

    private ActivityLoginBinding binding;
    private UserRepository userRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Hawk.init(getApplicationContext()).build();
        SessionManager.init(this);
        userRepository = new UserRepository();

        if (SessionManager.isLoggedIn()) {
            redirectByRole(SessionManager.getRole());
            return;
        }

        binding.btnDaftar.setOnClickListener(v -> {
            Intent i = new Intent(LoginActivity.this, RegistrasiActivity.class);
            startActivity(i);
        });

        binding.btnLogin.setOnClickListener(v -> {
            String username = binding.etUsername.getText() != null ? binding.etUsername.getText().toString().trim() : "";
            String password = binding.etPassword.getText() != null ? binding.etPassword.getText().toString().trim() : "";
            if (username.isEmpty()) {
                Toaster("Username Tidak Boleh Kosong");
                return;
            }
            if (password.isEmpty()) {
                Toaster("Password Tidak Boleh Kosong");
                return;
            }
            binding.btnLogin.setEnabled(false);
            userRepository.login(username, password, new UserRepository.LoginCallback() {
                @Override
                public void onLoginSuccess(DocumentSnapshot result) {
                    binding.btnLogin.setEnabled(true);
                    Toaster("Login Berhasil");
                    SessionManager.setUser(result);
                    new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> redirectByRole(SessionManager.getRole()), 500);
                }

                @Override
                public void onLoginFailure(String message) {
                    binding.btnLogin.setEnabled(true);
                    Toaster(message != null ? message : "Login Gagal");
                }
            });
        });
    }

    private void redirectByRole(String role) {
        Intent i;
        if ("dokter".equals(role)) {
            i = new Intent(LoginActivity.this, DokterActivity.class);
        } else {
            i = new Intent(LoginActivity.this, MainActivity.class);
        }
        startActivity(i);
        finish();
    }

    private void Toaster(String text) {
        Toast.makeText(getApplicationContext(), text, Toast.LENGTH_SHORT).show();
    }
}
