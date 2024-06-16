package com.ladokgi.apps.homepage;

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
import android.widget.Button;
import android.widget.LinearLayout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.ladokgi.apps.AspekPerilakuActivity;
import com.ladokgi.apps.konsultasi.Konsultasi;
import com.ladokgi.apps.konsultasi.KonsultasiAdapter;
import com.ladokgi.apps.R;
import com.orhanobut.hawk.Hawk;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardPasienFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardPasienFragment extends Fragment implements KonsultasiAdapter.KonsultasiAdapterCallback {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView rcv;
    LinearLayout lin;
    Button btnKonsultasinow;

    List<Konsultasi> list = new ArrayList<>();

    LinearLayoutManager linearLayoutManager;
    KonsultasiAdapter adapter;
    RecyclerView.LayoutManager mLayoutManager;


    public DashboardPasienFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardPasienFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardPasienFragment newInstance(String param1, String param2) {
        DashboardPasienFragment fragment = new DashboardPasienFragment();
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
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard_pasien, null);
        Hawk.init(getContext()).build();
        String currentUser = Hawk.get("username");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        rcv = root.findViewById(R.id.rcv);
        lin = root.findViewById(R.id.lin);
        btnKonsultasinow = root.findViewById(R.id.btn_consultnow);

        btnKonsultasinow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AspekPerilakuActivity.class);
                startActivity(i);
            }
        });

        CollectionReference konsultasi = db.collection("users").document(currentUser).collection("konsultasi");
        konsultasi.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for (DocumentSnapshot data : task.getResult()){
                        //list.add(new Konsultasi(data.get("tanggal-konsultasi"),data.get(),));
                        Log.d("tanggal: ", ""+data.get("tanggal-konsultasi"));
                        Log.d("aspek perilaku: ", ""+data.get("perilaku.hasil"));
                        Log.d("aspek non perilaku: ", ""+data.get("non-perilaku.hasil"));
                        Log.d("status: ", ""+data.get("status"));
                        String tanggal = data.get("tanggal-konsultasi", String.class);

                        boolean hasilP = false;
                        if (data.get("perilaku.hasil", boolean.class) != null) {
                            hasilP = data.get("perilaku.hasil", boolean.class);
                        }
                        boolean hasilNP = false;
                        if (data.get("non-perilaku.hasil", boolean.class) != null) {
                            hasilNP = data.get("non-perilaku.hasil", boolean.class);
                        }

                        String status = "unfinished";
                        if (data.get("status", String.class) != null) {
                            status = data.get("status", String.class);
                        }
                        list.add(new Konsultasi(tanggal, hasilP, hasilNP, status));
                    }
                    if(list.size()>0){
                        rcv.setVisibility(View.VISIBLE);
                        lin.setVisibility(View.GONE);
                    }
                }
            }
        });

        mLayoutManager = new LinearLayoutManager(getActivity());
        adapter = new KonsultasiAdapter(getContext(),list,this);
        rcv.setLayoutManager(mLayoutManager);
        rcv.setAdapter(adapter);
        return root;
    }

    @Override
    public void onRowKonsultasiAdapterClicked(int position) {

    }
}