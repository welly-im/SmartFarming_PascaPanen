package com.example.smartfarming_pascapanen.GudangStok.GudangStokTambahData;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
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
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.GudangStok.GudangStok;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek.MetodeKering.MetodeKeringSortingJelekTambahData;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek.PengolahanJelek;
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

public class GudangStokTambahData extends AppCompatActivity {

    Button btnSimpan, btnKembali;
    TextView namaPengguna, InfoIdPenjemuran, InfoBeratAkhir, infoHargaJual, tvKetInfoHargaJual, tvInfoPengolahan;
    EditText edtIdStok, edtBerat, edtTanggalMasuk;
    AutoCompleteTextView gradeKopi;
    Dialog dialogPopUp, infoPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gudang_stok_tambah_data);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");
        String id_penjemuran = i.getStringExtra("id_penjemuran");
        String berat_akhir_proses = i.getStringExtra("berat_akhir_proses");

        final String[] id_grade_fix = {""};

        namaPengguna = findViewById(R.id.nama_pengguna);
        InfoIdPenjemuran = findViewById(R.id.info_id_penjemuran);
        InfoBeratAkhir = findViewById(R.id.info_berat_akhir_penjemuran);
        infoHargaJual = findViewById(R.id.info_harga_jual);
        edtIdStok = findViewById(R.id.id_stok);
        edtBerat = findViewById(R.id.berat_kopi);
        edtTanggalMasuk = findViewById(R.id.tanggal_masuk_gudang);
        gradeKopi = findViewById(R.id.autoCompleteTextViewGradeKopi);
        tvKetInfoHargaJual = findViewById(R.id.ketnfo_harga_jual);
        tvInfoPengolahan = findViewById(R.id.info_pengolahan);

        btnKembali = findViewById(R.id.kembali);
        btnSimpan = findViewById(R.id.tambahDataKopi);

        dialogPopUp = new Dialog(this);
        infoPopUp = new Dialog(this);

        namaPengguna.setText(nama_pengguna);
        InfoIdPenjemuran.setText(id_penjemuran);
        InfoBeratAkhir.setText(berat_akhir_proses);

        String dateNow = "STOK" + new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        edtIdStok.setText(dateNow);
        edtTanggalMasuk.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date()));

        edtTanggalMasuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(GudangStokTambahData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        edtTanggalMasuk.setText(sdf.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(GudangStokTambahData.this, GudangStok.class);
                i.putExtra("id_pengguna", id_pengguna);
                i.putExtra("nama_pengguna", nama_pengguna);
                startActivity(i);
                finish();
            }
        });

        String url = getString(R.string.localhost)+"=getgradekopi";
        RequestQueue queue = Volley.newRequestQueue(GudangStokTambahData.this);
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    JSONArray jsonArray = jsonObject.getJSONArray("data");
                    String[] id_grade = new String[jsonArray.length()];
                    String[] grade = new String[jsonArray.length()];
                    String[] hargaperkg = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject1 = jsonArray.getJSONObject(i);
                        id_grade[i] = jsonObject1.getString("id_grade");
                        grade[i] = jsonObject1.getString("grade");
                        hargaperkg[i] = jsonObject1.getString("harga_per_kg");
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(GudangStokTambahData.this, android.R.layout.simple_list_item_1, grade);
                    ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(GudangStokTambahData.this, android.R.layout.simple_list_item_1, hargaperkg);
                    ArrayAdapter<String> adapter3 = new ArrayAdapter<String>(GudangStokTambahData.this, android.R.layout.simple_list_item_1, id_grade);
                    gradeKopi.setAdapter(adapter);
                    gradeKopi.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                            String grade = adapterView.getItemAtPosition(i).toString();
                            String InfoHarga = adapter2.getItem(i);
                            String idGrade = adapter3.getItem(i);
                            id_grade_fix[0] = String.valueOf(Integer.parseInt(idGrade));
                            if (Integer.parseInt(edtBerat.getText().toString()) > 0) {
                                try {
                                    edtIdStok.setText(edtIdStok.getText().toString() + grade);
                                    int harga = Integer.parseInt(InfoHarga);
                                    int berat = Integer.parseInt(edtBerat.getText().toString());
                                    int total = harga * berat;
                                    //setText with currency format Rp.
                                    infoHargaJual.setText("Rp. " +NumberFormat.getNumberInstance(Locale.US).format(total));
                                    infoHargaJual.setTextSize(18);
                                    tvKetInfoHargaJual.setTextSize(10);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    Toast.makeText(GudangStokTambahData.this, "Berat tidak boleh kosong", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
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

        btnSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_stok = edtIdStok.getText().toString();
                String id_penjemuran = InfoIdPenjemuran.getText().toString();
                String id_grade = gradeKopi.getText().toString();
                String berat = edtBerat.getText().toString();
                String tanggal_masuk = edtTanggalMasuk.getText().toString();
                String harga_jual = infoHargaJual.getText().toString();
                String berat_akhir = InfoBeratAkhir.getText().toString();

                if (id_stok.isEmpty() || id_penjemuran.isEmpty() || id_grade.isEmpty() || berat.isEmpty() || tanggal_masuk.isEmpty() || harga_jual.isEmpty() || berat_akhir.isEmpty()) {
                    Toast.makeText(GudangStokTambahData.this, "Data tidak boleh kosong", Toast.LENGTH_SHORT).show();
                } else {
                    ShowPopup(id_pengguna, nama_pengguna, id_stok, id_penjemuran, id_grade_fix[0], berat, tanggal_masuk);
                }
            }
        });

        InfoPengolahan(id_penjemuran);
    }

    private void InfoPengolahan(String id_penjemuran) {
        String url = getString(R.string.localhost)+"=findinfopengolahankopi";
        RequestQueue queue = Volley.newRequestQueue(GudangStokTambahData.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try{
                    JSONObject jsonObject = new JSONObject(response);
                    tvInfoPengolahan.setText(jsonObject.getString("info_pengolahan"));
                    if (jsonObject.getString("info_pengolahan").equals("Hanya Dijemur")) {
                        tvInfoPengolahan.setTextColor(Color.parseColor("#E92020"));
                    } else {
                        tvInfoPengolahan.setTextColor(Color.parseColor("#FFE61B"));
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
            }
        })
        {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_penjemuran", id_penjemuran);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void ShowPopup( String id_pengguna, String nama_pengguna, String id_stok, String id_penjemuran, String id_grade_fix, String berat, String tanggal_masuk) {
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
                String url = getString(R.string.localhost) + "=inputdatastokkopibaru";
                RequestQueue queue = Volley.newRequestQueue(GudangStokTambahData.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("pesan");
                            if (status.equals("1")) {
                                ShowInfoPopup(v, id_pengguna,nama_pengguna,status, pesan);
                                dialogPopUp.dismiss();
                            } else if (status.equals("0")) {
                                ShowInfoPopup(v, id_pengguna, nama_pengguna, status, pesan);
                                dialogPopUp.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(GudangStokTambahData.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_pengguna", id_pengguna);
                        params.put("id_stok", id_stok);
                        params.put("id_penjemuran", id_penjemuran);
                        params.put("id_grade", id_grade_fix);
                        params.put("berat_kopi_tanpa_kulit", berat);
                        params.put("tanggal_masuk", tanggal_masuk);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
        dialogPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogPopUp.show();
    }

    private void ShowInfoPopup(View v, String id_pengguna,String nama_pengguna, String status, String pesan) {
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
                                    Intent intent = new Intent(GudangStokTambahData.this, GudangStok.class);
                                    intent.putExtra("id_pengguna", id_pengguna);
                                    intent.putExtra("nama_pengguna", nama_pengguna);
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