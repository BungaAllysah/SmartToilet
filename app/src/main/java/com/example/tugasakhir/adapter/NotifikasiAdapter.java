package com.example.tugasakhir.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.R;
import com.example.tugasakhir.VolleyConfig;
import com.example.tugasakhir.model.ComplaintCategory;
import com.example.tugasakhir.model.Notifikasi;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private List<Notifikasi> list;
    private final ItemRootOnClickListener onClickListener;
    private List<ComplaintCategory> complaintCategories = new ArrayList<>();

    public NotifikasiAdapter(List<Notifikasi> list, ItemRootOnClickListener onClickListener) {
        this.list = list;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public NotifikasiAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifikasi, parent, false);
        return new NotifikasiAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull NotifikasiAdapter.ViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        ConstraintLayout constraintLayout;
        TextView tvJudul;
        CheckBox checkBoxStatus;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            constraintLayout = itemView.findViewById(R.id.root_notifikasi);
            tvJudul = itemView.findViewById(R.id.tv_notifikasi);
            checkBoxStatus = itemView.findViewById(R.id.check_notifikasi);
        }

        public void bind(int position) {
            Notifikasi item = list.get(position);

            Integer categoryId = Integer.parseInt(item.isi);

            String category = "";

            for (ComplaintCategory complaintCategory : complaintCategories) {
                if (Objects.equals(complaintCategory.id, categoryId)) {
                    category = complaintCategory.name;
                }
            }

            constraintLayout.setOnClickListener(view -> onClickListener.OnClickListener(item.id));
            tvJudul.setText(category);
            checkBoxStatus.setChecked(item.status);

            checkBoxStatus.setOnCheckedChangeListener((view, isChecked) -> {
                try {
                    checkNotification(view, item.id, isChecked);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            });
        }

        private void checkNotification(View view, int id, boolean isChecked) throws JSONException {
            JSONObject body = new JSONObject();
            body.put("status", isChecked? "Teratasi" : "Belum Teratasi");

            JsonObjectRequest req = new JsonObjectRequest(
                    Request.Method.POST,
                    VolleyConfig.ANIN_HOST_URL + "complaint/" + id + "/status",
                    body,
                    res -> {
                        try {
                            String message = res.getString("message");

                            Toast.makeText(view.getContext(), message, Toast.LENGTH_SHORT).show();
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }, error -> {
                        error.printStackTrace();
                        Toast.makeText(view.getContext(), "Something's wrong", Toast.LENGTH_SHORT).show();
                    });

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(req);
        }
    }

    public void update(List<Notifikasi> newList) {
        list.clear();
        list.addAll(newList);
        notifyDataSetChanged();
    }

    public void setComplaints(List<ComplaintCategory> newList) {
        complaintCategories.clear();
        complaintCategories.addAll(newList);
    }

    interface ItemOnCheckedChangeListener {
        void OnCheckedChangedListener(Context context, Integer id, boolean isChecked);
    }

    public interface ItemRootOnClickListener {
        void OnClickListener(Integer id);
    }
}
