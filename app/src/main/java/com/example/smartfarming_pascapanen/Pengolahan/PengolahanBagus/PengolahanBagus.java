package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.MainActivity;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah.MetodeBasahSortingBagusTambahData;
import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.Sorting.DashboardSorting;
import com.example.smartfarming_pascapanen.RawData.Sorting.DataModelSortingBagus;
import com.example.smartfarming_pascapanen.RawData.Sorting.ListDataSortingBagusAdapter;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class PengolahanBagus extends AppCompatActivity {

    Button tambahData;
    Dialog DialogPilihMetode, DialogPilihIdSorting;

    RecyclerView recyclerViewDataKopiBelumProses, recyclerViewDataKopiSedangFermentasi;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    DataModelPengolahanSortingBagus dataModel;
    ListPengolahanDataSortingBagus adapter;
    List<DataModelPengolahanSortingBagus> listData;

    DataModelSortingBagusSedangFermentasi dataModel2;
    ListSortingBagusSedangFermentasiAdapter adapter2;
    List<DataModelSortingBagusSedangFermentasi> listData2;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengolahan_bagus);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        tambahData = findViewById(R.id.btnTambah);
        DialogPilihMetode = new Dialog(this);
        DialogPilihIdSorting = new Dialog(this);
        recyclerViewDataKopiBelumProses = findViewById(R.id.recycler_view_kopi_bagus_belum_diproses);
        recyclerViewDataKopiSedangFermentasi = findViewById(R.id.recycler_view_sedang_fermentasi);


        tambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpMetodePengolahan(view, id_pengguna, nama_pengguna);
            }
        });

        GetKopiBelumProses();
        GetKopiSedangFermentasi();

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

    private void PopUpMetodePengolahan(View view, String id_pengguna, String nama_pengguna) {
        CardView Basah, Kering;
        DialogPilihMetode.setContentView(R.layout.component_pilih_proses_pengolahan_bagus);
        Basah = DialogPilihMetode.findViewById(R.id.menu_metode_basah);
        Kering = DialogPilihMetode.findViewById(R.id.menu_metode_kering);

        Basah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PilihIdSorting(view, id_pengguna, nama_pengguna);
                DialogPilihMetode.dismiss();
            }
        });
        //make full width
        DialogPilihMetode.getWindow().setLayout(1000, 1100);
        DialogPilihMetode.getWindow().setGravity(Gravity.BOTTOM);
        DialogPilihMetode.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
        DialogPilihMetode.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DialogPilihMetode.show();

    }

    private void PilihIdSorting(View view, String id_pengguna, String nama_pengguna) {

        DialogPilihIdSorting.setContentView(R.layout.component_pilih_id_sorting);
        AutoCompleteTextView textIDSorting = DialogPilihIdSorting.findViewById(R.id.autoCompleteTextViewPilihIDSorting);
        Button btnPilihIDSorting = DialogPilihIdSorting.findViewById(R.id.pilihIDSorting);
        String url = getString(R.string.localhost)+"=getdatasortingbagusbelumproses";
        RequestQueue queue = Volley.newRequestQueue(PengolahanBagus.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String[] id_sorting = new String[jsonArray.length()];
                    String[] berat_sorting = new String[jsonArray.length()];
                    String[] id_panen = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id_sorting[i] = object.getString("id_sorting");
                        berat_sorting[i] = object.getString("berat");
                        id_panen[i] = object.getString("id_panen");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PengolahanBagus.this, android.R.layout.simple_list_item_1, id_sorting);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(PengolahanBagus.this, android.R.layout.simple_list_item_1, berat_sorting);
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(PengolahanBagus.this, android.R.layout.simple_list_item_1, id_panen);
                    textIDSorting.setAdapter(adapter);
                    textIDSorting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String berat = adapter2.getItem(i);
                            String id_panen = adapter3.getItem(i);
                            btnPilihIDSorting.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String id_sorting = textIDSorting.getText().toString();
                                    if (textIDSorting.getText().toString().equals("Klik Disini Untuk Pilih \nID Sorting")){
                                        Toast.makeText(PengolahanBagus.this, "ID Sorting Belum Dipilih", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(PengolahanBagus.this, MetodeBasahSortingBagusTambahData.class);
                                        intent.putExtra("id_pengguna", id_pengguna);
                                        intent.putExtra("nama_pengguna", nama_pengguna);
                                        intent.putExtra("id_sorting", id_sorting);
                                        intent.putExtra("berat_sorting", berat);
                                        intent.putExtra("id_panen", id_panen);
                                        startActivity(intent);
                                        DialogPilihIdSorting.dismiss();
                                        finish();
                                    }
                                }
                            });
                        }
                    });

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
        DialogPilihIdSorting.getWindow().setLayout(1000, 1000);
        DialogPilihIdSorting.getWindow().setGravity(Gravity.BOTTOM);
        DialogPilihIdSorting.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
        DialogPilihIdSorting.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DialogPilihIdSorting.show();
    }

    private  void GetKopiSedangFermentasi(){
        String url = getString(R.string.localhost)+"=getdatabagussedangprosesfermentasi";
        RequestQueue queue = Volley.newRequestQueue(PengolahanBagus.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData2 = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        dataModel2 = new DataModelSortingBagusSedangFermentasi();
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel2.setId_fermentasi(object.getString("id_fermentasi"));
                        dataModel2.setTanggal_mulai(object.getString("tanggal_awal_proses"));
                        dataModel2.setTanggal_akhir(object.getString("tanggal_akhir_proses"));
                        dataModel2.setBerat_fermentasi(object.getString("berat_awal_proses"));
                        listData2.add(dataModel2);
                    }
                    linearLayoutManager2 = new LinearLayoutManager(PengolahanBagus.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewDataKopiSedangFermentasi.setLayoutManager(linearLayoutManager2);
                    adapter2 = new ListSortingBagusSedangFermentasiAdapter(PengolahanBagus.this, listData2);
                    recyclerViewDataKopiSedangFermentasi.setAdapter(adapter2);
                    adapter2.notifyDataSetChanged();
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