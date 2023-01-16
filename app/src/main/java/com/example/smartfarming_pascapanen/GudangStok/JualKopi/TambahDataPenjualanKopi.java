package com.example.smartfarming_pascapanen.GudangStok.JualKopi;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.GudangStok.GudangStokTambahData.GudangStokTambahData;
import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.InputRawData;
import com.example.smartfarming_pascapanen.RawData.Raw;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class TambahDataPenjualanKopi extends AppCompatActivity {

    TextView nama_pengguna_input, id_stok, gradeKopi, berat, harga_jualKopi,harga_jual_per_kgKopi, keteranganKopi;
    EditText edtIDPenjualan, edtTanggalPenjualan, edtHargaPenjualan;
    Button kembali, simpan;
    AutoCompleteTextView acNamaPembeli;
    Dialog dialogPopUp, infoPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gudang_stok_tambah_data_penjualan_kopi);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");
        String id_stok_kopi = i.getStringExtra("id_stok");
        String berat_kopi = i.getStringExtra("berat");
        String harga_jual = i.getStringExtra("harga_jual");
        String keterangan = i.getStringExtra("keterangan");
        String grade = i.getStringExtra("grade");
        String harga_jual_per_kg = i.getStringExtra("harga_jual_per_kg");

        final String[] id_pembeli_fix = {""};

        id_stok = findViewById(R.id.id_stok);
        gradeKopi = findViewById(R.id.grade);
        berat = findViewById(R.id.berat_stok);
        harga_jualKopi = findViewById(R.id.harga_stok);
        harga_jual_per_kgKopi = findViewById(R.id.harga_stok_per_kg);
        keteranganKopi = findViewById(R.id.ket_stok_kopi);
        nama_pengguna_input = findViewById(R.id.nama_pengguna);
        kembali = findViewById(R.id.kembali);
        simpan = findViewById(R.id.tambahDataKopi);

        edtIDPenjualan = findViewById(R.id.id_penjualan);
        edtTanggalPenjualan = findViewById(R.id.tanggal_penjualan);
        acNamaPembeli = findViewById(R.id.autoCompleteTextViewPembeli);
        edtHargaPenjualan = findViewById(R.id.harga_penjualan);

        dialogPopUp = new Dialog(this);
        infoPopUp = new Dialog(this);

        nama_pengguna_input.setText(nama_pengguna);
        id_stok.setText(id_stok_kopi);
        gradeKopi.setText(grade);
        berat.setText(berat_kopi);
        harga_jualKopi.setText(harga_jual);
        harga_jual_per_kgKopi.setText(harga_jual_per_kg);
        keteranganKopi.setText(keterangan);
        String IdPenjualan = "PJ" + new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()) + grade;
        edtIDPenjualan.setText(IdPenjualan);
        edtTanggalPenjualan.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));
        edtHargaPenjualan.setHint(harga_jual + " adalah harga yang disarankan");

        edtTanggalPenjualan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(TambahDataPenjualanKopi.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        edtTanggalPenjualan.setText(sdf.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        kembali.setOnClickListener(v -> {
            Intent intent = new Intent(TambahDataPenjualanKopi.this, GudangStokPenjualanKopi.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
            finish();
        });

        simpan.setOnClickListener(v -> {
            String id_penjualan = edtIDPenjualan.getText().toString();
            String tanggal_penjualan = edtTanggalPenjualan.getText().toString();
            String harga_penjualan = edtHargaPenjualan.getText().toString();
            harga_penjualan = harga_penjualan.substring(0, harga_penjualan.length() - 2);
            String id_pembeli = id_pembeli_fix[0];
            ShowPopup(id_pengguna, nama_pengguna, id_stok_kopi, berat_kopi, harga_jual, harga_penjualan, id_penjualan, tanggal_penjualan, id_pembeli);
        });

        String url = getString(R.string.localhost)+"=getdatapembeli";
        RequestQueue queue = Volley.newRequestQueue(TambahDataPenjualanKopi.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String[] nama_pembeli = new String[jsonArray.length()];
                    String[] id_pembeli = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject data = jsonArray.getJSONObject(i);
                        nama_pembeli[i] = data.getString("nama_pembeli");
                        id_pembeli[i] = data.getString("id_pembeli");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(TambahDataPenjualanKopi.this, android.R.layout.simple_list_item_1, nama_pembeli);
                    acNamaPembeli.setAdapter(adapter);
                    acNamaPembeli.setOnItemClickListener((parent, view, position, id) -> {
                        id_pembeli_fix[0] = id_pembeli[position];
                    });
                } catch (Exception e) {
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
    private void ShowPopup( String id_pengguna, String nama_pengguna, String id_stok_kopi, String berat_kopi, String harga_jual, String harga_penjualan, String id_penjualan, String tanggal_penjualan, String id_pembeli) {
        Button batal, simpanData;
        dialogPopUp.setContentView(R.layout.component_dialog);
        batal = dialogPopUp.findViewById(R.id.batal);
        simpanData = dialogPopUp.findViewById(R.id.simpan_data);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPopUp.dismiss();
            }
        });
        simpanData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String url = getString(R.string.localhost) + "=inputdatapenjualankopi";
                RequestQueue queue = Volley.newRequestQueue(TambahDataPenjualanKopi.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("pesan");
                            if (status.equals("1")) {
                                ShowInfoPopup(id_pengguna, nama_pengguna,status, pesan);
                                dialogPopUp.dismiss();
                            } else if (status.equals("0")) {
                                ShowInfoPopup(id_pengguna, nama_pengguna, status, pesan);
                                dialogPopUp.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(TambahDataPenjualanKopi.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_pengguna", id_pengguna);
                        params.put("id_stok", id_stok_kopi);
                        params.put("berat_kopi", berat_kopi);
                        params.put("harga_jual", harga_jual);
                        params.put("id_penjualan", id_penjualan);
                        params.put("tanggal_penjualan", tanggal_penjualan);
                        params.put("id_pembeli", id_pembeli);
                        params.put("harga_penjualan", harga_penjualan);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
        dialogPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogPopUp.show();
    }

    private void ShowInfoPopup(String id_pengguna_input, String nama_pengguna_input, String status, String pesan) {
        Button ok;
        TextView textInfo;
        infoPopUp.setContentView(R.layout.component_info);
        ok = infoPopUp.findViewById(R.id.ok);
        textInfo = infoPopUp.findViewById(R.id.showinfo);

        textInfo.setText(pesan);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoPopUp.dismiss();
                //settimeout
                if (status.equals("1")){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(TambahDataPenjualanKopi.this, GudangStokPenjualanKopi.class);
                                    intent.putExtra("id_pengguna", id_pengguna_input);
                                    intent.putExtra("nama_pengguna", nama_pengguna_input);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                }
            }
        });
        infoPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        infoPopUp.show();
    }

}