package com.example.tugasakhir.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.R;
import com.example.tugasakhir.VolleyConfig;
import com.example.tugasakhir.model.Notifikasi;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NotifikasiAdapter extends RecyclerView.Adapter<NotifikasiAdapter.ViewHolder> {

    private final List<Notifikasi> list;
    private final ItemRootOnClickListener onClickListener;

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

            constraintLayout.setOnClickListener(view -> onClickListener.OnClickListener(item.id));
            tvJudul.setText(item.isi);
            checkBoxStatus.setChecked(item.status);

            checkBoxStatus.setOnCheckedChangeListener((view, isChecked) -> {
                checkNotification(view, item.id, isChecked);
            });
        }

        private void checkNotification(View view, int id, boolean isChecked) {
            StringRequest req = new StringRequest(Request.Method.POST, VolleyConfig.HOST_URL + "notifcentang.php", res -> {
                if (res.contains("success")) {
                    Toast.makeText(view.getContext(), "Succcess!", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "Failure", Toast.LENGTH_SHORT).show();
                    Log.w(NotifikasiAdapter.class.getSimpleName(), res);
                }
            }, error -> {
                error.printStackTrace();
                Toast.makeText(view.getContext(), "Something's wrong", Toast.LENGTH_SHORT).show();
            }) {
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("id", String.valueOf(id));
                    data.put("status", (isChecked)? "1" : "0");
                    return data;
                }
            };;

            RequestQueue requestQueue = Volley.newRequestQueue(view.getContext());
            requestQueue.add(req);
        }
    }

    interface ItemOnCheckedChangeListener {
        void OnCheckedChangedListener(Context context, Integer id, boolean isChecked);
    }

    public interface ItemRootOnClickListener {
        void OnClickListener(Integer id);
    }
}
