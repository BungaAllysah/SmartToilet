package com.example.tugasakhir;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.data.SharedPreferencesManager;
import com.example.tugasakhir.databinding.ActivityProfileBinding;

import org.json.JSONException;
import org.json.JSONObject;

public class profile extends AppCompatActivity {

    private ActivityProfileBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityProfileBinding.inflate(LayoutInflater.from(this));
        setContentView(binding.getRoot());

        SharedPreferencesManager manager = new SharedPreferencesManager(this);

        JsonRequest<JSONObject> request = new JsonObjectRequest(
                VolleyConfig.HOST_URL + "profil.php?id=" + manager.getUserId(),
                response -> {
                    try {
                        binding.tvProfileName.setText(response.getString("name"));
                        binding.tvProfileNameHeading.setText(response.getString("name"));
                        binding.tvProfileEmail.setText(response.getString("email"));
                        binding.tvProfileNik.setText(response.getString("nik"));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                error -> {
                    Toast.makeText(this, "Gagal mengambil data", Toast.LENGTH_SHORT).show();
                });

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}