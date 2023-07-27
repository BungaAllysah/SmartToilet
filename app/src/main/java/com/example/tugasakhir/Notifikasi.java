package com.example.tugasakhir;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.adapter.NotifikasiAdapter;
import com.example.tugasakhir.data.SharedPreferencesManager;
import com.example.tugasakhir.model.ComplaintCategory;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Notifikasi extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifikasi);
        SharedPreferencesManager man = new SharedPreferencesManager(this);
        Integer idKereta = man.getUserId();

        RecyclerView rvNotifikasi = findViewById(R.id.rv_notifikasi);
        rvNotifikasi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));

        NotifikasiAdapter adapter = new NotifikasiAdapter(new ArrayList<>(), id -> {
            Laporan_pengaduan.launch(this, id);
        });
        rvNotifikasi.setAdapter(adapter);

        loadComplaintCategory(adapter);
    }

    private void loadComplaintCategory(NotifikasiAdapter notifikasiAdapter) {
        JsonObjectRequest req = new JsonObjectRequest(
                Request.Method.GET,
                VolleyConfig.ANIN_HOST_URL + "complaint-category",
                null,
                response -> {
                    try {
                        JSONArray arr = response.getJSONArray("data");

                        List<ComplaintCategory> complaintCategories = new ArrayList<>();

                        for (int i = 0; i < arr.length(); i++) {
                            try {
                                JSONObject obj = arr.getJSONObject(i);

                                complaintCategories.add(new ComplaintCategory(
                                        obj.getInt("id"),
                                        obj.getString("name")
                                ));

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }

                        notifikasiAdapter.setComplaints(complaintCategories);

                        getComplains(notifikasiAdapter);
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

    private void getComplains(NotifikasiAdapter rvAdapter) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, VolleyConfig.ANIN_HOST_URL + "complaint?page=" + 1, null, res -> {
            List<com.example.tugasakhir.model.Notifikasi> list = new ArrayList<>();

            try {
                JSONObject data = res.getJSONObject("data");
                JSONArray arr = data.getJSONArray("data");
                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    list.add(new com.example.tugasakhir.model.Notifikasi(
                            obj.getInt("id"),
                            obj.getString("wagon_id"),
                            obj.getString("category_id"),
                            obj.getString("status").equals("Teratasi")
                    ));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }


            rvAdapter.update(list);
        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Something's wrong", Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }
}