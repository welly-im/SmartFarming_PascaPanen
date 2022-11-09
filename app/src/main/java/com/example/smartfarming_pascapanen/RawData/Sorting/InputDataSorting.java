package com.example.smartfarming_pascapanen.RawData.Sorting;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeKering.MetodeKeringSortingBagusTambahData;
import com.example.smartfarming_pascapanen.R;
import com.example.smartfarming_pascapanen.RawData.InputRawData;
import com.example.smartfarming_pascapanen.RawData.Raw;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class InputDataSorting extends AppCompatActivity {

    TextView txtIDPanen,txtNamaPengguna, txtBeratPanen;
    EditText edtIDSortingBagus, edtBeratSortingBagus, edtTanggalSorting, edtIDSortingJelek, edtBeratSortingJelek;
    Button btnTambahData, btnHapusData;
    Dialog dialogPopUp, infoPopUp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.input_data_sorting);
        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");
        String id_panen = i.getStringExtra("id_panen");


        txtIDPanen = findViewById(R.id.id_panen);
        txtNamaPengguna = findViewById(R.id.nama_pengguna);
        txtBeratPanen = findViewById(R.id.info_berat_panen);
        edtIDSortingBagus = findViewById(R.id.id_sorting_bagus);
        edtBeratSortingBagus = findViewById(R.id.berat_sorting_bagus);
        edtIDSortingJelek = findViewById(R.id.id_sorting_jelek);
        edtBeratSortingJelek = findViewById(R.id.berat_sorting_jelek);
        edtTanggalSorting = findViewById(R.id.tanggal_sorting);
        btnTambahData = findViewById(R.id.tambah_sorting);
        btnHapusData = findViewById(R.id.hapus_sorting);

        String IDdate = new SimpleDateFormat("yyyyMMdd", Locale.getDefault()).format(new Date());
        String date = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(new Date());

        txtIDPanen.setText(id_panen);
        txtNamaPengguna.setText(nama_pengguna);
        edtIDSortingBagus.setText("SBA"+IDdate);
        edtIDSortingJelek.setText("SJE"+IDdate);
        edtTanggalSorting.setText(date);

        getInfoIDPanen(id_panen);

        dialogPopUp = new Dialog(this);
        infoPopUp = new Dialog(this);

        btnHapusData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                edtIDSortingBagus.setText("SBA"+IDdate);
                edtIDSortingJelek.setText("SJE"+IDdate);
                edtTanggalSorting.setText(date);
                edtBeratSortingBagus.setText("");
                edtBeratSortingJelek.setText("");
            }
        });

        btnTambahData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String id_panen = txtIDPanen.getText().toString();
                String id_sorting_bagus = edtIDSortingBagus.getText().toString();
                String berat_sorting_bagus = edtBeratSortingBagus.getText().toString();
                String tanggal_sorting = edtTanggalSorting.getText().toString();
                String id_sorting_jelek = edtIDSortingJelek.getText().toString();
                String berat_sorting_jelek = edtBeratSortingJelek.getText().toString();
                String id_pengguna_input = id_pengguna;
                String nama_pengguna_input = nama_pengguna;
                if(Integer.parseInt(berat_sorting_bagus) + Integer.parseInt(berat_sorting_jelek) == Integer.parseInt(txtBeratPanen.getText().toString())){
                    inputDataSorting(v, id_panen, id_sorting_bagus, berat_sorting_bagus, tanggal_sorting, id_sorting_jelek, berat_sorting_jelek, id_pengguna_input, nama_pengguna_input);
                } else {
                    String pesan = "Berat Sorting Bagus dan Jelek Tidak Sesuai Dengan Berat Panen!";
                    AlertPopup(v, pesan);
                }
            }
        });

        edtTanggalSorting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Calendar calendar = Calendar.getInstance();
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(InputDataSorting.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, month);
                        calendar.set(Calendar.DAY_OF_MONTH, day);
                        String myFormat = "yyyy-MM-dd";
                        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
                        edtTanggalSorting.setText(sdf.format(calendar.getTime()));
                    }
                }, year, month, day);
                datePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });

       //change edtBeratSortingJelek from calculate txtBeratPanen and edtBeratSortingBagus
        edtBeratSortingBagus.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edtBeratSortingBagus.getText().toString().equals("")) {
                    edtBeratSortingJelek.setText(txtBeratPanen.getText().toString());
                } else {
                    int berat_panen = Integer.parseInt(txtBeratPanen.getText().toString());
                    int berat_sorting_bagus = Integer.parseInt(edtBeratSortingBagus.getText().toString());
                    int berat_sorting_jelek = berat_panen - berat_sorting_bagus;
                    edtBeratSortingJelek.setText(String.valueOf(berat_sorting_jelek));
                    if (berat_sorting_jelek < 0 || berat_sorting_bagus < 0) {
                        edtBeratSortingJelek.setError("Berat tidak sesuai!");
                        edtBeratSortingBagus.setError("Berat tidak sesuai!");
                    }
                }
            }
            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void getInfoIDPanen(String id_panen) {
        String url = getString(R.string.localhost)+"=findpanen";
        RequestQueue queue = Volley.newRequestQueue(InputDataSorting.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String berat_panen = jsonObject.getString("berat_panen");
                    txtBeratPanen.setText(berat_panen);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(InputDataSorting.this, "Error", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id_panen", id_panen);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void inputDataSorting(View v, String id_panen, String id_sorting_bagus, String berat_sorting_bagus, String tanggal_sorting, String id_sorting_jelek, String berat_sorting_jelek, String id_pengguna_input, String nama_pengguna_input) {
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
                String url = getString(R.string.localhost) + "=inputsorting";
                RequestQueue queue = Volley.newRequestQueue(InputDataSorting.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("pesan");
                            if (status.equals("1")) {
                                ShowInfoPopup(v, id_pengguna_input, nama_pengguna_input,status, pesan);
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
                        Toast.makeText(InputDataSorting.this, "Error", Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_panen", id_panen);
                        params.put("id_sorting_bagus", id_sorting_bagus);
                        params.put("berat_kopi_bagus", berat_sorting_bagus);
                        params.put("tanggal_sorting", tanggal_sorting);
                        params.put("id_sorting_jelek", id_sorting_jelek);
                        params.put("berat_kopi_jelek", berat_sorting_jelek);
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
                                    Intent intent = new Intent(InputDataSorting.this, DashboardSorting.class);
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

    private void AlertPopup(View v, String pesan) {
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
            }
        });
        infoPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        infoPopUp.show();
    }

}