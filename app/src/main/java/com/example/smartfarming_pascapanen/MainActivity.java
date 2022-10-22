package com.example.smartfarming_pascapanen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
import com.example.smartfarming_pascapanen.Pengolahan.Pengolahan;
import com.example.smartfarming_pascapanen.RawData.MenuRaw;
import com.example.smartfarming_pascapanen.RawData.Raw;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textNama;
    ImageButton btnRaw, btnFinish, btnInfo, btnPengolahan;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textNama = findViewById(R.id.textNama);
        btnRaw = findViewById(R.id.raw);
        btnFinish = findViewById(R.id.finish);
        btnInfo = findViewById(R.id.informasi);
        btnPengolahan = findViewById(R.id.pengolahan);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        btnRaw.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuRaw.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

        btnPengolahan.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Pengolahan.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

        btnFinish.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, FinishGoods.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
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

}