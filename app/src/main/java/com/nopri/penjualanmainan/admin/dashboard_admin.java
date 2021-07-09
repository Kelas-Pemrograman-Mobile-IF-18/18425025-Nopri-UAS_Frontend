package com.nopri.penjualanmainan.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.nopri.penjualanmainan.LoginActivity;
import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.profile;
import com.nopri.penjualanmainan.session.PrefSetting;
import com.nopri.penjualanmainan.session.SessionManager;

public class dashboard_admin extends AppCompatActivity {
    CardView keluar,input, edit, pesanan, transaksiuser, profileadm;
    TextView admin;

    PrefSetting prefSetting;
    SharedPreferences prefs;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_admin);

            prefSetting = new PrefSetting(this);
            prefs = prefSetting.getSharedPreferences();

            session = new SessionManager(dashboard_admin.this);

            prefSetting.isLogin(session, prefs);

        input = (CardView)findViewById(R.id.cardinput);
        edit = (CardView)findViewById(R.id.cardedit);
        pesanan = (CardView) findViewById(R.id.cardpesanan);
        profileadm = (CardView) findViewById(R.id.cardpprofile);
        transaksiuser = (CardView) findViewById(R.id.cardpesanan);
        admin = (TextView) findViewById(R.id.txtadmin);

        admin.setText(PrefSetting.username);

        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_admin.this, inputdata.class);
                startActivity(i);
                finish();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              Intent i = new Intent(dashboard_admin.this, ActivityDataBarang.class);
              startActivity(i);
              finish();
            }
        });

        profileadm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_admin.this, profile.class);
                startActivity(i);
                finish();
            }
        });

        transaksiuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_admin.this, transaksi_user.class);
                startActivity(i);
                finish();
            }
        });

        keluar = (CardView) findViewById(R.id.KeluarUser);
        keluar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                session.setSessid(0);
                Intent i = new Intent(dashboard_admin.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

    }
}