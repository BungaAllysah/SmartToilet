package com.example.tugasakhir;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.adapter.LaporanPengaduanAdapter;
import com.example.tugasakhir.data.SharedPreferencesManager;
import com.example.tugasakhir.model.ComplaintCategory;
import com.example.tugasakhir.model.LaporanPengaduan;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.List;

public class Laporan_pengaduan extends AppCompatActivity {

    private LaporanPengaduanAdapter adapter;
    private List<LaporanPengaduan> list = new ArrayList<>();
    private List<ComplaintCategory> complaints = new ArrayList<>();
    private Integer currentPage = 1;
    private Boolean isLoading = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pengaduan);

        RecyclerView rvLaporanPengaduan = findViewById(R.id.rv_laporan_pengaduan);
        adapter = new LaporanPengaduanAdapter(new ArrayList<>(), this::deleteItem);
        rvLaporanPengaduan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvLaporanPengaduan.setAdapter(adapter);

        int chosenId = getIntent().getIntExtra(KEY_CHOSEN_ID, -1);

        rvLaporanPengaduan.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                LinearLayoutManager layoutManager = (LinearLayoutManager) rvLaporanPengaduan.getLayoutManager();

                boolean isLastItemVisible =
                        layoutManager.getItemCount() > 0 &&
                        layoutManager.findLastVisibleItemPosition() + 1 >= layoutManager.getItemCount();

                Log.d(Laporan_pengaduan.class.getSimpleName(), "position: " + layoutManager.findLastVisibleItemPosition());

                if (isLastItemVisible && !isLoading) {
                    currentPage++;
                    Log.d(Laporan_pengaduan.class.getSimpleName(), "onScrolled: " + currentPage);

                    loadItems2(adapter, (chosenId != -1)? chosenId : null);
                }
            }
        });

        loadComplaintCategory(adapter, (chosenId != -1)? chosenId : null);
    }

    private void deleteItem(Integer itemId) {
        StringRequest request = new StringRequest(
                VolleyConfig.HOST_URL + "hapus.php?id=" + itemId,
                response -> {
                    if (response.contains("success")) {
                        Toast.makeText(this, "Keluhan berhasil terhapus", Toast.LENGTH_SHORT).show();
                    } else  {
                        Toast.makeText(this, "Keluhan gagal terhapus", Toast.LENGTH_SHORT).show();
                    }

//                    loadItems(adapter, null);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Keluhan gagal terhapus", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void loadItems2(
            LaporanPengaduanAdapter pengaduanAdapter,
            Integer highlightedId
    ) {
        SharedPreferencesManager man = new SharedPreferencesManager(this);
        Integer keretaId = man.getIdKereta();

        isLoading = true;
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, VolleyConfig.ANIN_HOST_URL + "complaint?page=" + currentPage, null, res -> {

            try {
                JSONObject data = res.getJSONObject("data");
                JSONArray arr = data.getJSONArray("data");

                for (int i = 0; i < arr.length(); i++) {
                    try {
                        JSONObject obj = arr.getJSONObject(i);

                        Instant instant = Instant.parse(obj.getString("created_at"));
                        LocalDateTime dateTime = LocalDateTime.from(instant.atOffset(ZoneOffset.ofHours(7)));

                        list.add(new LaporanPengaduan(
                                obj.getInt("id"),
                                obj.getString("name"),
                                dateTime.format(DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)),
                                keretaId.toString(),
                                obj.getString("wagon_id"),
                                "Tidak ada",
                                obj.getString("category_id"),
                                obj.getString("content"),
                                obj.getString("status").equals("Teratasi")
                        ));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                pengaduanAdapter.update(list, highlightedId);
                isLoading = false;

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Something's wrong", Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    private void loadComplaintCategory(
            LaporanPengaduanAdapter pengaduanAdapter,
            Integer highlightedId
    ) {
        isLoading = true;

        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                VolleyConfig.ANIN_HOST_URL + "complaint-category",
                null,
                response -> {
                    try {
                        JSONArray arr = response.getJSONArray("data");

                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject obj = arr.getJSONObject(i);

                                complaints.add(new ComplaintCategory(
                                        obj.getInt("id"),
                                        obj.getString("name")
                                ));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        pengaduanAdapter.setComplaints(complaints);

                        loadItems2(pengaduanAdapter, highlightedId);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    error.printStackTrace();
                }
        );

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    private static final String KEY_CHOSEN_ID = "key-chosen-id";

    public static void launch(Context context, Integer id) {
        context.startActivity(
                new Intent(context, Laporan_pengaduan.class).putExtra(KEY_CHOSEN_ID, id)
        );
    }
}