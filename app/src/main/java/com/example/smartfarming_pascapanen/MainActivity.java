package com.example.smartfarming_pascapanen;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.PengolahanBagus;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek.PengolahanJelek;
import com.example.smartfarming_pascapanen.RawData.MenuRaw;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textNama;
    ImageButton btnRaw, btnInfo, btnPengolahan;
    Dialog DialogPilihSorting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textNama = findViewById(R.id.textNama);
        btnRaw = findViewById(R.id.raw);
        btnInfo = findViewById(R.id.informasi);
        btnPengolahan = findViewById(R.id.pengolahan);
        DialogPilihSorting = new Dialog(this);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        btnRaw.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuRaw.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

        btnPengolahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpPilihSorting(v, id_pengguna, nama_pengguna);
            }
        });

        if(id_pengguna == null){
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
        } else {
           get_data_pengguna(id_pengguna);
        }
    }

    private void get_data_pengguna(String id_pengguna){
        String url = getString(R.string.localhost)+"=find";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String nama = jsonObject.getString("nama");
                    Toast.makeText(MainActivity.this, "Selamat Datang " + nama, Toast.LENGTH_SHORT).show();
                    textNama.setText(nama);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", id_pengguna);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void PopUpPilihSorting(View v, String id_pengguna, String nama_pengguna){
        Button KopiBagus, KopiJelek;
        DialogPilihSorting.setContentView(R.layout.component_pilih_sorting);
        KopiBagus = DialogPilihSorting.findViewById(R.id.kopi_bagus);
        KopiJelek = DialogPilihSorting.findViewById(R.id.kopi_jelek);

        KopiBagus.setOnClickListener(v1 -> {
            Intent intent = new Intent(MainActivity.this, PengolahanBagus.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
            DialogPilihSorting.dismiss();
        });
        KopiJelek.setOnClickListener(v1 -> {
            Intent intent = new Intent(MainActivity.this, PengolahanJelek.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
            DialogPilihSorting.dismiss();
        });
        //make full width
        DialogPilihSorting.getWindow().setLayout(1000, 1000);
        DialogPilihSorting.getWindow().setGravity(Gravity.BOTTOM);
        DialogPilihSorting.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
        DialogPilihSorting.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DialogPilihSorting.show();
    }


}