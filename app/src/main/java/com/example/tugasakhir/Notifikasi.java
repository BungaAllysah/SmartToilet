package com.example.tugasakhir;

import android.os.Bundle;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.adapter.NotifikasiAdapter;
import com.example.tugasakhir.data.SharedPreferencesManager;

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

        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, VolleyConfig.HOST_URL + "notifikasi.php?id_kereta=" + idKereta , null, res -> {
            List<com.example.tugasakhir.model.Notifikasi> list = new ArrayList<>();

            for (int i = 0; i < res.length(); i++) {
                try {
                    JSONObject obj = res.getJSONObject(i);

                    list.add(new com.example.tugasakhir.model.Notifikasi(
                            obj.getInt("id"),
                            obj.getString("nama_gerbong") + "",
                            obj.getString("keluhan"),
                            obj.getInt("status") == 1
                    ));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            RecyclerView rvNotifikasi = findViewById(R.id.rv_notifikasi);
            rvNotifikasi.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
            rvNotifikasi.setAdapter(new NotifikasiAdapter(list));
        }, error -> {
            error.printStackTrace();
            Toast.makeText(this, "Something's wrong", Toast.LENGTH_SHORT).show();
        });

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }
}