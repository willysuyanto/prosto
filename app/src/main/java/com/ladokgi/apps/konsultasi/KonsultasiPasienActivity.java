package com.ladokgi.apps.konsultasi;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.tabs.TabLayoutMediator;
import com.ladokgi.apps.databinding.ActivityKonsultasiPasienBinding;

public class KonsultasiPasienActivity extends AppCompatActivity {

    private final String[] titles = new String[]{"Belum direview", "Sudah direview"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityKonsultasiPasienBinding binding = ActivityKonsultasiPasienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.pager.setAdapter(new KonsultasiPagerAdapter(this));
        new TabLayoutMediator(binding.tabLayout, binding.pager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();
    }
}
