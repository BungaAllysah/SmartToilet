package com.example.tugasakhir;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.JsonRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.data.SharedPreferencesManager;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.messaging.FirebaseMessaging;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

public class dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard);

        FirebaseMessaging messaging = FirebaseMessaging.getInstance();
        messaging.subscribeToTopic("pengaduan");
        messaging.getToken()
                .addOnCompleteListener(this, task -> {
                    if (!task.isSuccessful()) {
                        Log.w(dashboard.class.getSimpleName(), "token retrieval failed");
                        return;
                    }

                    String token = task.getResult();
                    Log.i(dashboard.class.getSimpleName(), "token: " + token);
                });

        ImageView ivLogout = findViewById(R.id.iv_logout);
        ivLogout.setOnClickListener((view -> {
            SharedPreferencesManager manager = new SharedPreferencesManager(this);
            manager.setIsLoggedIn(false);
            messaging.unsubscribeFromTopic("pengaduan");

            startActivity(new Intent(this, MainActivity.class));
            finish();
        }));

        TextInputLayout pililhtoilet = findViewById(R.id.til_pilihtoilet);
        getToilet((AutoCompleteTextView) pililhtoilet.getEditText());

        Button btnToMonitoring = findViewById(R.id.btn_to_monitoring);
        btnToMonitoring.setOnClickListener((view -> {
            startActivity(new Intent(this, monitoring_lampu.class));
        }));

        Button btnToLaporan = findViewById(R.id.btn_to_laporan);
        btnToLaporan.setOnClickListener((view -> {
            startActivity(new Intent(this, Laporan_pengaduan.class));
        }));

        ImageView ivToNotif = findViewById(R.id.iv_to_notification);
        ivToNotif.setOnClickListener((view -> {
            startActivity(new Intent(this, Notifikasi.class));
        }));

        ImageView ivToProfile = findViewById(R.id.iv_to_profile);
        ivToProfile.setOnClickListener((view -> {
            startActivity(new Intent(this, profile.class));
        }));

        TextInputLayout tilVolumePersen = findViewById(R.id.til_volume_p);
        TextInputLayout tilVolumeL = findViewById(R.id.til_volume_m3);
        TextInputLayout tilKonsumsiPersen = findViewById(R.id.til_konsumsi_ls);
        TextInputLayout tilKonsumsiMl = findViewById(R.id.til_konsumsi_ml);

        TimerTask tt = new TimerTask() {
            @Override
            public void run() {
                updateSensor(tilVolumePersen.getEditText(), tilVolumeL.getEditText(), tilKonsumsiPersen.getEditText(), tilKonsumsiMl.getEditText());
            }
        };
        Timer t = new Timer();
        t.scheduleAtFixedRate(tt, 0L, 1000L);

        getOnBackPressedDispatcher().addCallback(this, new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                finish();
            }
        });
    }

    private void updateSensor(
            EditText etVolumePersen,
            EditText etVolumeLiter,
            EditText etKonsumsiAirPersen,
            EditText etKonsumsiAirMl
    ) {
        JsonRequest req = new JsonObjectRequest(VolleyConfig.HOST_URL + "sensor_terbaru.php?id=" + 1, body -> {
            try {
                JSONObject data = body.getJSONObject("data");
                Double konsumsiSekarang = Double.parseDouble(data.getString("volume"));
                Double volumeTotal = Double.parseDouble(data.getString("volume_total"));
                Double konsumsiPersen;
                try {
                    konsumsiPersen = konsumsiSekarang / volumeTotal;
                } catch (ArithmeticException e) {
                    konsumsiPersen = (double) 0;
                }

                etVolumePersen.setText(data.getString("level"));
                etVolumeLiter.setText(data.getString("volume_sisa"));
                etKonsumsiAirPersen.setText(konsumsiPersen.toString());
                etKonsumsiAirMl.setText(volumeTotal.toString());
            } catch (JSONException e) {
                Log.d(dashboard.class.getSimpleName(), body.toString());
                e.printStackTrace();
            }
        }, Throwable::printStackTrace);

        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }

    private void getToilet(AutoCompleteTextView dropdown) {
        SharedPreferencesManager man = new SharedPreferencesManager(this);
        Integer idKereta = man.getUserId();
        JsonArrayRequest req = new JsonArrayRequest(VolleyConfig.HOST_URL + "daftar_toilet.php?kereta_id=" + idKereta, body -> {
            List<String> list = new ArrayList<>();
            for (int i = 0; i < body.length(); i++) {
                try {
                    list.add(body.getJSONObject(i).getString("nama"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            dropdown.setAdapter(new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list));
        },error -> {

        });
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        requestQueue.add(req);
    }
}