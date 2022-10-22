package com.example.smartfarming_pascapanen.Pengolahan;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.os.Bundle;

import com.example.smartfarming_pascapanen.Pengolahan.Fermentasi.DashboardPengolahanBasah;
import com.example.smartfarming_pascapanen.R;

public class Pengolahan extends AppCompatActivity {

    CardView Fermentasi, Penjemuran;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pengolahan);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        Fermentasi = findViewById(R.id.toFermentasi);
        Penjemuran = findViewById(R.id.toPenjemuran);

        Fermentasi.setOnClickListener(v -> {
            Intent intent = new Intent(Pengolahan.this, DashboardPengolahanBasah.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

    }
}