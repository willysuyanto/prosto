package com.ladokgi.apps.daftarpasien;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.ladokgi.apps.CreatePasienActivity;
import com.ladokgi.apps.R;
import com.ladokgi.apps.data.FirestoreKeys;
import com.ladokgi.apps.databinding.ActivityDaftarPasienBinding;
import com.ladokgi.apps.utils.ExportCSV;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class DaftarPasienActivity extends AppCompatActivity implements PasienAdapter.PasienAdapterCallback {

    private ActivityDaftarPasienBinding binding;
    private final List<Pasien> allPasiens = new ArrayList<>();
    private final List<Pasien> pasiens = new ArrayList<>();
    private String query = "";
    private PasienAdapter adapter;
    private ProgressDialog dialog;
    private ExportCSV exportCSV;
    private ListenerRegistration registration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDaftarPasienBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        exportCSV = new ExportCSV(this, this);
        adapter = new PasienAdapter(getApplicationContext(), pasiens, this);
        binding.rvPasien.setLayoutManager(new LinearLayoutManager(this));
        binding.rvPasien.setAdapter(adapter);

        dialog = ProgressDialog.show(this, "Mohon Tunggu", "Sedang Memuat Data", true);

        binding.toolbars.setOnMenuItemClickListener(item -> {
            if (item.getItemId() == R.id.download) {
                exportDataPasienInCSV();
            }
            return true;
        });

        binding.fab.setOnClickListener(v ->
                startActivity(new Intent(DaftarPasienActivity.this, CreatePasienActivity.class)));

        binding.etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                query = s == null ? "" : s.toString().toLowerCase(Locale.ROOT);
                applyFilter();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        registration = FirebaseFirestore.getInstance()
                .collection(FirestoreKeys.USERS)
                .whereEqualTo(FirestoreKeys.ROLE, FirestoreKeys.ROLE_PASIEN)
                .addSnapshotListener((value, error) -> {
                    if (dialog.isShowing()) {
                        dialog.hide();
                    }
                    if (error != null || value == null) {
                        return;
                    }
                    allPasiens.clear();
                    for (DocumentSnapshot ds : value) {
                        allPasiens.add(new Pasien(ds.getId(), ds.getString(FirestoreKeys.NAMA),
                                ds.getString("alamat"), ds.getString("telepon"),
                                ds.getString("jenis-kelamin"), ds.getString("pendidikan"),
                                ds.getString("lama-menggunakan")));
                    }
                    applyFilter();
                });
    }

    private void applyFilter() {
        pasiens.clear();
        if (query.isEmpty()) {
            pasiens.addAll(allPasiens);
        } else {
            for (Pasien pasien : allPasiens) {
                if (matches(pasien)) {
                    pasiens.add(pasien);
                }
            }
        }
        adapter.notifyDataSetChanged();
        binding.tvEmpty.setVisibility(pasiens.isEmpty() ? android.view.View.VISIBLE
                : android.view.View.GONE);
    }

    private boolean matches(Pasien pasien) {
        return contains(pasien.getNama()) || contains(pasien.getTelepon())
                || contains(pasien.getAlamat());
    }

    private boolean contains(String value) {
        return value != null && value.toLowerCase(Locale.ROOT).contains(query);
    }

    @Override
    protected void onDestroy() {
        if (registration != null) {
            registration.remove();
        }
        if (dialog != null && dialog.isShowing()) {
            dialog.dismiss();
        }
        super.onDestroy();
    }

    @Override
    public void onEditClicked(int position) {
        Intent intent = new Intent(DaftarPasienActivity.this, EditPasienActivity.class);
        intent.putExtra("pasien", pasiens.get(position));
        startActivity(intent);
    }

    @Override
    public void onDeleteClicked(int position) {
        Pasien pasien = pasiens.get(position);
        new AlertDialog.Builder(DaftarPasienActivity.this)
                .setTitle(R.string.action_confirm_title)
                .setMessage("Hapus data pasien " + pasien.getNama() + "?")
                .setPositiveButton(R.string.action_confirm, (DialogInterface.OnClickListener) (dialog, which) ->
                        FirebaseFirestore.getInstance()
                                .collection(FirestoreKeys.USERS)
                                .document(pasien.getId())
                                .delete()
                                .addOnCompleteListener(task -> {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(DaftarPasienActivity.this,
                                                "Berhasil hapus data pasien", Toast.LENGTH_SHORT).show();
                                    }
                                }))
                .setNegativeButton("Batal", (DialogInterface.OnClickListener) (dialog, which) -> dialog.dismiss())
                .show();
    }

    @Override
    public void onResetPasswordClicked(int position) {
        Intent intent = new Intent(DaftarPasienActivity.this, ResetPasswordActivity.class);
        intent.putExtra("pasien", pasiens.get(position));
        startActivity(intent);
    }

    void exportDataPasienInCSV() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Nama Pasien", "Jenis Kelamin", "Alamat", "Nomor Telepon", "Pendidikan Terakhir", "Lama Menggunakan"});
        for (Pasien pasien : pasiens) {
            data.add(new String[]{pasien.nama, pasien.jenisK, pasien.alamat, pasien.telepon, pasien.pendidikan, pasien.lama});
        }

        Calendar cal = Calendar.getInstance();
        String timestamp = cal.get(Calendar.YEAR) + "-" + (cal.get(Calendar.MONTH) + 1) + "-" + cal.get(Calendar.DATE);
        exportCSV.exportCSV(data, "data_pasien_" + timestamp + ".csv");
    }
}
