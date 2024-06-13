package com.ladokgi.apps;

import android.app.ProgressDialog;
import android.content.Intent;
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
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KonsultasiReviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KonsultasiReviewFragment extends Fragment implements DataKonsultasiAdapter.DataKonsultasiAdapterCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    TextView textView;

    List<DataKonsultasi> list = new ArrayList<>();

    DataKonsultasiAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;

    public KonsultasiReviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KonsultasiReviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static KonsultasiReviewFragment newInstance(String param1, String param2) {
        KonsultasiReviewFragment fragment = new KonsultasiReviewFragment();
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
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_konsultasi_review, null);
        recyclerView = root.findViewById(R.id.rv_konsultasi_rev);
        textView = root.findViewById(R.id.no_data);

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
                                    for(DocumentSnapshot docsnap : Objects.requireNonNull(task.getResult())){
                                        Log.d("Nama Pasien: ", ds.getString("nama"));
                                        if(Objects.equals(docsnap.getString("status"), "pending")){
                                        DataKonsultasi datas = new DataKonsultasi(ds.getString("nama"), docsnap.getString("tanggal-konsultasi"),docsnap.getBoolean("perilaku.hasil"), docsnap.getBoolean("non-perilaku.hasil"),docsnap.getString("status"), ds.getString("username"),docsnap.getId());
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
        adapter = new DataKonsultasiAdapter(getContext(),list,this);
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.setAdapter(adapter);

        // Inflate the layout for this fragment
        return root;
    }

    @Override
    public void onRowDataKonsultasiAdapterClicked(int position) {
        Log.d("onRowked", "Click");
        Intent i = new Intent(getActivity(), KonsultasiReviewActivity.class);
        i.putExtra("username", list.get(position).getUserName());
        i.putExtra("idKonsultasi", list.get(position).getIdKonsultasi());
        startActivity(i);
    }
}