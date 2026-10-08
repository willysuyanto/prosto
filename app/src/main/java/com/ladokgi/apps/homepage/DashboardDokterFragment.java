package com.ladokgi.apps.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ladokgi.apps.daftarpasien.DaftarPasienActivity;
import com.ladokgi.apps.databinding.FragmentDashboardDokterBinding;
import com.ladokgi.apps.konsultasi.KonsultasiPasienActivity;

public class DashboardDokterFragment extends Fragment {

    private FragmentDashboardDokterBinding binding;

    public DashboardDokterFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardDokterBinding.inflate(inflater, container, false);

        binding.cardKonsultasi.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), KonsultasiPasienActivity.class)));

        binding.cardPasien.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), DaftarPasienActivity.class)));

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
