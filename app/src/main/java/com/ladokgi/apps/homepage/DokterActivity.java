package com.ladokgi.apps.homepage;

import android.content.DialogInterface;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.NavigationUI;

import com.ladokgi.apps.R;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.databinding.ActivityDokterBinding;

public class DokterActivity extends AppCompatActivity {

    private ActivityDokterBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDokterBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SessionManager.init(this);

        NavHostFragment navHost = (NavHostFragment) getSupportFragmentManager()
                .findFragmentById(R.id.nav_host);
        if (navHost != null) {
            NavController navController = navHost.getNavController();
            NavigationUI.setupWithNavController(binding.bottomNavigationDokter, navController);
        }

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                showExitConfirmation();
            }
        });
    }

    private void showExitConfirmation() {
        new AlertDialog.Builder(DokterActivity.this)
                .setMessage(R.string.exit_confirm_message)
                .setTitle(R.string.action_confirm_title)
                .setCancelable(false)
                .setPositiveButton(R.string.action_confirm, (DialogInterface.OnClickListener) (dialog, which) -> finish())
                .setNegativeButton(R.string.action_no, (DialogInterface.OnClickListener) (dialog, which) -> dialog.cancel())
                .show();
    }
}
