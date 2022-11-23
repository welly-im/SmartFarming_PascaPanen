package com.example.smartfarming_pascapanen.GudangStok.HistoryPenjualanKopi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.GudangStok.GudangStok;
import com.example.smartfarming_pascapanen.GudangStok.JualKopi.DataModelGudangStokPenjualanKopi;
import com.example.smartfarming_pascapanen.GudangStok.JualKopi.GudangStokPenjualanKopi;
import com.example.smartfarming_pascapanen.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistoryPenjualanKopi extends AppCompatActivity {

    RecyclerView recyclerViewHistoryPenjualanKopi;
    ListDataHistoryPenjualan adapter;
    DataModelHistoryPenjualan dataModel;
    List<DataModelHistoryPenjualan> listData;

    LinearLayoutManager linearLayoutManager;

    Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gudang_stok_history_penjualan_kopi);

        recyclerViewHistoryPenjualanKopi = findViewById(R.id.recycler_view_stok_kopi_terjual);

        btnKembali = findViewById(R.id.kembali);
        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(HistoryPenjualanKopi.this, GudangStok.class);
                startActivity(i);
                finish();
            }
        });

        String url = getString(R.string.localhost)+"=getdatakopiterjual";
        RequestQueue queue = Volley.newRequestQueue(HistoryPenjualanKopi.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData = new ArrayList<>();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel = new DataModelHistoryPenjualan();
                        dataModel.setId_penjualan(object.getString("id_penjualan"));
                        dataModel.setNama_pembeli(object.getString("nama_pembeli"));
                        dataModel.setBerat(object.getString("berat_kopi")+" Kg");
                        dataModel.setHarga_jual("Rp. "+NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(object.getString("harga_penjualan"))));
                        dataModel.setTanggal_jual(object.getString("tanggal_penjualan"));
                        dataModel.setNama_penjual(object.getString("nama_pengguna"));
                        dataModel.setGrade(object.getString("grade"));
                        listData.add(dataModel);
                    }
                    adapter = new ListDataHistoryPenjualan(HistoryPenjualanKopi.this, listData);
                    recyclerViewHistoryPenjualanKopi.setAdapter(adapter);
                    linearLayoutManager = new LinearLayoutManager(HistoryPenjualanKopi.this);
                    recyclerViewHistoryPenjualanKopi.setLayoutManager(linearLayoutManager);
                    adapter.notifyDataSetChanged();
                } catch (Exception e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        });
        queue.add(stringRequest);
    }
}