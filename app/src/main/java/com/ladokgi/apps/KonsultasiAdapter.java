package com.ladokgi.apps;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class KonsultasiAdapter extends RecyclerView.Adapter<KonsultasiAdapter.ViewHolder> {

    private static final String TAG = KonsultasiAdapter.class.getSimpleName();

    private Context context;
    private List<Konsultasi> list;
    private KonsultasiAdapterCallback mAdapterCallback;

    public KonsultasiAdapter(Context context, List<Konsultasi> list, KonsultasiAdapterCallback adapterCallback) {
        this.context = context;
        this.list = list;
        this.mAdapterCallback = adapterCallback;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.konsultasi_list_item,
                parent, false);
        return new ViewHolder(view);
    }


    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Konsultasi item = list.get(position);
        holder.hasil.setText(calculateHasil(item.isHasilPerilaku(),item.isHasilNonPerilaku()));
        holder.tanggal.setText(item.getTanggal());
        if (item.getStatus().equals("pending")){
            holder.status.setText("Sedang direview oleh dokter");
        }else {
            holder.status.setText("Hasil telah diverifikasi oleh dokter");
        }
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
        public TextView tanggal;
        public TextView hasil;
        public TextView status;
        public ViewHolder(View itemView) {
            super(itemView);

            tanggal = itemView.findViewById(R.id.tv_tanggal);
            hasil = itemView.findViewById((R.id.tv_hasil));
            status = itemView.findViewById(R.id.tv_status);

        }
    }

    public interface KonsultasiAdapterCallback {
        void onRowKonsultasiAdapterClicked(int position);
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
}