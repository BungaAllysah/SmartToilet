package com.example.tugasakhir.worker;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.work.Worker;
import androidx.work.WorkerParameters;

import com.example.tugasakhir.R;
import com.example.tugasakhir.data.SharedPreferencesManager;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SmokeDetectorWorker extends Worker {

    public SmokeDetectorWorker(@NonNull Context context, @NonNull WorkerParameters workerParams) {
        super(context, workerParams);
    }

    @NonNull
    @Override
    public Result doWork() {
        SharedPreferencesManager man = new SharedPreferencesManager(getApplicationContext());
        String firebaseId = man.getKeretaFirebaseId();

        if (firebaseId.isEmpty()) {
            return Result.failure();
        }

        DatabaseReference ref = FirebaseDatabase.getInstance()
                .getReference("/sensor/" + firebaseId + "/status");

        ref.get().addOnSuccessListener(result -> {
            String smokeStatus = (String) result.getValue();

            buildNotification("Asap Rokok Terdeteksi", "Terdeteksi asap rokok!");

//            if (!Objects.equals(smokeStatus, "Aman")) {
//                buildNotification("Asap Rokok Terdeteksi", "Terdeteksi asap rokok!");
//            }
        });

        return Result.success();
    }

    void buildNotification(String title, String body) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), "smart-toilet");
        builder.setContentTitle(title);
        builder.setContentText(body);
        builder.setSmallIcon(R.drawable.ic_baseline_report_problem_24);
        builder.setAutoCancel(true);

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
