package com.example.tugasakhir.service;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.example.tugasakhir.R;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseService extends FirebaseMessagingService {
    @Override
    public void onNewToken(@NonNull String token) {
        Log.i(FirebaseService.class.getSimpleName(), "token: " + token);
    }

    @Override
    public void onMessageReceived(@NonNull RemoteMessage message) {
        RemoteMessage.Notification notification = message.getNotification();

        buildNotification(notification.getTitle(), notification.getBody());
    }

    void buildNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, "smart-toilet");
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setAutoCancel(true);
        builder.setSmallIcon(R.drawable.ic_baseline_report_problem_24);

        Notification notification = builder.build();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel("smart-toilet", "Notifikasi", NotificationManager.IMPORTANCE_DEFAULT);
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(channel);
            manager.notify("smart-toilet", (int) System.currentTimeMillis(), notification);
        } else {
            NotificationManagerCompat manager = NotificationManagerCompat.from(this);
            manager.notify("smart-toilet", (int) System.currentTimeMillis(), notification);
        }
    }
}
