package com.nopri.penjualanmainan.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.adapter.adapterorder;
import com.nopri.penjualanmainan.model.modelOrder;
import com.nopri.penjualanmainan.server.BaseURL;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class transaksi_user extends AppCompatActivity {
    ProgressDialog pDialog;

    adapterorder adapter;
    ListView list;

    ArrayList<modelOrder> newsList = new ArrayList<modelOrder>();
    private RequestQueue mRequestQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaksi_user);

        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        list = (ListView) findViewById(R.id.array_list_order_user);
        newsList.clear();
        adapter = new adapterorder(transaksi_user.this, newsList);
        list.setAdapter(adapter);
        getAllOrder();
    }
    @Override
    public void onBackPressed() {
        Intent i = new Intent(transaksi_user.this, dashboard_admin.class);
        startActivity(i);
        finish();
    }
    private void getAllOrder() {
        newsList.clear();
        pDialog.setMessage("Loading");
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.GET, BaseURL.getData, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            boolean status = response.getBoolean("error");
                            if (status == false) {
                                Log.e("data pesanan = ", response.getString("data"));
                                String data = response.getString("data");
                                JSONArray jsonArray = new JSONArray(data);
                                for (int i = 0; i < jsonArray.length(); i++) {
                                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                                    final modelOrder order = new modelOrder();
                                    final String _id = jsonObject.getString("_id");
                                    final String namabarang = jsonObject.getString("namabarang");
                                    final String hargabarang = jsonObject.getString("harga");
                                    final String jumlahbarang = jsonObject.getString("jumlah");
                                    final String Status = jsonObject.getString("status");
                                    final String Total = jsonObject.getString("total");
                                    final String User = jsonObject.getString("dataUser");
                                    JSONArray DataUSer = new JSONArray(User);
                                    Log.e("dataUser1", String.valueOf(DataUSer));
                                    JSONObject FinalUser = DataUSer.getJSONObject(0);
                                    Log.e("dataUser", String.valueOf(FinalUser));
                                    final String namaPembeli = FinalUser.getString("username");
                                    Log.e("NamaPembeli", namaPembeli);
                                    order.set_id(_id);
                                    order.setUsername(namaPembeli);
                                    order.setNamabarang(namabarang);
                                    order.setHarga(hargabarang);
                                    order.setJumlah(jumlahbarang);
                                    order.setStatus(Status);
                                    order.setTotal(Total);
//                                    order.set_Id(username);

                                    list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                                            if (newsList.get(position).getStatus().equals("Sedang Diproses")) {

                                                AlertDialog.Builder builder = new AlertDialog.Builder(transaksi_user.this);

                                                builder.setTitle("Konfirmasi");
                                                builder.setMessage("Yakin ingin konfirmasi transaksi ini ? ");

                                                builder.setPositiveButton("Iya", new DialogInterface.OnClickListener() {

                                                    public void onClick(DialogInterface dialog, int which) {
                                                        // Do nothing but close the dialog
//                                                    hapusData();
                                                        updateData(newsList.get(position).get_id());
                                                        Toast.makeText(getApplicationContext(), "ayooooo", Toast.LENGTH_LONG).show();
                                                    }
                                                });
                                                builder.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {

                                                    @Override
                                                    public void onClick(DialogInterface dialog, int which) {

                                                        // Do nothing
                                                        dialog.dismiss();
                                                    }
                                                });

                                                AlertDialog alert = builder.create();
                                                alert.show();
                                            }


                                            // TODO Auto-generated method stub
//                                            Intent a = new Intent(HistoryTransaksiPembeli.this, EditBukuDanHapusActivity.class);
//                                            a.putExtra("namaBarang", newsList.get(position).getNamaBarang());
//                                            a.putExtra("_id", newsList.get(position).get_id());
//                                            a.putExtra("hargaBarang", newsList.get(position).getHargaBarang());
//                                            a.putExtra("stok", newsList.get(position).getStok());
//                                            a.putExtra("kategori", newsList.get(position).getKategori());
//                                            a.putExtra("image", newsList.get(position).getImage());
//                                            startActivity(a);
                                        }
                                    });
                                    newsList.add(order);
                                }
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
                hideDialog();
            }
        });

        /* Add your Requests to the RequestQueue to execute */
        mRequestQueue.add(req);
    }
    public void updateData(String _id) {
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("status", "Proses Pengiriman");
        pDialog.setMessage("Mohon Tunggu....");
        Log.e("ID", _id);
        showDialog();
        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT, BaseURL.EditTransaksi + _id, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            hideDialog();
                            String strMsg = response.getString("msg");
                            boolean status = response.getBoolean("error");
                            if (status) {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                getAllOrder();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.e("Error: ", error.getMessage());
            }
        });


// add the request object to the queue to be executed
        mRequestQueue.add(req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }
}