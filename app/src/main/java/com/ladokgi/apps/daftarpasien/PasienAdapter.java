package com.ladokgi.apps.daftarpasien;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
        holder.nama.setText(text(item.getNama()));
        holder.alamat.setText(text(item.getAlamat()));
        holder.telepon.setText(text(item.getTelepon()));
        holder.jenisK.setText(text(item.getJenisK()));
        holder.pendidikan.setText(text(item.getPendidikan()));
        holder.lama.setText(formatLama(item.getLama()));

        holder.edit.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                mAdapterCallback.onEditClicked(pos);
            }
        });
        holder.hapus.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                mAdapterCallback.onDeleteClicked(pos);
            }
        });
        holder.reset.setOnClickListener(v -> {
            int pos = holder.getAdapterPosition();
            if (pos != RecyclerView.NO_POSITION) {
                mAdapterCallback.onResetPasswordClicked(pos);
            }
        });
    }

    private static String text(String value) {
        return value == null ? "-" : value;
    }

    /** Shows "N Tahun" when the stored value is a year, otherwise the raw value. */
    static String formatLama(String lama) {
        if (lama == null || lama.trim().isEmpty()) {
            return "-";
        }
        try {
            int year = Integer.parseInt(lama.trim());
            int years = Calendar.getInstance().get(Calendar.YEAR) - year;
            return years < 0 ? lama : years + " Tahun";
        } catch (NumberFormatException e) {
            return lama;
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
        public TextView nama;
        public TextView alamat;
        public TextView telepon;
        public TextView jenisK;
        public TextView pendidikan;
        public TextView lama;
        public Button edit, hapus, reset;

        ViewHolder(View itemView) {
            super(itemView);
            nama = itemView.findViewById(R.id.nama);
            alamat = itemView.findViewById(R.id.alamat);
            telepon = itemView.findViewById(R.id.nomor_tlp);
            jenisK = itemView.findViewById(R.id.jenkel);
            pendidikan = itemView.findViewById(R.id.pendidikan);
            lama = itemView.findViewById(R.id.lama);
            edit = itemView.findViewById(R.id.edit);
            hapus = itemView.findViewById(R.id.hapus);
            reset = itemView.findViewById(R.id.reset);
        }
    }

    public interface PasienAdapterCallback {
        void onEditClicked(int position);
        void onDeleteClicked(int position);
        void onResetPasswordClicked(int position);
    }
}