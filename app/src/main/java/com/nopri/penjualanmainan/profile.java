package com.nopri.penjualanmainan;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nopri.penjualanmainan.admin.dashboard_admin;
import com.nopri.penjualanmainan.session.PrefSetting;

public class profile extends AppCompatActivity {
TextView txtusername, txtnomortelp, txtemail;
Button backtohome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        txtusername = (TextView) findViewById(R.id.txtusername);
        txtnomortelp = (TextView) findViewById(R.id.txtnomortelp);
        txtemail = (TextView) findViewById(R.id.txtemial);
        backtohome = (Button)findViewById(R.id.backhome);

        txtusername.setText(PrefSetting.username);
        txtnomortelp.setText(PrefSetting.notelp);
        txtemail.setText(PrefSetting.email);

        backtohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profile.this, dashboard_admin.class);
                startActivity(i);
                finish();
            }
        });
    }
}