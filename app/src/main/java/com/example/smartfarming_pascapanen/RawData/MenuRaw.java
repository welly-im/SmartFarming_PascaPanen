package com.example.smartfarming_pascapanen.RawData;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.Sorting.DashboardSorting;

public class MenuRaw extends AppCompatActivity {

    Button toRaw, toSorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_raw);
        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        toRaw = findViewById(R.id.toGudangPanen);
        toSorting = findViewById(R.id.toSorting);

        toRaw.setOnClickListener(v -> {
            Intent intent = new Intent(MenuRaw.this, Raw.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

        toSorting.setOnClickListener(v -> {
            Intent intent = new Intent(MenuRaw.this, DashboardSorting.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

    }

}