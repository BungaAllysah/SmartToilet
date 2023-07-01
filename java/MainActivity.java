package com.example.tugasakhir;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class MainActivity extends AppCompatActivity
implements View.OnClickListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        @SuppressLint({"MissingInflatedId", "LocalSuppress"})
        CardView kondektur = findViewById(R.id.cv_kondektur);
        kondektur.setOnClickListener(this);

        CardView hal_pengaduan = findViewById(R.id.cv_hal_pengaduan);
        hal_pengaduan.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view){
        switch (view.getId()) {
            case R.id.cv_kondektur:
                Intent moveIntent;
                moveIntent = new
                        Intent(MainActivity.this, RegisterLogin.class);
                startActivity(moveIntent);
                break;
            case R.id.cv_hal_pengaduan:
                Intent intent = new
                        Intent(MainActivity.this, HalamanPengaduan.class);
                startActivity(intent);
                break;
            default:
                throw new IllegalStateException("Unexpected value: " + view.getId());
        }
    }
}
