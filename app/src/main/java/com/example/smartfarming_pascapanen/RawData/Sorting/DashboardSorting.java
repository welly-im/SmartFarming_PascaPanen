package com.example.smartfarming_pascapanen.RawData.Sorting;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class DashboardSorting extends AppCompatActivity {

    Button btnTambahSorting;
    Dialog DialogPilihIDPanen;

    RecyclerView recyclerView, recyclerView2;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2;
    ListDataSortingBagusAdapter listDataSortingBagusAdapter;
    List<DataModelSortingBagus> listData;
    DataModelSortingBagus dataModelSortingBagus;

    ListDataSortingJelekAdapter listDataSortingJelekAdapter;
    List<DataModelSortingJelek> listData2;
    DataModelSortingJelek dataModelSortingJelek;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_sorting);
        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        btnTambahSorting = findViewById(R.id.btnTambahSorting);
        DialogPilihIDPanen = new Dialog(this);
        recyclerView = findViewById(R.id.recycler_view_kopi_bagus);
        recyclerView2 = findViewById(R.id.recycler_view_kopi_jelek);

        btnTambahSorting.setOnClickListener(v -> {
           PopUpPilihID(v, id_pengguna, nama_pengguna);
        });

        getDataKopiBagus();
        getDataKopiJelek();
    }

    private void PopUpPilihID(View v, String id_pengguna, String nama_pengguna) {
        DialogPilihIDPanen.setContentView(R.layout.component_dashboard_sorting_dialog);
        AutoCompleteTextView textIDPanen = DialogPilihIDPanen.findViewById(R.id.autoCompleteTextViewPilihIDPanen);
        Button btnPilihIDPanen = DialogPilihIDPanen.findViewById(R.id.pilihIDPanen);
        String url = getString(R.string.localhost)+"=getidpanenbelumproses";
        RequestQueue queue = Volley.newRequestQueue(DashboardSorting.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String status = jsonObject.getString("status");
                    String data = jsonObject.getString("data");
                    if (status.equals("1")){
                        JSONArray jsonArray = new JSONArray(data);
                        String[] id_panen = new String[jsonArray.length()];
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject object = jsonArray.getJSONObject(i);
                            id_panen[i] = object.getString("id_panen");
                        }
                        ArrayAdapter<String> adapter = new ArrayAdapter<>(DashboardSorting.this, android.R.layout.simple_list_item_1, id_panen);
                        textIDPanen.setAdapter(adapter);
                        textIDPanen.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                            @Override
                            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                                String id_panen = (String) parent.getItemAtPosition(position);
                                textIDPanen.setText(id_panen);
                            }
                        });
                        btnPilihIDPanen.setOnClickListener(v1 -> {
                            String id_panen_dipilih = textIDPanen.getText().toString();
                            if (textIDPanen.getText().toString().equals("Klik Disini Untuk Pilih \nID Panen")){
                                Toast.makeText(DashboardSorting.this, "ID Panen Belum Dipilih", Toast.LENGTH_SHORT).show();
                            }else{
                                Intent i = new Intent(DashboardSorting.this, InputDataSorting.class);
                                i.putExtra("id_panen", id_panen_dipilih);
                                i.putExtra("id_pengguna", id_pengguna);
                                i.putExtra("nama_pengguna", nama_pengguna);
                                startActivity(i);
                                finish();
                            }
                        });
                    }else{
                        Toast.makeText(DashboardSorting.this, "Data Kosong", Toast.LENGTH_SHORT).show();
                    }
                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashboardSorting.this, "Error", Toast.LENGTH_SHORT).show();
            }
        });
        DialogPilihIDPanen.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DialogPilihIDPanen.show();
        queue.add(stringRequest);
    }

    private void getDataKopiBagus(){
        String url = getString(R.string.localhost)+"=getdatasortingbagus";
        RequestQueue queue = Volley.newRequestQueue(DashboardSorting.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData = new ArrayList<>();
                    try{
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            dataModelSortingBagus = new DataModelSortingBagus();
                            JSONObject object = jsonArray.getJSONObject(i);
                            dataModelSortingBagus.setId_sorting(object.getString("id_sorting"));
                            dataModelSortingBagus.setId_panen(object.getString("id_panen"));
                            dataModelSortingBagus.setTanggal_sorting(object.getString("tanggal_sorting"));
                            dataModelSortingBagus.setTanggal_panen(object.getString("tanggal_panen"));
                            dataModelSortingBagus.setBerat(object.getString("berat"));
                            dataModelSortingBagus.setNama_pengguna(object.getString("nama_pengguna"));
                            listData.add(dataModelSortingBagus);
                        }
                        linearLayoutManager = new LinearLayoutManager(DashboardSorting.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView.setLayoutManager(linearLayoutManager);
                        listDataSortingBagusAdapter = new ListDataSortingBagusAdapter(DashboardSorting.this, listData);
                        recyclerView.setAdapter(listDataSortingBagusAdapter);
                        listDataSortingBagusAdapter.notifyDataSetChanged();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashboardSorting.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void getDataKopiJelek(){
        String url = getString(R.string.localhost)+"=getdatasortingjelek";
        RequestQueue queue = Volley.newRequestQueue(DashboardSorting.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData2 = new ArrayList<>();
                    try{
                        JSONObject jsonObject2 = new JSONObject(response);
                        JSONArray jsonArray2 = jsonObject2.getJSONArray("data");
                        for (int i = 0; i < jsonArray2.length(); i++) {
                            dataModelSortingJelek = new DataModelSortingJelek();
                            JSONObject object = jsonArray2.getJSONObject(i);
                            dataModelSortingJelek.setId_sorting(object.getString("id_sorting"));
                            dataModelSortingJelek.setId_panen(object.getString("id_panen"));
                            dataModelSortingJelek.setTanggal_sorting(object.getString("tanggal_sorting"));
                            dataModelSortingJelek.setTanggal_panen(object.getString("tanggal_panen"));
                            dataModelSortingJelek.setBerat(object.getString("berat"));
                            dataModelSortingJelek.setNama_pengguna(object.getString("nama_pengguna"));
                            listData2.add(dataModelSortingJelek);
                        }
                        linearLayoutManager2 = new LinearLayoutManager(DashboardSorting.this, LinearLayoutManager.HORIZONTAL, false);
                        recyclerView2.setLayoutManager(linearLayoutManager2);
                        listDataSortingJelekAdapter = new ListDataSortingJelekAdapter(DashboardSorting.this, listData2);
                        recyclerView2.setAdapter(listDataSortingJelekAdapter);
                        listDataSortingJelekAdapter.notifyDataSetChanged();
                    } catch (Exception e){
                        e.printStackTrace();
                    }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DashboardSorting.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}