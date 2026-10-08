package com.ladokgi.apps;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.firestore.DocumentSnapshot;
import com.ladokgi.apps.data.SessionManager;
import com.ladokgi.apps.data.UserRepository;
import com.ladokgi.apps.databinding.FragmentAkunBinding;
import com.ladokgi.apps.login.LoginActivity;

import java.util.Calendar;

public class AkunFragment extends Fragment {

    private FragmentAkunBinding binding;
    private UserRepository userRepository;
    private com.google.firebase.firestore.ListenerRegistration registration;

    public AkunFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAkunBinding.inflate(inflater, container, false);
        SessionManager.init(requireContext());
        userRepository = new UserRepository();

        binding.btnEdit.setOnClickListener(v ->
                startActivity(new Intent(getActivity(), EditProfileActivity.class)));

        binding.btnLogout.setOnClickListener(v -> {
            SessionManager.clear();
            startActivity(new Intent(getActivity(), LoginActivity.class));
            requireActivity().finish();
        });

        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        registration = userRepository.observe(SessionManager.getUsername(), (value, error) -> {
            if (value == null || !value.exists() || binding == null) {
                return;
            }
            render(value);
        });
    }

    private void render(DocumentSnapshot value) {
        binding.nama.setText(orDash(value.getString("nama")));
        binding.alamat.setText(orDash(value.getString("alamat")));
        binding.telepon.setText(orDash(value.getString("telepon")));

        boolean isDokter = "dokter".equals(value.getString("role"));
        int visibility = isDokter ? View.GONE : View.VISIBLE;
        binding.labelJenkel.setVisibility(visibility);
        binding.labelPendidikan.setVisibility(visibility);
        binding.labelLama.setVisibility(visibility);
        binding.jenkel.setVisibility(visibility);
        binding.pendidikan.setVisibility(visibility);
        binding.lama.setVisibility(visibility);

        if (isDokter) {
            return;
        }
        binding.jenkel.setText(orDash(value.getString("jenis-kelamin")));
        binding.pendidikan.setText(orDash(value.getString("pendidikan")));
        binding.lama.setText(formatLama(value.getString("lama-menggunakan")));
    }

    private String formatLama(String tahunRaw) {
        try {
            int tahun = Integer.parseInt(tahunRaw);
            int lama = Calendar.getInstance().get(Calendar.YEAR) - tahun;
            return lama + " Tahun";
        } catch (Exception e) {
            return orDash(tahunRaw);
        }
    }

    private String orDash(String value) {
        return value == null || value.isEmpty() ? "-" : value;
    }

    @Override
    public void onDestroyView() {
        if (registration != null) {
            registration.remove();
            registration = null;
        }
        binding = null;
        super.onDestroyView();
    }
}
