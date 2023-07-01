package com.example.tugasakhir;

import android.annotation.SuppressLint;
import android.graphics.drawable.TransitionDrawable;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.time.Instant;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;

public class monitoring_lampu extends AppCompatActivity {

    ImageView imageview;
    Button button;
    Boolean turnOn = false;

    private DatabaseReference dbRef;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_monitoring_lampu);

        imageview = (ImageView) findViewById(R.id.imageView);

        dbRef = FirebaseDatabase.getInstance().getReference("/sensor/346555/lampu");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                Long value = (Long) snapshot.getValue();
                turnOn = value == 1;

                imageview.setImageResource((turnOn)? R.drawable.trans_on : R.drawable.trans_off);
                ((TransitionDrawable) imageview.getDrawable()).startTransition(3000);

                Log.d(monitoring_lampu.class.getSimpleName(), "onDataChange: " + turnOn);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.w(monitoring_lampu.class.getSimpleName(), "", error.toException());
            }
        });

        TextInputLayout tilTanggal = findViewById(R.id.til_piltanggal);
        tilTanggal.setEndIconOnClickListener(view -> {
            MaterialDatePicker.Builder<Long> builder = MaterialDatePicker.Builder.datePicker();
            MaterialDatePicker<Long> datePicker = builder.build();

            datePicker.addOnPositiveButtonClickListener((epoch) -> {
                String tanggal = LocalDate.from(Instant.ofEpochMilli(epoch).atOffset(OffsetDateTime.now().getOffset()))
                        .format(DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL));
                tilTanggal.getEditText().setText(tanggal);
            });
            datePicker.show(getSupportFragmentManager(), null);
        });

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextInputLayout Pilih_Kereta = findViewById(R.id.til_pilihtoilet);
        ((AutoCompleteTextView) Pilih_Kereta.getEditText()).setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{
                        "Joglosemarkerto", "Taksaka", "Gajayana Premium"
                })
        );

        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextInputLayout Pilih_Gerbong;
        Pilih_Gerbong = findViewById(R.id.til_pilihgerbong);
        ((AutoCompleteTextView) Pilih_Gerbong.getEditText()).setAdapter(
                new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, new String[]{
                        "Gerbong 1", "Gerbong 2", "Gerbong 3", "gerbong 4"
                })
        );

    }
}
