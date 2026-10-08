package com.ladokgi.apps.konsultasi;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.ladokgi.apps.databinding.FragmentKonsultasiReviewBinding;
import com.ladokgi.apps.data.ConsultationRepository;
import com.ladokgi.apps.utils.ExportCSV;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class KonsultasiReviewFragment extends Fragment implements DataKonsultasiAdapter.DataKonsultasiAdapterCallback {

    private FragmentKonsultasiReviewBinding binding;
    private final List<DataKonsultasi> list = new ArrayList<>();
    private DataKonsultasiAdapter adapter;
    private ExportCSV exportCSV;
    private final ConsultationRepository repository = new ConsultationRepository();

    public KonsultasiReviewFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        exportCSV = new ExportCSV(this, requireContext());
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentKonsultasiReviewBinding.inflate(inflater, container, false);

        adapter = new DataKonsultasiAdapter(getContext(), list, this);
        binding.rvKonsultasiRev.setLayoutManager(new LinearLayoutManager(getActivity()));
        binding.rvKonsultasiRev.setAdapter(adapter);

        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Mohon Tunggu",
                "Sedang Memuat Data", true);
        repository.doctorConsultations("pending", new ConsultationRepository.ListCallback() {
            @Override
            public void onItems(List<DataKonsultasi> items) {
                if (dialog.isShowing()) {
                    dialog.hide();
                }
                if (binding == null) {
                    return;
                }
                list.addAll(items);
                adapter.notifyDataSetChanged();
                binding.rvKonsultasiRev.setVisibility(list.isEmpty() ? View.GONE : View.VISIBLE);
                binding.noData.setVisibility(list.isEmpty() ? View.VISIBLE : View.GONE);
            }

            @Override
            public void onError(Exception e) {
                if (dialog.isShowing()) {
                    dialog.hide();
                }
                if (binding != null) {
                    binding.rvKonsultasiRev.setVisibility(View.GONE);
                    binding.noData.setVisibility(View.VISIBLE);
                }
            }
        });

        binding.downloadCsv.setOnClickListener(v -> exportDataInCSV());

        return binding.getRoot();
    }

    @Override
    public void onRowDataKonsultasiAdapterClicked(int position) {
        Intent i = new Intent(getActivity(), KonsultasiReviewActivity.class);
        i.putExtra("username", list.get(position).getUserName());
        i.putExtra("idKonsultasi", list.get(position).getIdKonsultasi());
        startActivity(i);
    }

    void exportDataInCSV() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Nama Pasien", "Tanggal Konsultasi", "Bobot Aspek Perilaku", "Bobot Aspek Non Perilaku", "Rekomendasi"});
        for (DataKonsultasi dataKonsultasi : list) {
            data.add(new String[]{dataKonsultasi.getNamaPasien(), dataKonsultasi.getTanggal(),
                    dataKonsultasi.getTotalBobotPerilaku(), dataKonsultasi.getTotalBobotNonPerilaku(),
                    calculateHasil(dataKonsultasi.isHasilPerilaku(), dataKonsultasi.isHasilNonPerilaku())});
        }

        String timestamp = Calendar.getInstance().get(Calendar.YEAR) + "-"
                + (Calendar.getInstance().get(Calendar.MONTH) + 1) + "-"
                + Calendar.getInstance().get(Calendar.DATE);
        exportCSV.exportCSV(data, String.format(Locale.US, "data_konsultasi_%s.csv", timestamp));
    }

    private String calculateHasil(boolean h1, boolean h2) {
        if (h1 && h2) {
            return "Tidak disarankan berobat";
        } else if (h1 || h2) {
            return "Disarankan berobat";
        }
        return "Harus berobat";
    }

    @Override
    public void onDestroyView() {
        binding = null;
        super.onDestroyView();
    }
}
