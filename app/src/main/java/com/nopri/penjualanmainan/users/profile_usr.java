package com.nopri.penjualanmainan.users;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.session.PrefSetting;

public class profile_usr extends AppCompatActivity {
    TextView userameprof, emailprof, notelpprof;
    Button bactohome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_usr);

        userameprof = (TextView)findViewById(R.id.userprof);
        notelpprof = (TextView)findViewById(R.id.telpprof);
        emailprof = (TextView)findViewById(R.id.emailprof);
        bactohome = (Button)findViewById(R.id.backtohome);

        userameprof.setText(PrefSetting.username);
        notelpprof.setText(PrefSetting.notelp);
        emailprof.setText(PrefSetting.email);

        bactohome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(profile_usr.this, dashboard_user.class);
                startActivity(i);
                finish();
            }
        });
    }
}