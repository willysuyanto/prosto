package com.ladokgi.apps.daftarpasien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.ladokgi.apps.R;

import java.util.Calendar;
import java.util.List;

public class PasienAdapter extends
        RecyclerView.Adapter<PasienAdapter.ViewHolder> {

    private static final String TAG = PasienAdapter.class.getSimpleName();

    private final Context context;
    private final List<Pasien> list;
    private final PasienAdapterCallback mAdapterCallback;

    public PasienAdapter(Context context, List<Pasien> list, PasienAdapterCallback adapterCallback) {
        this.context = context;
        this.list = list;
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.patient_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Pasien item = list.get(position);
        holder.nama.setText(item.getNama());
        holder.alamat.setText(item.getAlamat());
        holder.telepon.setText(item.getTelepon());
        holder.jenisK.setText(item.getJenisK());
        holder.pendidikan.setText(item.getPendidikan());

        int now = Calendar.getInstance().get(Calendar.YEAR);
        int tahun = Integer.parseInt(item.getLama());
        int lamas = now - tahun;
        holder.lama.setText(lamas+" Tahun");

        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCallback.onEditClicked(position);
            }
        });
        holder.hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCallback.onDeleteClicked(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void clear() {
        int size = this.list.size();
        this.list.clear();
        notifyItemRangeRemoved(0, size);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView nama;
        public TextView alamat;
        public TextView telepon;
        public TextView jenisK;
        public TextView pendidikan;
        public TextView lama;
        public TextView edit;
        public TextView hapus;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            alamat = itemView.findViewById(R.id.alamat);
            telepon = itemView.findViewById(R.id.nomor_tlp);
            jenisK = itemView.findViewById(R.id.jenkel);
            pendidikan = itemView.findViewById(R.id.pendidikan);
            lama = itemView.findViewById(R.id.lama);
            edit = itemView.findViewById(R.id.edit);
            hapus = itemView.findViewById(R.id.hapus);
        }
    }

    public interface PasienAdapterCallback {
        void onEditClicked(int position);
        void onDeleteClicked(int position);
    }
}