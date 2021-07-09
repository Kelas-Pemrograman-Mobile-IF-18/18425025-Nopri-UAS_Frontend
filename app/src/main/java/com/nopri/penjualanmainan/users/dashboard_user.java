package com.nopri.penjualanmainan.users;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.tabs.TabItem;
import com.google.android.material.tabs.TabLayout;
import com.nopri.penjualanmainan.LoginActivity;
import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.admin.dashboard_admin;
import com.nopri.penjualanmainan.session.PrefSetting;
import com.nopri.penjualanmainan.session.SessionManager;

public class dashboard_user extends AppCompatActivity {
    TextView usernama;
    Button keluaruser, profileuser;
    CardView actionfig, baragkol, boneka, pesananuser;
    PrefSetting prefSetting;
    SharedPreferences prefs;
    SessionManager session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dashboard_user);

        prefSetting = new PrefSetting(this);
        prefs = prefSetting.getSharedPreferences();

        session = new SessionManager(dashboard_user.this);

        prefSetting.isLogin(session, prefs);
        usernama = (TextView) findViewById(R.id.usernama);
        actionfig = (CardView) findViewById(R.id.actionfig);
        baragkol = (CardView) findViewById(R.id.barangkol);
        boneka = (CardView) findViewById(R.id.boneka);
        pesananuser = (CardView) findViewById(R.id.pesananusr);
        keluaruser = (Button) findViewById(R.id.keluark);
        profileuser = (Button) findViewById(R.id.profilusr);

        usernama.setText(prefSetting.username);

        actionfig.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_user.this, actionfiActivity.class);
                startActivity(i);
                finish();
            }
        });

        boneka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_user.this, bonekaActivity.class);
                startActivity(i);
                finish();
            }
        });

        baragkol.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_user.this, koleksiActivity.class);
                startActivity(i);
                finish();
            }
        });

        pesananuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_user.this, pesanan_user.class);
                startActivity(i);
                finish();
            }
        });

        profileuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(dashboard_user.this, profile_usr.class);
                startActivity(i);
                finish();
            }
        });

        keluaruser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                session.setLogin(false);
                session.setSessid(0);
                Intent i = new Intent(dashboard_user.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}