package com.nopri.penjualanmainan;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
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
import com.nopri.penjualanmainan.server.BaseURL;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class RegisteActivity extends AppCompatActivity {

    Button btnbacklogin, btnregis;
    EditText usernameregis, emailregis, notelpregis, passwordregis;
    ProgressDialog pDialog;
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mRequestQueue = Volley.newRequestQueue(this);

        usernameregis   = (EditText)findViewById(R.id.usernameregister);
        emailregis      = (EditText)findViewById(R.id.emailregister);
        notelpregis     = (EditText)findViewById(R.id.notelpregister);
        passwordregis   = (EditText)findViewById(R.id.paswordregister);

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        btnbacklogin = (Button) findViewById(R.id.btnbacklogin);
        btnbacklogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(RegisteActivity.this, LoginActivity.class);
                startActivity(i);
                finish();
            }
        });

        btnregis = (Button)findViewById(R.id.btnregis);
        btnregis.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strUsername   = usernameregis.getText().toString();
                String strEmail      = emailregis.getText().toString();
                String strnotelp     = notelpregis.getText().toString();
                String strPassword   = passwordregis.getText().toString();

                if(strUsername.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Username tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else if (strEmail.isEmpty()){
                    Toast.makeText(getApplicationContext(), "E-Mail tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else if (strnotelp.isEmpty()){
                    Toast.makeText(getApplicationContext(), "Nomor Telepon tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else if (strPassword.isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Password tidak boleh kosong!", Toast.LENGTH_LONG).show();
                } else {
                    registrasi(strUsername, strEmail, strnotelp, strPassword);
                }

            }
        });
    }

    public void registrasi(String username, String email, String notelp, String password){

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", username);
        params.put("email", email);
        params.put("notelp", notelp);
        params.put("role", "2");
        params.put("password", password);

        pDialog.setMessage("Mohon Tunggu Sebentar.........");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(BaseURL.register, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");

                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(RegisteActivity.this, LoginActivity.class);
                                startActivity(i);
                                finish();
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