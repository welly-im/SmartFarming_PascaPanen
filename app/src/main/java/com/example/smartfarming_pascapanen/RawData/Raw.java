package com.example.smartfarming_pascapanen.RawData;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class Raw extends AppCompatActivity {

    ListView listView;
    ListRawDataAdapter listRawDataAdapter;
    List<ListRawData> itemList = new ArrayList<ListRawData>();
    Button btnTambahData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_raw);
        btnTambahData = findViewById(R.id.btnTambah);
        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        btnTambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent input = new Intent(Raw.this, InputRawData.class);
                input.putExtra("id_pengguna", id_pengguna);
                input.putExtra("nama_pengguna", nama_pengguna);
                startActivity(input);
                finish();
            }
        });

        listView = (ListView) findViewById(R.id.list_raw_data);
        listRawDataAdapter = new ListRawDataAdapter(Raw.this, itemList, this);
        listView.setAdapter(listRawDataAdapter);
        getData();

    }

    private void getData() {
        String url = getString(R.string.localhost)+"=datapanen";
        JsonArrayRequest jArr = new JsonArrayRequest(url, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                for (int i = 0; i < response.length(); i++) {
                    try {
                        JSONObject obj = response.getJSONObject(i);
                        ListRawData item = new ListRawData();
                        item.setId_panen(obj.getString("id_panen"));
                        item.setBerat(obj.getString("berat"));
                        item.setTanggal_panen(obj.getString("tanggal_panen"));
                        item.setNama_pengguna(obj.getString("nama_pengguna"));
                        itemList.add(item);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                listRawDataAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(Raw.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        // menambah request ke request queue
        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext());
        mRequestQueue.add(jArr);
    }
}