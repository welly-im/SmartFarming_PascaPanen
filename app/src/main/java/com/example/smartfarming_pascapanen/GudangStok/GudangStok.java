package com.example.smartfarming_pascapanen.GudangStok;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.GudangStok.GudangStokTambahData.GudangStokTambahData;
import com.example.smartfarming_pascapanen.GudangStok.HistoryPenjualanKopi.DataModelHistoryPenjualan;
import com.example.smartfarming_pascapanen.GudangStok.HistoryPenjualanKopi.HistoryPenjualanKopi;
import com.example.smartfarming_pascapanen.GudangStok.HistoryPenjualanKopi.ListDataHistoryPenjualan;
import com.example.smartfarming_pascapanen.GudangStok.JualKopi.GudangStokPenjualanKopi;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.PengolahanBagus;
import com.example.smartfarming_pascapanen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GudangStok extends AppCompatActivity {

    TextView tvKopiBelumDigiling, BeratKopiGradeA, BeratKopiGradeB, BeratKopiGradeC, TotalHargaJual, nodata;
    Button btnDetailSiapDistribusi, btnDetailKopiTerjual;

    LinearLayoutManager linearLayoutManager, linearLayoutManager2, linearLayoutManager3;

    RecyclerView recyclerViewKopiBelumDigiling;
    DataModelKopiBelumDigiling dataModel;
    ListKopiBelumDigilingAdapter adapter;
    List<DataModelKopiBelumDigiling> listData;

    RecyclerView recyclerViewKopiSiapDistribusi;
    DataModelKopiSiapDistribusi dataModel2;
    ListKopiSiapDistribusiAdapter adapter2;
    List<DataModelKopiSiapDistribusi> listData2;

    RecyclerView recyclerViewHistoryPenjualanKopi;
    ListDataHistoryPenjualan adapter3;
    DataModelHistoryPenjualan dataModel3;
    List<DataModelHistoryPenjualan> listData3;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gudang_stok);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        BeratKopiGradeA = findViewById(R.id.berat_kopi_grade_a);
        BeratKopiGradeB = findViewById(R.id.berat_kopi_grade_b);
        BeratKopiGradeC = findViewById(R.id.berat_kopi_grade_c);
        TotalHargaJual = findViewById(R.id.total_harga_jual);
        btnDetailKopiTerjual = findViewById(R.id.kopi_terjual);
        btnDetailSiapDistribusi = findViewById(R.id.detail_siap_distribusi);
        tvKopiBelumDigiling = findViewById(R.id.txtKopiBelumDigiling);
        recyclerViewKopiBelumDigiling = findViewById(R.id.recycler_view_kopi_belum_digiling);
        recyclerViewKopiSiapDistribusi = findViewById(R.id.recycler_view_kopi_siap_distribusi);
        recyclerViewHistoryPenjualanKopi = findViewById(R.id.recycler_view_kopi_terjual);
        nodata = findViewById(R.id.noData);

        btnDetailKopiTerjual.setOnClickListener(v -> {
            Intent intent = new Intent(GudangStok.this, HistoryPenjualanKopi.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

        btnDetailSiapDistribusi.setOnClickListener(v -> {
            Intent intent = new Intent(GudangStok.this, GudangStokPenjualanKopi.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
            finish();
        });

        GetKopiBelumGiling(id_pengguna, nama_pengguna);
        GetKopiSiapDistribusi();
        GetKopiTerjual();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetKopiBelumGiling(id_pengguna, nama_pengguna);
                GetKopiSiapDistribusi();
                GetKopiTerjual();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void GetKopiBelumGiling(String id_pengguna, String nama_pengguna) {
        String url = getString(R.string.localhost)+"=getpenjemuranbelumgiling";
        RequestQueue queue = Volley.newRequestQueue(GudangStok.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel = new DataModelKopiBelumDigiling();
                        dataModel.setId_penjemuran(object.getString("id_penjemuran"));
                        dataModel.setBerat_akhir(object.getString("berat_akhir_proses") + " Kg");
                        listData.add(dataModel);
                    }
                    linearLayoutManager = new LinearLayoutManager(GudangStok.this);
                    adapter = new ListKopiBelumDigilingAdapter(GudangStok.this, listData);
                    recyclerViewKopiBelumDigiling.setAdapter(adapter);
                    recyclerViewKopiBelumDigiling.setLayoutManager(new LinearLayoutManager(GudangStok.this));
                    adapter.notifyDataSetChanged();
                    adapter.setOnItemClickCallback(new ListKopiBelumDigilingAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(DataModelKopiBelumDigiling dataModel) {
                            Intent i = new Intent(GudangStok.this, GudangStokTambahData.class);
                            i.putExtra("id_penjemuran", dataModel.getId_penjemuran());
                            i.putExtra("berat_akhir_proses", dataModel.getBerat_akhir());
                            i.putExtra("id_pengguna", id_pengguna);
                            i.putExtra("nama_pengguna", nama_pengguna);
                            startActivity(i);
                            finish();
                        }
                    });
                } catch (JSONException e) {
                    tvKopiBelumDigiling.setTextSize(0);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(GudangStok.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void GetKopiSiapDistribusi(){
        String url = getString(R.string.localhost)+"=getdatastokkopibelumjual";
        RequestQueue queue = Volley.newRequestQueue(GudangStok.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData2 = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    BeratKopiGradeA.setText(jsonObject.getString("total_berat_kopi_tanpa_kulit_grade_1"));
                    BeratKopiGradeB.setText(jsonObject.getString("total_berat_kopi_tanpa_kulit_grade_2"));
                    BeratKopiGradeC.setText(jsonObject.getString("total_berat_kopi_tanpa_kulit_grade_3"));
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel2 = new DataModelKopiSiapDistribusi();
                        dataModel2.setId_stok(object.getString("id_stok"));
                        dataModel2.setBerat_kopi(object.getString("berat_kopi_tanpa_kulit") + " Kg");
                        dataModel2.setHarga_jual("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(object.getString("harga_jual"))));
                        dataModel2.setGrade(object.getString("grade"));
                        listData2.add(dataModel2);
                    }
                    //sum all harga_jual then set TotalHargaJual
                    int sum = 0;
                    for (int i = 0; i < listData2.size(); i++) {
                        sum += Integer.parseInt(listData2.get(i).getHarga_jual().replace("Rp. ", "").replace(",", ""));
                    }
                    TotalHargaJual.setText("Rp. " + NumberFormat.getNumberInstance(Locale.US).format(sum));
                    linearLayoutManager2 = new LinearLayoutManager(GudangStok.this, LinearLayoutManager.HORIZONTAL, false);
                    adapter2 = new ListKopiSiapDistribusiAdapter(GudangStok.this, listData2);
                    recyclerViewKopiSiapDistribusi.setAdapter(adapter2);
                    recyclerViewKopiSiapDistribusi.setLayoutManager(linearLayoutManager2);
                    adapter2.notifyDataSetChanged();
                } catch (JSONException e) {
                    nodata.setTextSize(20);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(GudangStok.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void GetKopiTerjual(){
        String url = getString(R.string.localhost)+"=getdatakopiterjuallimit5row";
        RequestQueue queue = Volley.newRequestQueue(GudangStok.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData3 = new ArrayList<>();
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    //just loop 5 times
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel3 = new DataModelHistoryPenjualan();
                        dataModel3.setId_penjualan(object.getString("id_penjualan"));
                        dataModel3.setNama_pembeli(object.getString("nama_pembeli"));
                        dataModel3.setBerat(object.getString("berat_kopi")+" Kg");
                        dataModel3.setHarga_jual("Rp. "+ NumberFormat.getNumberInstance(Locale.US).format(Integer.parseInt(object.getString("harga_penjualan"))));
                        dataModel3.setTanggal_jual(object.getString("tanggal_penjualan"));
                        dataModel3.setNama_penjual(object.getString("nama_pengguna"));
                        dataModel3.setGrade(object.getString("grade"));
                        listData3.add(dataModel3);
                    }
                    linearLayoutManager3 = new LinearLayoutManager(GudangStok.this);
                    adapter3 = new ListDataHistoryPenjualan(GudangStok.this, listData3);
                    recyclerViewHistoryPenjualanKopi.setAdapter(adapter3);
                    recyclerViewHistoryPenjualanKopi.setLayoutManager(linearLayoutManager3);
                    adapter3.notifyDataSetChanged();
                } catch (JSONException e) {
                    //e.getLocalizedMessage();
                    //Toast.makeText(GudangStok.this, e.getLocalizedMessage(), Toast.LENGTH_LONG).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Toast.makeText(GudangStok.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

}