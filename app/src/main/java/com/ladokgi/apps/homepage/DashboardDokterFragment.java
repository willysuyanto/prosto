package com.ladokgi.apps.homepage;

import android.content.Intent;
import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ladokgi.apps.daftarpasien.DaftarPasienActivity;
import com.ladokgi.apps.konsultasi.KonsultasiPasienActivity;
import com.ladokgi.apps.R;
import com.orhanobut.hawk.Hawk;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DashboardDokterFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashboardDokterFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    CardView cvKonsultasi, cvPasien;

    public DashboardDokterFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashboardDokterFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashboardDokterFragment newInstance(String param1, String param2) {
        DashboardDokterFragment fragment = new DashboardDokterFragment();
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
        Hawk.init(getContext()).build();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_dashboard_dokter, null);
        cvKonsultasi = root.findViewById(R.id.card_konsultasi);
        cvPasien = root.findViewById(R.id.card_pasien);

        cvKonsultasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), KonsultasiPasienActivity.class);
                startActivity(intent);
            }
        });

        cvPasien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), DaftarPasienActivity.class);
                startActivity(intent);
            }
        });

        return root;
    }
}