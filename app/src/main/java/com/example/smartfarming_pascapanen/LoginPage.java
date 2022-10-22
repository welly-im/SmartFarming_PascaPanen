package com.example.smartfarming_pascapanen;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginPage extends AppCompatActivity {

    Button masuk;
    EditText username, password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        masuk = findViewById(R.id.login);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);

        masuk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String get_username = username.getText().toString().trim();
                String get_password = password.getText().toString().trim();

                if(get_username.isEmpty()){
                    username.setError("Nama Pengguna tidak boleh kosong!");
                    username.requestFocus();
                }else if(get_password.isEmpty()){
                    password.setError("Password tidak boleh kosong!");
                    password.requestFocus();
                }else{
                    User_Login(get_username,get_password);
                }
            }
            private void User_Login(String get_username, String get_password){
                String url = getString(R.string.localhost)+"=login";
                RequestQueue queue = Volley.newRequestQueue(LoginPage.this);
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonObject = new JSONObject(response);
                            String status = jsonObject.getString("status");
                            String message = jsonObject.getString("pesan");
                            String jabatan = jsonObject.getString("jabatan");
                            String id_pengguna = jsonObject.getString("id_pengguna");
                            String nama_pengguna = jsonObject.getString("nama_pengguna");
                            if(status.equals("1") && jabatan.equals("1")){
                                Intent i = new Intent(LoginPage.this, MainActivity.class);
                                i.putExtra("id_pengguna", id_pengguna);
                                i.putExtra("nama_pengguna", nama_pengguna);
                                startActivity(i);
                                finish();
                            }else if(status.equals("1") && jabatan.equals("2")){
                                Toast.makeText(LoginPage.this, "Anda Tidak Boleh Masuk!", Toast.LENGTH_SHORT).show();
                            }else{
                                Toast.makeText(LoginPage.this, message, Toast.LENGTH_SHORT).show();
                            }
                        }
                        catch (Exception e){
                            Toast.makeText(LoginPage.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(LoginPage.this, error.toString(), Toast.LENGTH_SHORT).show();
                    }
                }){
                    @Override
                    protected Map<String, String> getParams() throws AuthFailureError {
                        Map<String, String> params = new HashMap<String, String>();
                        params.put("nama_pengguna", get_username);
                        params.put("password", get_password);
                        return params;
                    }
                };
                queue.add(stringRequest);
            }
        });
    }
}