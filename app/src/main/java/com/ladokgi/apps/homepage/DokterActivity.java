package com.ladokgi.apps.homepage;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.ladokgi.apps.AkunFragment;
import com.ladokgi.apps.R;

import org.jetbrains.annotations.NotNull;

public class DokterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dokter);

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation_dokter);
        bottomNav.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull @NotNull MenuItem item) {
                Fragment active = null;
                Fragment home = new DashboardDokterFragment();
                Fragment akun = new AkunFragment();

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        active = home;
                        break;
                    case R.id.nav_akun:
                        active = akun;
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        active).commit();

                return true;
            }
        });
        //I added this if statement to keep the selected fragment when rotating the device
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new DashboardDokterFragment()).commit();
        }
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DokterActivity.this);
        builder.setMessage("Keluar dari aplikasi ?");
        builder.setTitle("Konfirmasi");
        builder.setCancelable(false);
        builder.setPositiveButton("Ya", (DialogInterface.OnClickListener) (dialog, which) -> {
            finish();
        });
        builder.setNegativeButton("Tidak", (DialogInterface.OnClickListener) (dialog, which) -> {
            dialog.cancel();
        });

        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}