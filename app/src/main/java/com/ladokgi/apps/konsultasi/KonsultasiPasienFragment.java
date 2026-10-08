package com.ladokgi.apps.konsultasi;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.ladokgi.apps.AspekPerilakuActivity;
import com.ladokgi.apps.databinding.FragmentKonsultasiPasienBinding;

public class KonsultasiPasienFragment extends Fragment {

    public KonsultasiPasienFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        FragmentKonsultasiPasienBinding binding =
                FragmentKonsultasiPasienBinding.inflate(inflater, container, false);
        binding.btnKonsultasi.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AspekPerilakuActivity.class)));
        return binding.getRoot();
    }
}
