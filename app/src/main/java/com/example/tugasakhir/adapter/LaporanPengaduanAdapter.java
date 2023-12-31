package com.example.tugasakhir.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tugasakhir.R;
import com.example.tugasakhir.model.ComplaintCategory;
import com.example.tugasakhir.model.LaporanPengaduan;
import com.google.android.material.card.MaterialCardView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class LaporanPengaduanAdapter extends RecyclerView.Adapter<LaporanPengaduanAdapter.ViewHolder> {

    private final List<LaporanPengaduan> list;
    private final OnDeleteClickListener onDelete;
    private Integer highlightedId = -1;
    private List<ComplaintCategory> complaints = new ArrayList<>();

    public LaporanPengaduanAdapter(
            List<LaporanPengaduan> list,
            OnDeleteClickListener onDelete
    ) {
        this.list = list;
        this.onDelete = onDelete;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_laporan_pengaduan, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        MaterialCardView root;
        TextView tvJudul, tvTanggal, tvKereta, tvIsi, tvKeterangan, tvStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            root = itemView.findViewById(R.id.root);
            tvJudul = itemView.findViewById(R.id.tv_laporan_judul);
            tvTanggal = itemView.findViewById(R.id.tv_laporan_tanggal);
            tvKereta = itemView.findViewById(R.id.tv_laporan_kereta);
            tvIsi = itemView.findViewById(R.id.tv_laporan_isi);
            tvKeterangan = itemView.findViewById(R.id.tv_laporan_keterangan);
            tvStatus = itemView.findViewById(R.id.tv_laporan_status);
        }

        public void bind(int position) {
            LaporanPengaduan item = list.get(position);

            String location = (item.kursi.isEmpty())?
                    String.format("%s (%s)", item.kereta, item.gerbong):
                    String.format("%s (%s, %s)", item.kereta, item.gerbong, item.kursi);

            Integer categoryId = Integer.parseInt(item.isi);

            String category = "";

            for (ComplaintCategory complaintCategory : complaints) {
                if (Objects.equals(complaintCategory.id, categoryId)) {
                    category = complaintCategory.name;
                }
            }

            tvJudul.setText(item.judul);
            tvTanggal.setText(item.tanggal);
            tvKereta.setText(location);
            tvIsi.setText(category);
            tvKeterangan.setText(item.keterangan);
            tvStatus.setText((item.Status)? "Sudah teratasi" : "Belum teratasi");

            if (Objects.equals(highlightedId, item.id)) {
                root.setCardBackgroundColor(0x33000000);
            }
        }
    }

    public void update(List<LaporanPengaduan> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void update(List<LaporanPengaduan> newList, Integer highlightedId) {
        list.clear();
        list.addAll(newList);
        this.highlightedId = highlightedId;
        notifyDataSetChanged();
    }

    public void setComplaints(List<ComplaintCategory> newList) {
        complaints.clear();
        complaints.addAll(newList);
    }

    public interface OnDeleteClickListener {
        void onClick(Integer itemId);
    }
}
