package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeKering;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
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
import com.example.smartfarming_pascapanen.Informasi.InformasiCuaca;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah.MetodeBasahSortingBagusTambahData;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.PengolahanBagus;
import com.example.smartfarming_pascapanen.R;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class MetodeKeringSortingBagusTambahData extends AppCompatActivity {

    TextView txtIDFermentasi,txtNamaPengguna, txtBeratFermentasi, target_tKulit, target_dKulit;
    EditText IdPenjemuran, tglAwalProses, tglAkhirProses;
    Button btnTambahData, btnHapusData;
    Dialog dialogPopUp, infoPopUp;
    CardView lihatDetailCuaca;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pengolahan_metode_kering_sorting_bagus_tambah_data);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");
        String id_fermentasi = i.getStringExtra("id_fermentasi");
        String id_sorting = i.getStringExtra("id_sorting");
        String berat_fermentasi = i.getStringExtra("berat_akhir_proses");
        String id_panen = i.getStringExtra("id_panen");

        IdPenjemuran = findViewById(R.id.id_penjemuran);
        txtIDFermentasi = findViewById(R.id.id_fermentasi_jemur);
        txtNamaPengguna = findViewById(R.id.nama_pengguna);
        txtBeratFermentasi = findViewById(R.id.info_berat_akhir);
        tglAwalProses = findViewById(R.id.tanggal_mulai_penjemuran);
        tglAkhirProses = findViewById(R.id.tanggal_akhir_penjemuran);
        btnTambahData = findViewById(R.id.tambah_proses);
        btnHapusData = findViewById(R.id.hapus_data);
        target_dKulit = findViewById(R.id.target_dengan_kulit);
        target_tKulit = findViewById(R.id.target_tanpa_kulit);
        lihatDetailCuaca = findViewById(R.id.lihat_detail_cuaca);
        dialogPopUp = new Dialog(this);
        infoPopUp = new Dialog(this);

        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.DAY_OF_YEAR, 25);
        Date tanggal = calendar.getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        String tanggal25Selanjutnya = dateFormat.format(tanggal);


        String GenIdPenjemuran = "PEN" + new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date()) + "SBA";;
        String dateNow = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        IdPenjemuran.setText(GenIdPenjemuran);
        txtIDFermentasi.setText(id_fermentasi);
        txtNamaPengguna.setText(nama_pengguna);
        txtBeratFermentasi.setText(berat_fermentasi);
        tglAwalProses.setText(dateNow);
        tglAkhirProses.setText(tanggal25Selanjutnya);

        int dengan_kulit = Integer.parseInt(berat_fermentasi) * 35 / 100;
        int tanpa_kulit = Integer.parseInt(berat_fermentasi) * 25 / 100;

        target_dKulit.setText(dengan_kulit+"Kg" +" - "+ (dengan_kulit+2)+"Kg");
        target_tKulit.setText((tanpa_kulit-1)+"Kg" +" - "+ (tanpa_kulit+1)+"Kg");



        btnTambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_penjemuran = IdPenjemuran.getText().toString();
                String id_fermentasi_jemur = id_fermentasi;
                String id_pengguna_jemur = id_pengguna;
                String id_panen_jemur = id_panen;
                String id_sorting_jemur = id_sorting;
                String berat_fermentasi_jemur = berat_fermentasi;
                String nama_pengguna_jemur = nama_pengguna;
                String tgl_awal_proses = tglAwalProses.getText().toString();
                String tgl_akhir_proses = tglAkhirProses.getText().toString();

                //setError if empty
                if (id_penjemuran.isEmpty()) {
                    IdPenjemuran.setError("ID Penjemuran tidak boleh kosong");
                } else if (tgl_awal_proses.isEmpty()) {
                    tglAwalProses.setError("Tanggal Awal Proses tidak boleh kosong");
                } else if (tgl_akhir_proses.isEmpty()) {
                    tglAkhirProses.setError("Tanggal Akhir Proses tidak boleh kosong");
                } else {
                    InputDataPenjemuran(id_penjemuran, id_fermentasi_jemur, id_pengguna_jemur, id_panen_jemur, id_sorting_jemur, berat_fermentasi_jemur, tgl_awal_proses, tgl_akhir_proses, nama_pengguna_jemur);
                }
            }
        });

        lihatDetailCuaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MetodeKeringSortingBagusTambahData.this, InformasiCuaca.class);
                startActivity(i);
            }
        });

        tglAwalProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MetodeKeringSortingBagusTambahData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        tglAwalProses.setText(sdf.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

        tglAkhirProses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final  Calendar calendar = Calendar.getInstance();
                calendar.add(Calendar.DAY_OF_YEAR, 25);
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(MetodeKeringSortingBagusTambahData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                        tglAkhirProses.setText(sdf.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

    }

    private void InputDataPenjemuran(String id_penjemuran, String id_fermentasi_jemur, String id_pengguna_jemur, String id_panen_jemur, String id_sorting_jemur, String berat_fermentasi_jemur, String tgl_awal_proses, String tgl_akhir_proses, String nama_pengguna_jemur) {
        Button simpan, batal;
        dialogPopUp.setContentView(R.layout.component_dialog);
        batal = dialogPopUp.findViewById(R.id.batal);
        simpan = dialogPopUp.findViewById(R.id.simpan_data);
        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialogPopUp.dismiss();
            }
        });

        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.localhost) + "=inputdatapenjemuranbarusortingbagus";
                RequestQueue queue = Volley.newRequestQueue(MetodeKeringSortingBagusTambahData.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("pesan");
                            if (status.equals("1")) {
                                ShowInfoPopup(view, id_pengguna_jemur, nama_pengguna_jemur,status, pesan);
                                dialogPopUp.dismiss();
                            } else if (status.equals("0")) {
                                ShowInfoPopup(view, id_pengguna_jemur, nama_pengguna_jemur,status, pesan);
                                dialogPopUp.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                       //show error
                        Toast.makeText(MetodeKeringSortingBagusTambahData.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_penjemuran", id_penjemuran);
                        params.put("id_fermentasi", id_fermentasi_jemur);
                        params.put("id_pengguna", id_pengguna_jemur);
                        params.put("id_panen", id_panen_jemur);
                        params.put("id_sorting_bagus", id_sorting_jemur);
                        params.put("berat_awal_proses", berat_fermentasi_jemur);
                        params.put("tanggal_awal_proses", tgl_awal_proses);
                        params.put("tanggal_akhir_proses", tgl_akhir_proses);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
        dialogPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogPopUp.show();
    }

    private void ShowInfoPopup(View view,String id_pengguna_sorting, String nama_pengguna_sorting, String status, String pesan) {
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
                                    Intent intent = new Intent(MetodeKeringSortingBagusTambahData.this, PengolahanBagus.class);
                                    intent.putExtra("id_pengguna", id_pengguna_sorting);
                                    intent.putExtra("nama_pengguna", nama_pengguna_sorting);
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