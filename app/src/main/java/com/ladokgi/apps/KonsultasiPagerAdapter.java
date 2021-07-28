package com.ladokgi.apps;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import org.jetbrains.annotations.NotNull;

public class KonsultasiPagerAdapter extends FragmentStateAdapter {
    private String[] titles = new String[]{"Belum direview", "Sudah direview"};
    public KonsultasiPagerAdapter(@NonNull @NotNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    public KonsultasiPagerAdapter(@NonNull @NotNull Fragment fragment) {
        super(fragment);
    }

    public KonsultasiPagerAdapter(@NonNull @NotNull FragmentManager fragmentManager, @NonNull @NotNull Lifecycle lifecycle) {
        super(fragmentManager, lifecycle);
    }

    @NonNull
    @NotNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                return new KonsultasiReviewFragment();
            case 1:
                return new KonsultasiDoneFragment();
        }
        return new KonsultasiReviewFragment();
    }

    @Override
    public int getItemCount() {
        return titles.length;
    }
}
