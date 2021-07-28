package com.ladokgi.apps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PasienAdapter extends
        RecyclerView.Adapter<PasienAdapter.ViewHolder> {

    private static final String TAG = PasienAdapter.class.getSimpleName();

    private Context context;
    private List<Pasien> list;
    private PasienAdapterCallback mAdapterCallback;

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
        public TextView edit;
        public TextView hapus;

        public ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            alamat = itemView.findViewById(R.id.alamat);
            telepon = itemView.findViewById(R.id.nomor_tlp);
            edit = itemView.findViewById(R.id.edit);
            hapus = itemView.findViewById(R.id.hapus);
        }
    }

    public interface PasienAdapterCallback {
        void onEditClicked(int position);
        void onDeleteClicked(int position);
    }
}