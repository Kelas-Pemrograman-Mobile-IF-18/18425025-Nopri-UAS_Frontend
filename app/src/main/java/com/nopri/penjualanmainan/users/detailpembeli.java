package com.nopri.penjualanmainan.users;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.server.BaseURL;
import com.nopri.penjualanmainan.session.PrefSetting;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class detailpembeli extends AppCompatActivity {
    EditText kodebrg, namabrg, hargabrg, kategoribrg, deskripsibrng, jumlah;
    Button pesan;

    ImageView imagedetail;
    String nameuser;

    ProgressDialog pDialog;

    private RequestQueue mRequestQueue;

    String strKodebarang, strNamabarang, strHargabarang, strKategori, strDeskripsi,strGambar, _id, strJumlah;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detailpembeli);
        nameuser = PrefSetting.username;
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        mRequestQueue = Volley.newRequestQueue(this);

        kodebrg = (EditText) findViewById(R.id.codebrg);
        namabrg = (EditText) findViewById(R.id.namebrg);
        hargabrg = (EditText) findViewById(R.id.pricebrg);
        kategoribrg = (EditText) findViewById(R.id.category);
        deskripsibrng = (EditText) findViewById(R.id.description);
        jumlah = (EditText) findViewById(R.id.jumlah);
        pesan = (Button) findViewById (R.id.pesanbarang);

        pesan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                strKodebarang = kodebrg.getText().toString();
                strNamabarang = namabrg.getText().toString();
                strHargabarang = hargabrg.getText().toString();
                strKategori = kategoribrg.getText().toString();
                strDeskripsi = deskripsibrng.getText().toString();
                strJumlah = jumlah.getText().toString();

                int total = Integer.parseInt(strHargabarang) * Integer.parseInt(strJumlah);

                order(strNamabarang, strHargabarang, strJumlah, total);
            }
        });
        imagedetail = (ImageView) findViewById(R.id.gambardetail);
        Intent i = getIntent();
        strKodebarang = i.getStringExtra("kodebarang");
        strNamabarang = i.getStringExtra("namabarang");
        strHargabarang= i.getStringExtra("hargabarang");
        strKategori = i.getStringExtra("kategori");
        strDeskripsi = i.getStringExtra("deskripsi");
        strGambar = i.getStringExtra("gambar");
        _id = i.getStringExtra("id");

        kodebrg.setText(strKodebarang);
        namabrg.setText(strNamabarang);
        hargabrg.setText(strHargabarang);
        kategoribrg.setText(strKategori);
        deskripsibrng.setText(strDeskripsi);
        Picasso.get().load(BaseURL.baseURL + "gambar/" + strGambar)
                .into(imagedetail);
    }
    public void order(String namabarang, String harga, String jumlah, int total){
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("username", nameuser);
        params.put("namabarang", namabarang);
        params.put("harga", harga);
        params.put("jumlah", jumlah);
        params.put("status", "Sedang Diproses");
        params.put("total", String.valueOf(total));

        pDialog.setMessage("Mohon Tunggu.....");
        showDialog();
        Log.e("param", String.valueOf(params));

        JsonObjectRequest req = new JsonObjectRequest(BaseURL.order, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(detailpembeli.this, dashboard_user.class);
                                startActivity(i);
                                finish();
                            }else {
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