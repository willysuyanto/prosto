package com.ladokgi.apps.homepage;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.ladokgi.apps.AspekPerilakuActivity;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.databinding.FragmentDashboardPasienBinding;
import com.ladokgi.apps.konsultasi.Konsultasi;
import com.ladokgi.apps.konsultasi.KonsultasiAdapter;

import java.util.ArrayList;
import java.util.List;

public class DashboardPasienFragment extends Fragment implements KonsultasiAdapter.KonsultasiAdapterCallback {

    private FragmentDashboardPasienBinding binding;
    private final List<Konsultasi> list = new ArrayList<>();

    public DashboardPasienFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDashboardPasienBinding.inflate(inflater, container, false);
        SessionManager.init(requireContext());

        binding.btnConsultnow.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), AspekPerilakuActivity.class)));

        list.clear();
        KonsultasiAdapter adapter = new KonsultasiAdapter(getContext(), list, this);
        binding.rcv.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rcv.setAdapter(adapter);

        FirebaseFirestore.getInstance()
                .collection("users")
                .document(SessionManager.getUsername())
                .collection("konsultasi")
                .orderBy("tanggal-konsultasi", com.google.firebase.firestore.Query.Direction.DESCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (!task.isSuccessful() || task.getResult() == null || binding == null) {
                        return;
                    }
                    for (com.google.firebase.firestore.DocumentSnapshot data : task.getResult()) {
                        String tanggal = data.getString("tanggal-konsultasi");
                        boolean hasilP = Boolean.TRUE.equals(data.get("perilaku.hasil", Boolean.class));
                        boolean hasilNP = Boolean.TRUE.equals(data.get("non-perilaku.hasil", Boolean.class));
                        String status = data.getString("status");
                        if (status == null) {
                            status = "unfinished";
                        }
                        list.add(new Konsultasi(tanggal, hasilP, hasilNP, status));
                    }
                    if (!list.isEmpty()) {
                        binding.rcv.setVisibility(View.VISIBLE);
                        binding.lin.setVisibility(View.GONE);
                        adapter.notifyDataSetChanged();
                    }
                });

        return binding.getRoot();
    }

    @Override
    public void onRowKonsultasiAdapterClicked(int position) {
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
