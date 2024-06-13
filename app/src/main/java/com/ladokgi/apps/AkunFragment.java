package com.ladokgi.apps;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.ladokgi.apps.login.LoginActivity;
import com.orhanobut.hawk.Hawk;

import java.util.Calendar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AkunFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AkunFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AkunFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AkunFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AkunFragment newInstance(String param1, String param2) {
        AkunFragment fragment = new AkunFragment();
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
        Hawk.init(getContext()).build();
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.fragment_akun, null);

        TextView tvNama, tvAlamat, tvTelepon, tvJenisK, lblJenisK, tvPendidikan, lblPendidikan, tvLama, lblLama;
        Button btnLogout = root.findViewById(R.id.btn_logout);
        Button btnUbah = root.findViewById(R.id.btn_edit);

        tvNama = root.findViewById(R.id.nama);
        tvAlamat = root.findViewById(R.id.alamat);
        tvTelepon = root.findViewById(R.id.telepon);
        tvJenisK = root.findViewById(R.id.jenkel);
        lblJenisK = root.findViewById(R.id.label_jenkel);
        tvPendidikan = root.findViewById(R.id.pendidikan);
        lblPendidikan = root.findViewById(R.id.label_pendidikan);
        tvLama = root.findViewById(R.id.lama);
        lblLama = root.findViewById(R.id.label_lama);



        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(Hawk.get("username")).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable @org.jetbrains.annotations.Nullable DocumentSnapshot value, @Nullable @org.jetbrains.annotations.Nullable FirebaseFirestoreException error) {
                Log.d("onEvent: ", value.getString("role"));
                tvNama.setText(value.getString("nama"));
                tvAlamat.setText(value.getString("alamat"));
                tvTelepon.setText(value.getString("telepon"));
                if (value.getString("role").equals("dokter")){
                    lblJenisK.setVisibility(View.GONE);
                    lblLama.setVisibility(View.GONE);
                    lblPendidikan.setVisibility(View.GONE);
                    tvJenisK.setVisibility(View.GONE);
                    tvPendidikan.setVisibility(View.GONE);
                    tvLama.setVisibility(View.GONE);
                }else {
                    int tahun = Integer.parseInt(value.getString("lama-menggunakan"));
                    int now = Calendar.getInstance().get(Calendar.YEAR);
                    int lama = now - tahun;

                    tvJenisK.setText(value.getString("jenis-kelamin"));
                    tvPendidikan.setText(value.getString("pendidikan"));
                    tvLama.setText(lama+" Tahun");
                }

            }
        });

        btnUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(i);
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Hawk.deleteAll();
                Intent i = new Intent(getActivity(), LoginActivity.class);
                startActivity(i);
                getActivity().finish();
            }
        });
        return root;

    }

}