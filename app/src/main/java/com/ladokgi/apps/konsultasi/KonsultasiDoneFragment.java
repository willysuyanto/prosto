package com.ladokgi.apps.konsultasi;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ladokgi.apps.R;
import com.ladokgi.apps.utils.ExportCSV;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KonsultasiDoneFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KonsultasiDoneFragment extends Fragment implements DataKonsultasiAdapter.DataKonsultasiAdapterCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    RecyclerView recyclerView;
    TextView textView;

    List<DataKonsultasi> list = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;
    DataKonsultasiAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;
    FloatingActionButton btnExportCSV;
    ExportCSV exportCSV;

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public KonsultasiDoneFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KonsultasiDoneFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KonsultasiDoneFragment newInstance(String param1, String param2) {
        KonsultasiDoneFragment fragment = new KonsultasiDoneFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_konsultasi_done,null);
        // Inflate the layout for this fragment
        recyclerView = root.findViewById(R.id.rv_konsultasi_done);
        textView = root.findViewById(R.id.no_data);

        btnExportCSV = root.findViewById(R.id.download_csv);
        exportCSV = new ExportCSV(getActivity());

        Hawk.init(getContext()).build();
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        CollectionReference users = db.collection("users");
        ProgressDialog dialog = ProgressDialog.show(getActivity(), "Mohon Tunggu",
                "Sedang Memuat Data", true);
        users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {

                if(task.isSuccessful()){
                    for(DocumentSnapshot ds: task.getResult()){
                        if(ds.get("role").equals("pasien")){
                            CollectionReference konsultasi = users.document(ds.getString("username")).collection("konsultasi");
                            konsultasi.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                                    for(DocumentSnapshot docsnap : task.getResult()){
                                        Log.d("Nama Pasien: ", ds.getString("nama"));
                                        String status = docsnap.getString("status");
                                        if(status != null && status.equals("confirmed")){
                                            DataKonsultasi datas = new DataKonsultasi(ds.getString("nama"), docsnap.getString("tanggal-konsultasi"),docsnap.getBoolean("perilaku.hasil"), docsnap.getBoolean("non-perilaku.hasil"),docsnap.getString("status"), ds.getString("username"),docsnap.getId());
                                            if (docsnap.get("perilaku.total-bobot") != null) {
                                                datas.setTotalBobotPerilaku(String.valueOf(docsnap.get("perilaku.total-bobot", Integer.class)));
                                            }
                                            if (docsnap.get("non-perilaku.total-bobot") != null) {
                                                datas.setTotalBobotNonPerilaku(String.valueOf(docsnap.get("non-perilaku.total-bobot", Integer.class)));
                                            }
                                            list.add(datas);
                                        }
                                    }
                                    if(list.size()>0){
                                        recyclerView.setVisibility(View.VISIBLE);
                                        textView.setVisibility(View.GONE);
                                        adapter.notifyDataSetChanged();
                                    }
                                }
                            });
                            dialog.hide();
                        }
                    }
                }
                dialog.hide();
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new DataKonsultasiAdapter(getContext(),list, this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        btnExportCSV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exportDataInCSV();
            }
        });

        return root;
    }

    void exportDataInCSV() {
        List<String[]> data = new ArrayList<>();
        data.add(new String[]{"Nama Pasien", "Tanggal Konsultasi", "Bobot Aspek Perilaku", "Bobot Aspek Non Perilaku", "Rekomendasi"});
        for (DataKonsultasi dataKonsultasi : list) {
            data.add(new String[]{dataKonsultasi.getNamaPasien(), dataKonsultasi.getTanggal(), dataKonsultasi.getTotalBobotPerilaku(), dataKonsultasi.getTotalBobotNonPerilaku(), calculateHasil(dataKonsultasi.isHasilPerilaku(), dataKonsultasi.isHasilNonPerilaku())});
        }

        String timestamp = Calendar.getInstance().get(Calendar.YEAR)+String.valueOf(Calendar.getInstance().get(Calendar.MONTH))+Calendar.getInstance().get(Calendar.DATE)+Calendar.getInstance().get(Calendar.MINUTE);
        exportCSV.exportCSV(data, "data_konsultasi_selesai"+timestamp+".csv");
    }

    private String calculateHasil(boolean h1, boolean h2){
        if(h1 && h2){
            return "Tidak disarankan berobat";
        }else if(h1 || h2){
            return "Disarankan berobat";
        }else {
            return "Harus berobat";
        }
    }

    @Override
    public void onRowDataKonsultasiAdapterClicked(int position) {

    }
}