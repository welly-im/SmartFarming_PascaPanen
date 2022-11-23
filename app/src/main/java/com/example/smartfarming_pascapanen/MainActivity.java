package com.example.smartfarming_pascapanen;

import androidx.appcompat.app.AppCompatActivity;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
import com.example.smartfarming_pascapanen.Informasi.InformasiCuaca;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeKering.MetodeKeringSortingBagusTambahData;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.PengolahanBagus;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek.PengolahanJelek;
import com.example.smartfarming_pascapanen.RawData.MenuRaw;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    TextView textNama, tvBeratPanen, tvBeratSortingBagus, tvBeratSortingJelek,
            tvBeratGradeA, tvBeratGradeB, tvBeratGradeC, tvBeratStokKopi, tvBeratFermentasi, tvBeratPenjemuran;
    ImageButton btnRaw, btnPengolahan, btnStok, btnCuaca;
    Dialog DialogPilihSorting;
    View userIcon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textNama = findViewById(R.id.textNama);
        btnRaw = findViewById(R.id.raw);
        btnCuaca = findViewById(R.id.info_cuaca);
        btnPengolahan = findViewById(R.id.pengolahan);
        btnStok = findViewById(R.id.finish);
        DialogPilihSorting = new Dialog(this);
        userIcon = findViewById(R.id.user_icon);

        tvBeratPanen = findViewById(R.id.beratPanen);
        tvBeratSortingBagus = findViewById(R.id.beratSortingBagus);
        tvBeratSortingJelek = findViewById(R.id.beratSortingJelek);
        tvBeratGradeA = findViewById(R.id.berat_kopi_grade_a);
        tvBeratGradeB = findViewById(R.id.berat_kopi_grade_b);
        tvBeratGradeC = findViewById(R.id.berat_kopi_grade_c);
        tvBeratStokKopi = findViewById(R.id.beratStokKopi);
        tvBeratFermentasi = findViewById(R.id.beratFermentasi);
        tvBeratPenjemuran = findViewById(R.id.beratPenjemuran);

        Intent i = getIntent();
        String id_pengguna = i.getStringExtra("id_pengguna");
        String nama_pengguna = i.getStringExtra("nama_pengguna");

        btnRaw.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, MenuRaw.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
        });

        btnPengolahan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopUpPilihSorting(v, id_pengguna, nama_pengguna);
            }
        });

        btnStok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GudangStok.class);
                intent.putExtra("id_pengguna", id_pengguna);
                intent.putExtra("nama_pengguna", nama_pengguna);
                startActivity(intent);
            }
        });

        btnCuaca.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this, InformasiCuaca.class);
                startActivity(i);
            }
        });

        if(id_pengguna == null){
            Intent intent = new Intent(MainActivity.this, LoginPage.class);
            startActivity(intent);
            finish();
        } else {
           get_data_pengguna(id_pengguna);
        }

        GetDataDashboard();

        SwipeRefreshLayout swipeRefreshLayout = findViewById(R.id.swipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                GetDataDashboard();
                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    private void get_data_pengguna(String id_pengguna){
        String url = getString(R.string.localhost)+"=find";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    String nama = jsonObject.getString("nama");
                    String jenis_kelamin = jsonObject.getString("jenis_kelamin");
                    Toast.makeText(MainActivity.this, "Selamat Datang " + nama, Toast.LENGTH_SHORT).show();
                    //set textNama Capitlize first letter then lowercase
                    textNama.setText(nama.substring(0, 1).toUpperCase() + nama.substring(1).toLowerCase());
                    if(jenis_kelamin.equals("L")){
                        userIcon.setBackgroundResource(R.drawable.userl);
                    } else if (jenis_kelamin.equals("P")){
                        userIcon.setBackgroundResource(R.drawable.userp);
                    } else {
                        userIcon.setBackgroundResource(R.drawable.user);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("id_pengguna", id_pengguna);
                return params;
            }
        };
        queue.add(stringRequest);
    }

    private void PopUpPilihSorting(View v, String id_pengguna, String nama_pengguna){
        Button KopiBagus, KopiJelek;
        DialogPilihSorting.setContentView(R.layout.component_pilih_sorting);
        KopiBagus = DialogPilihSorting.findViewById(R.id.kopi_bagus);
        KopiJelek = DialogPilihSorting.findViewById(R.id.kopi_jelek);

        KopiBagus.setOnClickListener(v1 -> {
            Intent intent = new Intent(MainActivity.this, PengolahanBagus.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
            DialogPilihSorting.dismiss();
        });
        KopiJelek.setOnClickListener(v1 -> {
            Intent intent = new Intent(MainActivity.this, PengolahanJelek.class);
            intent.putExtra("id_pengguna", id_pengguna);
            intent.putExtra("nama_pengguna", nama_pengguna);
            startActivity(intent);
            DialogPilihSorting.dismiss();
        });
        //make full width
        DialogPilihSorting.getWindow().setLayout(1000, 1000);
        DialogPilihSorting.getWindow().setGravity(Gravity.BOTTOM);
        DialogPilihSorting.getWindow().getAttributes().windowAnimations = androidx.appcompat.R.style.Animation_AppCompat_Dialog;
        DialogPilihSorting.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        DialogPilihSorting.show();
    }

    private void GetDataDashboard(){
        String url = getString(R.string.localhost)+"=infodashboard";
        RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    tvBeratPanen.setText(jsonObject.getString("total_berat"));
                    tvBeratSortingBagus.setText(jsonObject.getString("total_sorting_bagus"));
                    tvBeratSortingJelek.setText(jsonObject.getString("total_sorting_jelek"));
                    tvBeratFermentasi.setText(jsonObject.getString("total_berat_fermentasi"));
                    tvBeratPenjemuran.setText(jsonObject.getString("total_berat_penjemuran"));
                    tvBeratStokKopi.setText(jsonObject.getString("total_berat_stok"));
                    tvBeratGradeA.setText(jsonObject.getString("total_berat_kopi_tanpa_kulit_grade_1"));
                    tvBeratGradeB.setText(jsonObject.getString("total_berat_kopi_tanpa_kulit_grade_2"));
                    tvBeratGradeC.setText(jsonObject.getString("total_berat_kopi_tanpa_kulit_grade_3"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(MainActivity.this, "Terjadi kesalahan!", Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
}