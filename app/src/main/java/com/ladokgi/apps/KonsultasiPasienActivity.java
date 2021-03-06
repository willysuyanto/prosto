package com.ladokgi.apps;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Bundle;

import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.ladokgi.apps.databinding.ActivityKonsultasiPasienBinding;

public class KonsultasiPasienActivity extends AppCompatActivity {
    ActivityKonsultasiPasienBinding binding;

    private String[] titles = new String[]{"Belum direview", "Sudah direview"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_konsultasi_pasien);
        TabLayout tabLayout = findViewById(R.id.tab_layout);
        ViewPager2 viewPager = findViewById(R.id.pager);
        viewPager.setAdapter(new KonsultasiPagerAdapter(this));
        new TabLayoutMediator(tabLayout, viewPager,
                (tab, position) -> tab.setText(titles[position])
        ).attach();

    }
}