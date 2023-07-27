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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class HalamanPengaduan extends AppCompatActivity {

    private String[] kereta = {""};

    private final List<String> keretaNames = new ArrayList<>();
    private final List<Integer> keretaIds = new ArrayList<>();
    private final List<String> gerbongNames = new ArrayList<>();
    private final List<Integer> gerbongIds = new ArrayList<>();
    private final List<String> complaintNames = new ArrayList<>();
    private final List<Integer> complaintIds = new ArrayList<>();

    private TextInputLayout tilKereta, tilGerbong, tilKeluhan;
    private Integer selectedTrainId, selectedWagonId, selectedCategoryId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_halaman_pengaduan);

        getTrainList();
        getComplaintCategories();

        ImageView ivback = findViewById(R.id.iv_back_pengaduan);
        ivback.setOnClickListener(view -> finish());

        tilKereta = findViewById(R.id.til_kereta);
        ((AutoCompleteTextView) tilKereta.getEditText()).setOnItemClickListener((a, v, index, l) -> {
            getWagonList(keretaIds.get(index));
            selectedTrainId = keretaIds.get(index);
        });

        tilGerbong = findViewById(R.id.til_gerbong);
        ((AutoCompleteTextView) tilGerbong.getEditText()).setOnItemClickListener((a, v, index, l) -> {
            selectedWagonId = gerbongIds.get(index);
        });

        tilKeluhan = findViewById(R.id.til_keluhan);
        ((AutoCompleteTextView) tilKeluhan.getEditText()).setOnItemClickListener((a, v, index, l) -> {
            selectedCategoryId = complaintIds.get(index);
        });

        Button btnSubmit = findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(view -> {
            String nama = ((TextInputLayout) findViewById(R.id.til_nama)).getEditText().getText().toString();
            String kereta = tilKereta.getEditText().getText().toString();
            String gerbong = tilGerbong.getEditText().getText().toString();
            String kursi = ((TextInputLayout) findViewById(R.id.til_kursi)).getEditText().getText().toString();
            String keluhan = tilKeluhan.getEditText().getText().toString();
            String keterangan = ((TextInputLayout) findViewById(R.id.til_keterangan)).getEditText().getText().toString();

            if (nama.isEmpty() || kereta.isEmpty() || gerbong.isEmpty() || keluhan.isEmpty()) {
                Toast.makeText(this, "Masukan kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            StringRequest stringRequest = new StringRequest(Request.Method.POST, VolleyConfig.HOST_URL + "submit.php", response -> {
                if (response.contains("success")) {
                    Toast.makeText(this, "Keluhan terkirim", Toast.LENGTH_SHORT).show();
                    sendToOther(selectedWagonId, selectedCategoryId, nama, keterangan);
                } else if (response.contains("failure")) {
                    Toast.makeText(this, "Gagal terkirim", Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                error.printStackTrace();
                Toast.makeText(this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }){
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("nama", nama);
                    data.put("nama_kereta", kereta);
                    data.put("kereta_id", selectedTrainId.toString());
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

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.POST, VolleyConfig.ANIN_HOST_URL + "complaint", obj, body -> {
            Log.d(HalamanPengaduan.class.getSimpleName(), body.toString());
        }, error -> {});

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    private void getTrainList() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, VolleyConfig.ANIN_HOST_URL + "train", null, body -> {
            keretaNames.clear();
            keretaIds.clear();

            try {
                JSONArray arr = body.getJSONArray("data");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    keretaNames.add(obj.getString("name"));
                    keretaIds.add(obj.getInt("id"));
                }

                ((AutoCompleteTextView) tilKereta.getEditText()).setAdapter(
                        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, keretaNames)
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }

    private void getWagonList(Integer trainId) {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, VolleyConfig.ANIN_HOST_URL + "train/" + trainId + "/wagon", null, body -> {
            gerbongNames.clear();
            gerbongIds.clear();

            try {
                JSONArray arr = body.getJSONArray("data");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    gerbongNames.add(obj.getString("name"));
                    gerbongIds.add(obj.getInt("id"));
                }

                AutoCompleteTextView dropdown = (AutoCompleteTextView) tilGerbong.getEditText();
                dropdown.setAdapter(
                        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, gerbongNames)
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }

    private void getComplaintCategories() {
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, VolleyConfig.ANIN_HOST_URL + "complaint-category", null, body -> {
            complaintIds.clear();
            complaintNames.clear();

            try {
                JSONArray arr = body.getJSONArray("data");

                for (int i = 0; i < arr.length(); i++) {
                    JSONObject obj = arr.getJSONObject(i);

                    complaintNames.add(obj.getString("name"));
                    complaintIds.add(obj.getInt("id"));
                }

                AutoCompleteTextView dropdown = (AutoCompleteTextView) tilKeluhan.getEditText();
                dropdown.setAdapter(
                        new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, complaintNames)
                );
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }, error -> {

        });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(req);
    }
}