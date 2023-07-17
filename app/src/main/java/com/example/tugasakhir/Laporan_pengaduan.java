package com.example.tugasakhir;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;
import android.content.Context;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.adapter.LaporanPengaduanAdapter;
import com.example.tugasakhir.data.SharedPreferencesManager;
import com.example.tugasakhir.model.LaporanPengaduan;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Laporan_pengaduan extends AppCompatActivity {

    private LaporanPengaduanAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan_pengaduan);

        RecyclerView rvLaporanPengaduan = findViewById(R.id.rv_laporan_pengaduan);
        adapter = new LaporanPengaduanAdapter(new ArrayList<>(), this::deleteItem);
        rvLaporanPengaduan.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvLaporanPengaduan.setAdapter(adapter);

        int chosenId = getIntent().getIntExtra(KEY_CHOSEN_ID, -1);

        loadItems(adapter, (chosenId != -1)? chosenId : null);
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

                    loadItems(adapter, null);
                },
                error -> {
                    error.printStackTrace();
                    Toast.makeText(this, "Keluhan gagal terhapus", Toast.LENGTH_SHORT).show();
                }
        );

        RequestQueue queue = Volley.newRequestQueue(getApplicationContext());
        queue.add(request);
    }

    private void loadItems(LaporanPengaduanAdapter pengaduanAdapter, Integer highlightedId) {
        SharedPreferencesManager man = new SharedPreferencesManager(this);
        Integer keretaId = man.getIdKereta();
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, VolleyConfig.HOST_URL + "laporanpengaduan.php?id=" + keretaId, null, res -> {
            List<LaporanPengaduan> list = new ArrayList<>();

            for (int i = 0; i < res.length(); i++) {
                try {
                    JSONObject obj = res.getJSONObject(i);

                    list.add(new LaporanPengaduan(
                            obj.getInt("id"),
                            obj.getString("nama"),
                            obj.getString("tgl_keluhan"),
                            obj.getString("kereta"),
                            obj.getString("gerbong"),
                            obj.getString("kursi"),
                            obj.getString("keluhan"),
                            obj.getString("keterangan"),
                            obj.getString("status").equals("1")
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            pengaduanAdapter.update(list, highlightedId);

        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Something's wrong", Toast.LENGTH_SHORT).show();
        });

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