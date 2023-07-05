package com.example.tugasakhir;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.textfield.TextInputLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class HalamanPengaduan extends AppCompatActivity {

    private String[] kereta = {""};

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_pengaduan);

        ImageView ivback = findViewById(R.id.iv_back_pengaduan);
        ivback.setOnClickListener(view -> finish());

        TextInputLayout tilKereta = findViewById(R.id.til_kereta);
        ((AutoCompleteTextView) tilKereta.getEditText()).setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{
                        "Joglosemarkerto", "Taksaka", "Gajayana Premium"
                })
        );

        TextInputLayout tilGerbong = findViewById(R.id.til_gerbong);
        ((AutoCompleteTextView) tilGerbong.getEditText()).setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{
                        "Gerbong 1", "Gerbong 2", "Gerbong 3", "gerbong 4"
                })
        );

        TextInputLayout tilKeluhan = findViewById(R.id.til_keluhan);
        ((AutoCompleteTextView) tilKeluhan.getEditText()).setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{
                        "Toilet Kotor", "Air Mati", "Lampu Mati", "Asap Rokok Terdeteksi"
                })
        );

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(view -> {
            String nama = ((TextInputLayout) findViewById(R.id.til_nama)).getEditText().getText().toString();
            String kereta = ((AutoCompleteTextView) tilKereta.getEditText()).getText().toString();
            String gerbong = ((AutoCompleteTextView) tilGerbong.getEditText()).getText().toString();
            String kursi = ((TextInputLayout) findViewById(R.id.til_kursi)).getEditText().getText().toString();
            String keluhan = ((AutoCompleteTextView) tilKeluhan.getEditText()).getText().toString();
            String keterangan = ((TextInputLayout) findViewById(R.id.til_keterangan)).getEditText().getText().toString();

            if (nama.isEmpty() || kereta.isEmpty() || gerbong.isEmpty() || keluhan.isEmpty()) {
                Toast.makeText(this, "Masukan kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, VolleyConfig.HOST_URL + "submit.php", response -> {
                if (response.contains("success")) {
                    Toast.makeText(this, "Keluhan terkirim", Toast.LENGTH_SHORT).show();
                    sendToOther(1, 1, "bunga", "test");
                } else if (response.contains("failure")) {
                    Toast.makeText(this, "Gagal terkirim", Toast.LENGTH_SHORT).show();
                }
            }, error -> Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show()){
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("nama", nama);
                    data.put("kereta", kereta);
                    data.put("gerbong", gerbong);
                    data.put("kursi", kursi);
                    data.put("keluhan", keluhan);
                    data.put("keterangan", keterangan);
                    return data;
                }
            };

            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        });
    }

    private void sendToOther(Integer trainId, Integer categoryId, String name, String content) {
        JSONObject obj = new JSONObject();
        try {
            obj.put("wagon_id", trainId);
            obj.put("category_id", categoryId);
            obj.put("name", name);
            obj.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, VolleyConfig.OTHER_HOST_URL + "complain", obj, body -> {
            Log.d(HalamanPengaduan.class.getSimpleName(), body.toString());
        }, error -> {});

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }
}