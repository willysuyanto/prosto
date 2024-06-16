package com.ladokgi.apps.konsultasi;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ladokgi.apps.R;

import java.util.List;

public class DataKonsultasiAdapter extends RecyclerView.Adapter<DataKonsultasiAdapter.ViewHolder> {

    private static final String TAG = DataKonsultasiAdapter.class.getSimpleName();

    private final Context context;
    private final List<DataKonsultasi> list;
    private final DataKonsultasiAdapterCallback mAdapterCallback;

    public DataKonsultasiAdapter(Context context, List<DataKonsultasi> list, DataKonsultasiAdapterCallback adapterCallback) {
        this.context = context;
        this.list = list;
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.konsultasi_dokter_list_item,
                parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        DataKonsultasi item = list.get(position);
        Log.d(TAG, "Data Adapter nama: "+ item.getNamaPasien());
        holder.tvNama.setText(item.getNamaPasien());
        holder.tvTanggal.setText(item.getTanggal());
        holder.tvHasil.setText(calculateHasil(item.isHasilPerilaku(),item.isHasilNonPerilaku()));
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAdapterCallback.onRowDataKonsultasiAdapterClicked(position);
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
        TextView tvNama, tvHasil, tvTanggal;
        CardView cardView;
        public ViewHolder(View itemView) {
            super(itemView);
            tvNama = itemView.findViewById(R.id.tv_nama);
            tvHasil = itemView.findViewById(R.id.tv_hasil);
            tvTanggal = itemView.findViewById(R.id.tv_tanggal);
            cardView = itemView.findViewById(R.id.cv_row);
        }
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

    public interface DataKonsultasiAdapterCallback {
        void onRowDataKonsultasiAdapterClicked(int position);
    }
}