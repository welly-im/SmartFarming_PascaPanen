package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah.MetodeBasahSortingBagusTambahData;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeKering.MetodeKeringSortingBagusTambahData;
import com.example.smartfarming_pascapanen.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class DialogKonfirmasiProses extends DialogFragment {
    Dialog infoPopUp, infoLanjutPopUp;
    RecyclerView recyclerView;
    LinearLayoutManager linearLayoutManager;
    DataModelPengolahanBagusKonfirmasiDataCuacaAdapter dataModel4;
    ListPengolahanBagusKonfirmasiDataCuacaAdapter adapter4;
    List<DataModelPengolahanBagusKonfirmasiDataCuacaAdapter> listData4;
    TextView textGG;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.component_konfirmasi_proses_fermentasi, container);
        recyclerView = view.findViewById(R.id.recycler_view_data_cuaca);
        infoPopUp = new Dialog(getActivity());
        infoLanjutPopUp = new Dialog(getActivity());
        Bundle bundle = getArguments();
        String id_fermentasi = bundle.getString("id_fermentasi","");
        String berat_akhir = bundle.getString("berat_akhir","");
        String tanggal_akhir = bundle.getString("tanggal_akhir","");
        String id_pengguna = bundle.getString("id_pengguna","");
        String nama_pengguna = bundle.getString("nama_pengguna","");
        String id_panen = bundle.getString("id_panen","");
        String id_sorting = bundle.getString("id_sorting","");
        Button batal, proses;
        batal = view.findViewById(R.id.btn_cancel);
        proses = view.findViewById(R.id.btn_ok);

        batal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
        proses.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = getString(R.string.localhost) + "=updatedatafermentasi";
                RequestQueue queue = Volley.newRequestQueue(DialogKonfirmasiProses.this.getContext());
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String pesan = jsonObject.getString("pesan");
                            if (status.equals("1")) {
                                ShowInfoLanjutPopup(pesan, id_fermentasi, id_sorting, id_panen, berat_akhir, tanggal_akhir, id_pengguna, nama_pengguna);
                                dismiss();
                            } else if (status.equals("0")) {
                                ShowInfoPopup(pesan);
                                dismiss();
                            }
                        } catch (Exception e) {
                            Toast.makeText(DialogKonfirmasiProses.this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(DialogKonfirmasiProses.this.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }) {
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<>();
                        params.put("id_fermentasi", id_fermentasi);
                        params.put("berat_akhir_proses", berat_akhir);
                        params.put("tanggal_akhir_proses", tanggal_akhir);
                        params.put("id_pengguna", id_pengguna);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
        getDataCuaca();
        this.getDialog().setTitle("Konfirmasi Proses");
        this.getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        this.getDialog().getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        this.getDialog().getWindow().setGravity(Gravity.BOTTOM);
        this.getDialog().getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;

        return view;
    }

    private void ShowInfoPopup(String pesan) {
        Button ok;
        TextView textInfo;
        infoPopUp.setContentView(R.layout.component_info);
        ok = infoPopUp.findViewById(R.id.ok);
        textInfo = infoPopUp.findViewById(R.id.showinfo);

        textInfo.setText(pesan);
        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //refresh activity
                Intent intent = new Intent(infoPopUp.getContext(), PengolahanBagus.class);
                infoPopUp.getContext().startActivity(intent);
                infoPopUp.dismiss();
            }
        });
        infoPopUp.getWindow().setLayout(1000, 1000);
        infoPopUp.getWindow().setGravity(Gravity.BOTTOM);
        infoPopUp.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
        infoPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        infoPopUp.show();
    }

    private void ShowInfoLanjutPopup(String pesan, String id_fermentasi, String id_sorting, String id_panen_sorting, String berat_akhir, String tanggal_akhir, String id_pengguna, String nama_pengguna) {
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
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                Intent intent = new Intent(infoPopUp.getContext(), PengolahanBagus.class);
                                infoLanjutPopUp.getContext().startActivity(intent);
                                infoLanjutPopUp.dismiss();
                            }
                        }, 1000);

            }
        });
        lanjut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(infoPopUp.getContext(), MetodeKeringSortingBagusTambahData.class);
                intent.putExtra("id_pengguna", id_pengguna);
                intent.putExtra("nama_pengguna", nama_pengguna);
                intent.putExtra("id_panen", id_panen_sorting);
                intent.putExtra("id_sorting", id_sorting);
                intent.putExtra("id_fermentasi", id_fermentasi);
                intent.putExtra("berat_akhir_proses", berat_akhir);
                infoLanjutPopUp.getContext().startActivity(intent);
                infoLanjutPopUp.dismiss();
            }
        });
        infoLanjutPopUp.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        infoLanjutPopUp.show();
    }



    private void getDataCuaca() {
        DateFormat dateFormat = new SimpleDateFormat("dd MMMM yyyy");
        //make date with format dd-mm-yyyy;
        String url = "https://api.openweathermap.org/data/2.5/forecast?lat=-8.21&lon=112.75&cnt=80&units=metric&lang=id&appid=f547764f6e26f7611f43926924364e0d";
        RequestQueue queue = Volley.newRequestQueue(DialogKonfirmasiProses.this.getContext());
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                listData4 = new ArrayList<>();
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    dataModel4 = new DataModelPengolahanBagusKonfirmasiDataCuacaAdapter();
                    JSONArray data = jsonObject.getJSONArray("list");
                    dataModel4.setHari1(dateFormat.format(new Date(data.getJSONObject(1).getLong("dt") * 1000)));
                    dataModel4.setCuaca1(data.getJSONObject(1).getJSONArray("weather").getJSONObject(0).getString("description"));
                    dataModel4.setSuhu1(data.getJSONObject(1).getJSONObject("main").getString("temp"));
                    dataModel4.setHari2(dateFormat.format(new Date(data.getJSONObject(9).getLong("dt") * 1000)));
                    dataModel4.setCuaca2(data.getJSONObject(9).getJSONArray("weather").getJSONObject(0).getString("description"));
                    dataModel4.setSuhu2(data.getJSONObject(9).getJSONObject("main").getString("temp"));
                    dataModel4.setHari3(dateFormat.format(new Date(data.getJSONObject(17).getLong("dt") * 1000)));
                    dataModel4.setCuaca3(data.getJSONObject(17).getJSONArray("weather").getJSONObject(0).getString("description"));
                    dataModel4.setSuhu3(data.getJSONObject(17).getJSONObject("main").getString("temp"));
                    dataModel4.setHari4(dateFormat.format(new Date(data.getJSONObject(25).getLong("dt") * 1000)));
                    dataModel4.setCuaca4(data.getJSONObject(25).getJSONArray("weather").getJSONObject(0).getString("description"));
                    dataModel4.setSuhu4(data.getJSONObject(25).getJSONObject("main").getString("temp"));
                    dataModel4.setHari5(dateFormat.format(new Date(data.getJSONObject(33).getLong("dt") * 1000)));
                    dataModel4.setCuaca5(data.getJSONObject(33).getJSONArray("weather").getJSONObject(0).getString("description"));
                    dataModel4.setSuhu5(data.getJSONObject(33).getJSONObject("main").getString("temp"));
                    dataModel4.setHari6(dateFormat.format(new Date(data.getJSONObject(38).getLong("dt") * 1000)));
                    dataModel4.setCuaca6(data.getJSONObject(38).getJSONArray("weather").getJSONObject(0).getString("description"));
                    dataModel4.setSuhu6(data.getJSONObject(38).getJSONObject("main").getString("temp"));
                    listData4.add(dataModel4);
                    linearLayoutManager = new LinearLayoutManager(DialogKonfirmasiProses.this.getContext());
                    recyclerView.setLayoutManager(linearLayoutManager);
                    adapter4 = new ListPengolahanBagusKonfirmasiDataCuacaAdapter(DialogKonfirmasiProses.this.getContext(), listData4);
                    recyclerView.setAdapter(adapter4);
                } catch (Exception e) {
                    Toast.makeText(DialogKonfirmasiProses.this.getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(DialogKonfirmasiProses.this.getContext(), error.toString(), Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}
