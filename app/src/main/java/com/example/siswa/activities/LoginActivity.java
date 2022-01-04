package com.example.siswa.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.siswa.R;
import com.example.siswa.model.LoginResponse;
import com.example.siswa.network.ServiceClient;
import com.example.siswa.network.ServiceGenerator;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    EditText etNis,etPass;
    Spinner spTingkatan, spTahunAjaran;
    ProgressDialog pd;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        etNis = findViewById(R.id.et_nis_siswa);
        etPass = findViewById(R.id.et_pass_siswa);
        spTingkatan = findViewById(R.id.sp_tingkatan);
        spTahunAjaran = findViewById(R.id.sp_tahun_ajaran);

        pd = new ProgressDialog(this);
    }

    public void login(View view) {
        pd.setMessage("loading..");
        pd.setCancelable(false);
        pd.show();

        if (etNis.getText().toString().isEmpty()){
            pd.dismiss();
            Toast.makeText(this, "NIS tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }
        if (etPass.getText().toString().isEmpty()){
            pd.dismiss();
            Toast.makeText(this, "Password tidak boleh kosong", Toast.LENGTH_SHORT).show();
            return;
        }

        String nis = etNis.getText().toString().trim();
        String pass = etPass.getText().toString().trim();
        String tingkatan = spTingkatan.getSelectedItem().toString();
        String tahunAjaran = spTahunAjaran.getSelectedItem().toString();

        ServiceClient service = ServiceGenerator.createService(ServiceClient.class);
        Call<LoginResponse> requestLogin = service.loginSiswa("loginSiswa","login",tingkatan,tahunAjaran,nis,pass);
        requestLogin.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.body().getHasil().equals("succsess")){
                    pd.dismiss();
                    startActivity(new Intent(LoginActivity.this,MainActivity.class));
                    finish();
                }else {
                    Toast.makeText(LoginActivity.this, "login gagal", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                pd.dismiss();
                Toast.makeText(LoginActivity.this, "koneksi eror", Toast.LENGTH_SHORT).show();
            }
        });
    }
}