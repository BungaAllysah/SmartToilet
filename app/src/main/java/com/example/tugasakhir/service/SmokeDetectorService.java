package com.example.tugasakhir.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tugasakhir.R;
import com.example.tugasakhir.dashboard;
import com.example.tugasakhir.data.SharedPreferencesManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Objects;
import java.util.Timer;
import java.util.TimerTask;

public class SmokeDetectorService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        Timer t = new Timer();

        t.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                SharedPreferencesManager man = new SharedPreferencesManager(getApplicationContext());
                String firebaseId = man.getKeretaFirebaseId();

                if (firebaseId.isEmpty()) {
                    return;
                }

                DatabaseReference ref = FirebaseDatabase.getInstance()
                        .getReference("/sensor/" + firebaseId + "/");

                ref.child("status")
                        .get()
                        .addOnSuccessListener(result -> {
                            String smokeStatus = (String) result.getValue();

                            if (!Objects.equals(smokeStatus.trim(), "Aman")) {
                                buildNotification("Asap Rokok Terdeteksi", "Terdeteksi asap rokok!");
                            }
                        });

                ref.child("vol_sisa")
                        .get()
                        .addOnSuccessListener(result -> {
                            Double current = (Double) result.getValue();
                            Double currentPercent = current / 63.827;

                            Log.d(SmokeDetectorService.class.getSimpleName(), currentPercent.toString());

                            if (currentPercent < 0.2) {
                                buildNotification("Air Tangki Habis", "Air tangki habis!");
                            }
                        });
            }
        }, 0, 10000);

        return super.onStartCommand(intent, flags, startId);
    }

    private void buildNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "smart-toilet");

        PendingIntent pendingIntent = PendingIntent.getActivity(
                this,
                (int) System.currentTimeMillis(),
                new Intent(this, dashboard.class),
                PendingIntent.FLAG_IMMUTABLE
        );

        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.ic_baseline_report_problem_24);
        builder.setAutoCancel(true);
        builder.setContentIntent(pendingIntent);

        Notification notification = builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("smart-toilet", "Notifikasi", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getApplicationContext().getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.notify("smart-toilet", (int) System.currentTimeMillis(), notification);
        } else {
            NotificationManagerCompat manager = NotificationManagerCompat.from(getApplicationContext());
            manager.notify("smart-toilet", (int) System.currentTimeMillis(), notification);
        }
    }
}
