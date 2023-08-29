package com.example.tugasakhir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.Window;

import androidx.appcompat.app.AppCompatActivity;

import com.example.tugasakhir.data.SharedPreferencesManager;
import com.google.firebase.messaging.FirebaseMessaging;

@SuppressLint("CustomSplashScreen")
public class  splashscreen extends AppCompatActivity {

    @Override
    protected void onCreate(@SuppressLint("UnknownNullness") Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splashscreen);

        SharedPreferencesManager manager = new SharedPreferencesManager(this);

        final Handler handler = new Handler();
        handler.postDelayed(() -> {
            Boolean isLoggedIn = manager.getIsLoggedIn();

            if (isLoggedIn) {
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
            }

            startActivity(new Intent(
                    getApplicationContext(),
                    (isLoggedIn)? dashboard.class : MainActivity.class
            ));
            finish();
        }, 3000L); //3000 L = 3 detik
    }
}