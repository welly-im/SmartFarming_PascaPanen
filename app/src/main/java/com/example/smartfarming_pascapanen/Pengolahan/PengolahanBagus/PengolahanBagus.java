package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.Sorting.DataModelSortingBagus;
import com.example.smartfarming_pascapanen.RawData.Sorting.ListDataSortingBagusAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PengolahanBagus extends AppCompatActivity {

    RecyclerView recyclerViewDataKopiBelumProses;
    LinearLayoutManager linearLayoutManager;
    DataModelPengolahanSortingBagus dataModel;
    ListPengolahanDataSortingBagus adapter;
    List<DataModelPengolahanSortingBagus> listData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengolahan_bagus);

        recyclerViewDataKopiBelumProses = findViewById(R.id.recycler_view_kopi_bagus_belum_diproses);

        GetKopiBelumProses();
    }

    private void GetKopiBelumProses() {
        String url = getString(R.string.localhost)+"=getdatasortingbagusbelumproses";
        RequestQueue queue = Volley.newRequestQueue(PengolahanBagus.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        dataModel = new DataModelPengolahanSortingBagus();
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel.setId_sorting(object.getString("id_sorting"));
                        dataModel.setId_panen(object.getString("id_panen"));
                        dataModel.setTanggal_sorting(object.getString("tanggal_sorting"));
                        dataModel.setTanggal_panen(object.getString("tanggal_panen"));
                        dataModel.setBerat(object.getString("berat"));
                        dataModel.setNama_pengguna(object.getString("nama_pengguna"));
                        listData.add(dataModel);
                    }
                    linearLayoutManager = new LinearLayoutManager(PengolahanBagus.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewDataKopiBelumProses.setLayoutManager(linearLayoutManager);
                    adapter = new ListPengolahanDataSortingBagus(PengolahanBagus.this, listData);
                    recyclerViewDataKopiBelumProses.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (Exception e) {
                    Toast.makeText(PengolahanBagus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PengolahanBagus.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}