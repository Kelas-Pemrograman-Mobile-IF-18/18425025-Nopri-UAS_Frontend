package com.nopri.penjualanmainan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nopri.penjualanmainan.admin.dashboard_admin;
import com.nopri.penjualanmainan.server.BaseURL;
import com.nopri.penjualanmainan.session.PrefSetting;
import com.nopri.penjualanmainan.session.SessionManager;
import com.nopri.penjualanmainan.users.dashboard_user;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class LoginActivity extends AppCompatActivity {
 Button btnregis, btnlogin;
 EditText edtUN, edtPW;
 ProgressDialog pDialog;
 private RequestQueue mRequestQueue;
 SessionManager session;
 SharedPreferences prefs;
 PrefSetting prefSetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mRequestQueue = Volley.newRequestQueue(this);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnregis = (Button) findViewById(R.id.regis);
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisteActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnlogin = (Button) findViewById(R.id.loginbutton);
        edtUN = (EditText) findViewById(R.id.edtusername);
        edtPW = (EditText) findViewById(R.id.edtpw);

        prefSetting = new PrefSetting(this);
        prefs = prefSetting.getSharedPreferences();

        session = new SessionManager(this);

        prefSetting.CekLogin(session, prefs);


        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String Strusername = edtUN.getText().toString();
                String Strpassword = edtPW.getText().toString();

                if (Strusername.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username Tidak Boleh Kosong!",Toast.LENGTH_LONG).show();
                } else if (Strpassword.isEmpty()){
                    Toast.makeText(getApplicationContext(),"Password Tidak Boleh Kosong!",Toast.LENGTH_LONG).show();
                } else {
                    login(Strusername, Strpassword);
                }
            }
        });
    }

    public void login(String username, String password){

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("password", password);

        pDialog.setMessage("Mohon Tunggu Sebentar.........");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(BaseURL.login, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");

                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                String data = response.getString("data");
                                JSONObject jsonObject = new JSONObject(data);
                                String role = jsonObject.getString("role");
                                String _id = jsonObject.getString("_id");
                                String username = jsonObject.getString("username");
                                String email = jsonObject.getString("email");
                                String notelp = jsonObject.getString("notelp");
                                session.setLogin(true);
                                prefSetting.storeRegIdSharedPreferences(LoginActivity.this, _id,username,email,notelp,role, prefs);
                                if (role.equals("1")) {
                                    Intent i = new Intent(LoginActivity.this, dashboard_admin.class);
                                    startActivity(i);
                                    finish();
                                } else {
                                    Intent i = new Intent(LoginActivity.this, dashboard_user.class);
                                    startActivity(i);
                                    finish();
                                }
                            } else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });
        mRequestQueue.add(req);
    }

    private void showDialog(){
        if(!pDialog.isShowing()){
            pDialog.show();
        }
    }
    private void hideDialog(){
        if(pDialog.isShowing()){
            pDialog.dismiss();
        }
    }
}