package com.example.smartfarming_pascapanen.RawData;

import androidx.appcompat.app.AppCompatActivity;

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
import com.example.smartfarming_pascapanen.LoginPage;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeKering.MetodeKeringSortingBagusTambahData;
import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.Sorting.InputDataSorting;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InputRawData extends AppCompatActivity {

    TextView textNama;
    EditText IDPanen, BeratPanen, TanggalPanen;
    Button btnTambahData, btnKembali;
    Dialog dialogPopUp, infoPopUp, infoLanjutPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_raw_data);
        Intent input = getIntent();
        String id_pengguna = input.getStringExtra("id_pengguna");
        String nama_pengguna = input.getStringExtra("nama_pengguna");

        String IDdate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        dialogPopUp = new Dialog(this);
        infoPopUp = new Dialog(this);
        infoLanjutPopUp = new Dialog(this);

        textNama = findViewById(R.id.nama_pengguna);
        IDPanen = findViewById(R.id.id_panen);
        BeratPanen = findViewById(R.id.berat_panen);
        TanggalPanen = findViewById(R.id.tanggal_panen);
        btnTambahData = findViewById(R.id.tambah_panen);
        btnKembali = findViewById(R.id.kembali);

        textNama.setText(nama_pengguna);
        IDPanen.setText("P"+IDdate);
        TanggalPanen.setText(date);

        btnTambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String id_panen = IDPanen.getText().toString();
                String berat_panen = BeratPanen.getText().toString();
                String tanggal_panen = TanggalPanen.getText().toString();
                String id_pengguna_input = id_pengguna;
                String nama_pengguna_input = nama_pengguna;
                ShowPopup(view, id_panen, berat_panen, tanggal_panen, id_pengguna_input, nama_pengguna_input);
            }
        });

        btnKembali.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //backto intent
                Intent back = new Intent(InputRawData.this, MenuRaw.class);
                startActivity(back);
                finish();
            }
        });

        TanggalPanen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(InputRawData.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        TanggalPanen.setText(sdf.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

    }
    private void ShowPopup(View v, String id_panen, String berat_panen, String tanggal_panen, String id_pengguna_input, String nama_pengguna_input) {
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
                String url = getString(R.string.localhost) + "=inputdatapanen";
                RequestQueue queue = Volley.newRequestQueue(InputRawData.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("pesan");
                            if (status.equals("1")) {
                                ShowInfoLanjutPopup(v, id_panen, id_pengguna_input, nama_pengguna_input,status, pesan);
                                dialogPopUp.dismiss();
                            } else if (status.equals("0")) {
                                ShowInfoPopup(v, id_pengguna_input, nama_pengguna_input, status, pesan);
                                dialogPopUp.dismiss();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(InputRawData.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_panen", id_panen);
                        params.put("berat", berat_panen);
                        params.put("tanggal_panen", tanggal_panen);
                        params.put("id_pengguna", id_pengguna_input);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
        dialogPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialogPopUp.show();
    }

    private void ShowInfoPopup(View v, String id_pengguna_input, String nama_pengguna_input, String status, String pesan) {
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
                                    Intent intent = new Intent(InputRawData.this, Raw.class);
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

    private void ShowInfoLanjutPopup(View v, String id_panen, String id_pengguna_input, String nama_pengguna_input, String status, String pesan) {
        Button ok, lanjut;
        TextView textInfo;
        infoLanjutPopUp.setContentView(R.layout.component_info_lanjutkan);
        ok = infoLanjutPopUp.findViewById(R.id.ok);
        lanjut = infoLanjutPopUp.findViewById(R.id.lanjut);
        textInfo = infoLanjutPopUp.findViewById(R.id.showinfo);

        textInfo.setText(pesan);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                infoLanjutPopUp.dismiss();
                //settimeout
                if (status.equals("1")){
                    new android.os.Handler().postDelayed(
                            new Runnable() {
                                public void run() {
                                    Intent intent = new Intent(InputRawData.this, Raw.class);
                                    intent.putExtra("id_pengguna", id_pengguna_input);
                                    intent.putExtra("nama_pengguna", nama_pengguna_input);
                                    startActivity(intent);
                                    finish();
                                }
                            }, 1000);
                }
            }
        });
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InputRawData.this, InputDataSorting.class);
                intent.putExtra("id_pengguna", id_pengguna_input);
                intent.putExtra("nama_pengguna", nama_pengguna_input);
                intent.putExtra("id_panen", id_panen);
                startActivity(intent);
                finish();
                infoLanjutPopUp.dismiss();
            }
        });
        infoLanjutPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        infoLanjutPopUp.show();
    }

}