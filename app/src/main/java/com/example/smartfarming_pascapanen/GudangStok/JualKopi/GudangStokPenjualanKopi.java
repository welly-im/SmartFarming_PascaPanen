package com.example.smartfarming_pascapanen.GudangStok.JualKopi;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.GudangStok.GudangStok;
import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.InputRawData;
import com.example.smartfarming_pascapanen.RawData.MenuRaw;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GudangStokPenjualanKopi extends AppCompatActivity {

    RecyclerView recyclerViewGudangStokPenjualanKopi;
    LinearLayoutManager linearLayoutManager;
    ListDataGudangStokPenjualanKopi adapter;
    DataModelGudangStokPenjualanKopi dataModel;
    List<DataModelGudangStokPenjualanKopi> listData;

    TextView nodata;

    Button btnKembali;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gudang_stok_penjualan_kopi);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        btnKembali = findViewById(R.id.kembali);
        nodata = findViewById(R.id.noData);
        recyclerViewGudangStokPenjualanKopi = findViewById(R.id.recycler_view_stok_kopi_siap_distribusi);

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //backto intent
                Intent back = new Intent(GudangStokPenjualanKopi.this, GudangStok.class);
                startActivity(back);
                finish();
            }
        });
        GetDataStokKopi(id_pengguna, nama_pengguna);
    }

    private void GetDataStokKopi(String id_pengguna, String nama_pengguna) {
        String url = getString(R.string.localhost)+"=getdatastokkopibelumjual";
        RequestQueue queue = Volley.newRequestQueue(GudangStokPenjualanKopi.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel = new DataModelGudangStokPenjualanKopi();
                        dataModel.setId_stok(object.getString("id_stok"));
                        dataModel.setBerat(object.getString("berat_kopi_tanpa_kulit"));
                        dataModel.setHarga_jual("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(object.getString("harga_jual"))));
                        dataModel.setGrade(object.getString("grade"));
                        dataModel.setKeterangan(object.getString("keterangan"));
                        dataModel.setHarga_jual_per_kg("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(object.getString("harga_per_kg"))));
                        listData.add(dataModel);
                    }
                    adapter = new ListDataGudangStokPenjualanKopi(GudangStokPenjualanKopi.this, listData);
                    linearLayoutManager = new LinearLayoutManager(GudangStokPenjualanKopi.this);
                    recyclerViewGudangStokPenjualanKopi.setLayoutManager(linearLayoutManager);
                    recyclerViewGudangStokPenjualanKopi.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickCallback(new ListDataGudangStokPenjualanKopi.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(DataModelGudangStokPenjualanKopi data) {
                            Intent i = new Intent(GudangStokPenjualanKopi.this, TambahDataPenjualanKopi.class);
                            i.putExtra("id_stok", data.getId_stok());
                            i.putExtra("berat", data.getBerat());
                            i.putExtra("harga_jual", data.getHarga_jual());
                            i.putExtra("grade", data.getGrade());
                            i.putExtra("keterangan", data.getKeterangan());
                            i.putExtra("harga_jual_per_kg", data.getHarga_jual_per_kg());
                            i.putExtra("id_pengguna", id_pengguna);
                            i.putExtra("nama_pengguna", nama_pengguna);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    nodata.setTextSize(20);
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