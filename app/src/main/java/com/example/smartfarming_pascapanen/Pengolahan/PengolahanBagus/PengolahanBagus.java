package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah.DataModelMetodeBasahDataBulan;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah.MetodeBasahSortingBagusTambahData;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeKering.MetodeKeringSortingBagusTambahData;
import com.example.smartfarming_pascapanen.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PengolahanBagus extends AppCompatActivity {

    TextView nodata, nodata1, nodata2;
    Button tambahData;
    Dialog infoPopUp, DialogPilihMetode, DialogPilihIdSorting,
            DialogDetailSedangFermentasi, DialogDetailSelesaiFermentasi,
            DialogSelesaikanFermentasi;

    RecyclerView recyclerViewDataKopiBelumProses, recyclerViewDataKopiSedangFermentasi, recyclerViewDataKopiSedangPenjemuran;
    LinearLayoutManager linearLayoutManager, linearLayoutManager2, linearLayoutManager3;
    DataModelPengolahanSortingBagus dataModel;
    ListPengolahanDataSortingBagus adapter;
    List<DataModelPengolahanSortingBagus> listData;

    DataModelSortingBagusSedangFermentasi dataModel2;
    ListSortingBagusSedangFermentasiAdapter adapter2;
    List<DataModelSortingBagusSedangFermentasi> listData2;

    DataModelSortingBagusSedangPenjemuran dataModel3;
    ListSortingBagusSedangPenjemuranAdapter adapter3;
    List<DataModelSortingBagusSedangPenjemuran> listData3;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengolahan_bagus);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        tambahData = findViewById(R.id.btnTambah);
        nodata = findViewById(R.id.noData);
        nodata1 = findViewById(R.id.noData1);
        nodata2 = findViewById(R.id.noData2);

        infoPopUp = new Dialog(this);
        DialogPilihMetode = new Dialog(this);
        DialogPilihIdSorting = new Dialog(this);
        DialogDetailSedangFermentasi = new Dialog(this);
        DialogDetailSelesaiFermentasi = new Dialog(this);
        DialogSelesaikanFermentasi = new Dialog(this);
        recyclerViewDataKopiBelumProses = findViewById(R.id.recycler_view_kopi_bagus_belum_diproses);
        recyclerViewDataKopiSedangFermentasi = findViewById(R.id.recycler_view_sedang_fermentasi);
        recyclerViewDataKopiSedangPenjemuran = findViewById(R.id.recycler_view_sedang_penjemuran);

        final FragmentManager fm = getSupportFragmentManager();
        final DialogKonfirmasiProses dialogKonfirmasiProses = new DialogKonfirmasiProses();


        tambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopUpMetodePengolahan(view, id_pengguna, nama_pengguna);
            }
        });

        GetKopiBelumProses();
        GetKopiSedangFermentasi(id_pengguna, nama_pengguna, dialogKonfirmasiProses, fm);
        GetKopiSedangPenjemuran();

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
                    nodata.setTextSize(20);
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

        Kering.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PilihIdSortingMetodeKering(view, id_pengguna, nama_pengguna);
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
        TextView judul = DialogPilihIdSorting.findViewById(R.id.judul);
        judul.setText("Pilih ID Sorting");
        TextView ket = DialogPilihIdSorting.findViewById(R.id.keteranganPilihId);
        ket.setText("Silahkan pilih ID Sorting yang ingin diproses");
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
                                    if (textIDSorting.getText().toString().equals("Klik Disini Untuk Pilih ID")){
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
                    Toast.makeText(PengolahanBagus.this, "Data kopi tidak ada!", Toast.LENGTH_SHORT).show();
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

    private void PilihIdSortingMetodeKering(View view, String id_pengguna, String nama_pengguna){
        DialogPilihIdSorting.setContentView(R.layout.component_pilih_id_sorting);
        TextView judul = DialogPilihIdSorting.findViewById(R.id.judul);
        judul.setText("Pilih ID Fermentasi");
        TextView ket = DialogPilihIdSorting.findViewById(R.id.keteranganPilihId);
        ket.setText("Silahkan pilih ID Fermentasi yang selesai diproses");
        AutoCompleteTextView textIDSorting = DialogPilihIdSorting.findViewById(R.id.autoCompleteTextViewPilihIDSorting);
        Button btnPilihIDSorting = DialogPilihIdSorting.findViewById(R.id.pilihIDSorting);
        String url = getString(R.string.localhost)+"=getidbagusselesaiprosesfermentasi";
        RequestQueue queue = Volley.newRequestQueue(PengolahanBagus.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String[] id_fermentasi = new String[jsonArray.length()];
                    String[] id_sorting = new String[jsonArray.length()];
                    String[] berat_sorting = new String[jsonArray.length()];
                    String[] id_panen = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        id_fermentasi[i] = object.getString("id_fermentasi");
                        id_sorting[i] = object.getString("id_sorting_bagus");
                        berat_sorting[i] = object.getString("berat_akhir_proses");
                        id_panen[i] = object.getString("id_panen");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(PengolahanBagus.this, android.R.layout.simple_list_item_1, id_fermentasi);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(PengolahanBagus.this, android.R.layout.simple_list_item_1, id_sorting);
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(PengolahanBagus.this, android.R.layout.simple_list_item_1, berat_sorting);
                    ArrayAdapter<String> adapter4 = new ArrayAdapter<String>(PengolahanBagus.this, android.R.layout.simple_list_item_1, id_panen);
                    textIDSorting.setAdapter(adapter);
                    textIDSorting.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String id_sorting_jemur = adapter2.getItem(i);
                            String berat = adapter3.getItem(i);
                            String id_panen = adapter4.getItem(i);
                            btnPilihIDSorting.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    String id_fermentasi = textIDSorting.getText().toString();
                                    if (textIDSorting.getText().toString().equals("Klik Disini Untuk Pilih ID")){
                                        Toast.makeText(PengolahanBagus.this, "ID Sorting Belum Dipilih", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Intent intent = new Intent(PengolahanBagus.this, MetodeKeringSortingBagusTambahData.class);
                                        intent.putExtra("id_pengguna", id_pengguna);
                                        intent.putExtra("nama_pengguna", nama_pengguna);
                                        intent.putExtra("id_fermentasi", id_fermentasi);
                                        intent.putExtra("id_sorting", id_sorting_jemur);
                                        intent.putExtra("berat_akhir_proses", berat);
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
                    Toast.makeText(PengolahanBagus.this, "Data kopi tidak ada!", Toast.LENGTH_SHORT).show();
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

    private  void GetKopiSedangFermentasi(String id_pengguna, String nama_pengguna, DialogKonfirmasiProses dialogKonfirmasiProses, FragmentManager fm){
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
                    adapter2.setOnItemClickCallback(new ListSortingBagusSedangFermentasiAdapter.OnItemClickCallback() {
                        @Override
                        public void onItemClicked(DataModelSortingBagusSedangFermentasi data) {
                            showSelectedData(data, id_pengguna, nama_pengguna, dialogKonfirmasiProses, fm);
                        }
                    });
                } catch (Exception e) {
                    nodata1.setTextSize(20);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PengolahanBagus.this, "Data fermentasi tidak ada!",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void GetKopiSedangPenjemuran(){
        String url = getString(R.string.localhost)+"=getdatabagussedangprosespenjemuran";
        RequestQueue queue = Volley.newRequestQueue(PengolahanBagus.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData3 = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    for (int i = 0; i < jsonArray.length(); i++) {
                        dataModel3 = new DataModelSortingBagusSedangPenjemuran();
                        JSONObject object = jsonArray.getJSONObject(i);
                        dataModel3.setId_penjemuran(object.getString("id_penjemuran"));
                        dataModel3.setTanggal_mulai(object.getString("tanggal_awal_proses"));
                        dataModel3.setTanggal_akhir(object.getString("tanggal_akhir_proses"));
                        dataModel3.setBerat_penjemuran(object.getString("berat_awal_proses"));
                        listData3.add(dataModel3);
                    }
                    linearLayoutManager3 = new LinearLayoutManager(PengolahanBagus.this, LinearLayoutManager.HORIZONTAL, false);
                    recyclerViewDataKopiSedangPenjemuran.setLayoutManager(linearLayoutManager3);
                    adapter3 = new ListSortingBagusSedangPenjemuranAdapter(PengolahanBagus.this, listData3);
                    recyclerViewDataKopiSedangPenjemuran.setAdapter(adapter3);
                    adapter3.notifyDataSetChanged();
                } catch (Exception e) {
                    nodata2.setTextSize(20);
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PengolahanBagus.this,"Data Penjemuran tidak ada!",Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void showSelectedData(DataModelSortingBagusSedangFermentasi data, String id_pengguna, String nama_pengguna, DialogKonfirmasiProses dialogKonfirmasiProses, FragmentManager fm) {
        String url = getString(R.string.localhost)+"=findidfermentasi";
        RequestQueue queue = Volley.newRequestQueue(PengolahanBagus.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String id_fermentasi = jsonObject.getString("id_fermentasi");
                    String tanggal_mulai = jsonObject.getString("tanggal_awal_proses");
                    String tanggal_akhir = jsonObject.getString("tanggal_akhir_proses");
                    String berat_fermentasi = jsonObject.getString("berat_awal_proses");
                    String id_panen = jsonObject.getString("id_panen");
                    String id_sorting = jsonObject.getString("id_sorting_bagus");
                    String sisa_hari = jsonObject.getString("sisa_hari");
                    if( Integer.parseInt(sisa_hari) > 0){
                        DialogDetailSedangFermentasi.setContentView(R.layout.component_pengolahan_detail_bagus_sedang_fermentasi);
                        TextView txtIdFermentasi, txtTanggalMulai, txtTanggalAkhir, txtBeratFermentasi, txtSisaHari;
                        txtIdFermentasi = DialogDetailSedangFermentasi.findViewById(R.id.detail_id_fermentasi);
                        txtTanggalMulai = DialogDetailSedangFermentasi.findViewById(R.id.detail_mulai_fermentasi);
                        txtTanggalAkhir = DialogDetailSedangFermentasi.findViewById(R.id.detail_selesai_fermentasi);
                        txtBeratFermentasi = DialogDetailSedangFermentasi.findViewById(R.id.detail_berat_fermentasi);
                        txtSisaHari = DialogDetailSedangFermentasi.findViewById(R.id.sisa_hari_fermentasi);

                        txtIdFermentasi.setText(id_fermentasi);
                        txtTanggalMulai.setText(tanggal_mulai);
                        txtTanggalAkhir.setText(tanggal_akhir);
                        txtBeratFermentasi.setText(berat_fermentasi);
                        txtSisaHari.setText(sisa_hari);

                        DialogDetailSedangFermentasi.getWindow().setLayout(1000, 1100);
                        DialogDetailSedangFermentasi.getWindow().setGravity(Gravity.BOTTOM);
                        DialogDetailSedangFermentasi.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
                        DialogDetailSedangFermentasi.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        DialogDetailSedangFermentasi.show();
                    } else {
                        DialogDetailSelesaiFermentasi.setContentView(R.layout.component_pengolahan_detail_bagus_selesai_fermentasi);
                        TextView slsitxtIdFermentasi, slsitxtTanggalMulai, slsitxtTanggalAkhir, slsitxtBeratFermentasi;
                        Button selesaikanFermentasi;

                        slsitxtIdFermentasi = DialogDetailSelesaiFermentasi.findViewById(R.id.detail_id_fermentasi);
                        slsitxtTanggalMulai = DialogDetailSelesaiFermentasi.findViewById(R.id.detail_mulai_fermentasi);
                        slsitxtTanggalAkhir = DialogDetailSelesaiFermentasi.findViewById(R.id.detail_selesai_fermentasi);
                        slsitxtBeratFermentasi = DialogDetailSelesaiFermentasi.findViewById(R.id.detail_berat_fermentasi);
                        selesaikanFermentasi = DialogDetailSelesaiFermentasi.findViewById(R.id.btnSelesaikan);

                        slsitxtIdFermentasi.setText(id_fermentasi);
                        slsitxtTanggalMulai.setText(tanggal_mulai);
                        slsitxtTanggalAkhir.setText(tanggal_akhir);
                        slsitxtBeratFermentasi.setText(berat_fermentasi);

                        selesaikanFermentasi.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                selesaikanFermentasi(id_fermentasi, id_panen, id_sorting, tanggal_mulai, berat_fermentasi, id_pengguna, nama_pengguna, dialogKonfirmasiProses, fm);
                                DialogDetailSelesaiFermentasi.dismiss();
                            }
                        });
                        DialogDetailSelesaiFermentasi.getWindow().setLayout(1000, 1300);
                        DialogDetailSelesaiFermentasi.getWindow().setGravity(Gravity.BOTTOM);
                        DialogDetailSelesaiFermentasi.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
                        DialogDetailSelesaiFermentasi.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
                        DialogDetailSelesaiFermentasi.show();
                    }
                } catch (Exception e) {
                    Toast.makeText(PengolahanBagus.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(PengolahanBagus.this,error.toString(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_fermentasi", data.getId_fermentasi());
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void selesaikanFermentasi(String id_fermentasi, String id_panen, String id_sorting, String tanggal_mulai, String berat_fermentasi, String id_pengguna, String nama_pengguna, DialogKonfirmasiProses dialogKonfirmasiProses, FragmentManager fm) {
        DialogSelesaikanFermentasi.setContentView(R.layout.component_pengolahan_detail_bagus_selesaikan_fermentasi);
        TextView txtIdFermentasi, txtTanggalMulai, txtBeratFermentasi;
        EditText txtBeratAkhir, txtTanggalAkhir;
        Button btnSelesaikan;

        txtIdFermentasi = DialogSelesaikanFermentasi.findViewById(R.id.slsi_detail_id_fermentasi);
        txtTanggalMulai = DialogSelesaikanFermentasi.findViewById(R.id.slsi_detail_awal_fermentasi);
        txtBeratFermentasi = DialogSelesaikanFermentasi.findViewById(R.id.slsi_detail_berat_fermentasi);
        txtBeratAkhir = DialogSelesaikanFermentasi.findViewById(R.id.slsi_berat_akhir_fermentasi);
        txtTanggalAkhir = DialogSelesaikanFermentasi.findViewById(R.id.final_tanggal_akhir_fermentasi);
        btnSelesaikan = DialogSelesaikanFermentasi.findViewById(R.id.btnSelesaikan);

        String tglSekarang = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        txtIdFermentasi.setText(id_fermentasi);
        txtTanggalMulai.setText(tanggal_mulai);
        txtBeratFermentasi.setText(berat_fermentasi);
        txtBeratAkhir.setHint(berat_fermentasi);
        txtTanggalAkhir.setText(tglSekarang);

        txtTanggalAkhir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(PengolahanBagus.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        month = month + 1;
                        String date = year + "-" + month + "-" + day;
                        txtTanggalAkhir.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        btnSelesaikan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String berat_akhir = txtBeratAkhir.getText().toString();
                String tanggal_akhir = txtTanggalAkhir.getText().toString();
                if (berat_akhir.isEmpty() || tanggal_akhir.isEmpty()) {
                    Toast.makeText(PengolahanBagus.this, "Data tidak boleh kosong!!", Toast.LENGTH_SHORT).show();
                } else {
                    Bundle bundle = new Bundle();
                    bundle.putString("id_fermentasi",id_fermentasi);
                    bundle.putString("berat_akhir",berat_akhir);
                    bundle.putString("tanggal_akhir",tanggal_akhir);
                    bundle.putString("id_pengguna",id_pengguna);
                    bundle.putString("nama_pengguna",nama_pengguna);
                    bundle.putString("id_sorting",id_sorting);
                    bundle.putString("id_panen",id_panen);
                    dialogKonfirmasiProses.setArguments(bundle);
                    dialogKonfirmasiProses.show(fm, "Konfirmasi Proses");
                    DialogSelesaikanFermentasi.dismiss();
                }
            }
        });
        DialogSelesaikanFermentasi.getWindow().setLayout(1000, 1500);
        DialogSelesaikanFermentasi.getWindow().setGravity(Gravity.BOTTOM);
        DialogSelesaikanFermentasi.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
        DialogSelesaikanFermentasi.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DialogSelesaikanFermentasi.show();
    }
}