package com.example.tugasakhir;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.tugasakhir.data.SharedPreferencesManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class RegisterLogin extends AppCompatActivity {

    private EditText etmail, etPassword;
    private String email, password;
    public final String URL = VolleyConfig.HOST_URL + "login.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_login);
        email = password = "";
        etmail = findViewById(R.id.etmail);
        etPassword = findViewById(R.id.etPassword);

        Button button = findViewById(R.id.button);
        button.setOnClickListener(this::login);
    }

    public void login(View view) {
        email = etmail.getText().toString().trim();
        password = etPassword.getText().toString().trim();
        if(!email.equals("") && !password.equals("")) {
            StringRequest stringRequest = new StringRequest(Request.Method.POST, URL, response -> {
                try {
                    JSONObject json = new JSONObject(response);
                    if (json.getString("message").contains("success")) {
                        Intent intent = new Intent(RegisterLogin.this, dashboard.class);
                        SharedPreferencesManager manager = new SharedPreferencesManager(this);

                        JSONObject data = json.getJSONObject("data");
                        manager.setIsLoggedIn(true);
                        manager.setUserId(data.getInt("id"));
                        manager.setKeretaAninId(data.getInt("id_kereta_anin"));
                        manager.setKeretaFirebaseId(data.getString("id_kereta_firebase"));

                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(RegisterLogin.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.d(RegisterLogin.class.getSimpleName(), "login: " + response);
                    Toast.makeText(RegisterLogin.this, "Invalid Login Id/Password", Toast.LENGTH_SHORT).show();
                }
            }, error -> {
                error.printStackTrace();
                Toast.makeText(RegisterLogin.this, error.toString().trim(), Toast.LENGTH_SHORT).show();
            }){
                @NonNull
                @Override
                protected Map<String, String> getParams() {
                    Map<String, String> data = new HashMap<>();
                    data.put("email", email);
                    data.put("password", password);
                    return data;
                }
            };
            RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
            requestQueue.add(stringRequest);
        }else {
            Toast.makeText(this, "Field Cannot be emoty", Toast.LENGTH_SHORT).show();
        }
    }

    public void register(View view) {
        Intent intent = new Intent(this, register.class);
        startActivity(intent);
        finish();
    }
}