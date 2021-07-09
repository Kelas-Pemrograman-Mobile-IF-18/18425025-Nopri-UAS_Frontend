package com.nopri.penjualanmainan.admin;

import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.DexterError;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.PermissionRequestErrorListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.server.BaseURL;
import com.nopri.penjualanmainan.server.VolleyMultipart;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EditdanDeleteAc extends AppCompatActivity {
    TextView txtkode, txtnama, txtharga, txtdeskripsi, txtkategori;
    Button edit, ambil, hapus, upload;
    ImageView imggambarBarang;


    String strkodebarang, strnamabarang, strhargabarang, strkategori, strdeskripsi, strgambar, _id;

    private RequestQueue mRequestQueue;

    Bitmap bitmap;

    private final int CameraR_PP = 1;
    String mCurrentPhotoPath;

    ProgressDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editdan_delete);

        if (Build.VERSION.SDK_INT >= 24) {
            try {
                Method m = StrictMode.class.getMethod("disableDeathOnFileUriExposure");
                m.invoke(null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        mRequestQueue = Volley.newRequestQueue(this);
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        txtkode = (TextView) findViewById(R.id.kodebarang);
        txtnama = (TextView) findViewById(R.id.namabarang);
        txtharga = (TextView) findViewById(R.id.hargabarang);
        txtdeskripsi = (TextView) findViewById(R.id.deskripsi);
        txtkategori = (TextView) findViewById(R.id.kategori);
        edit = (Button) findViewById(R.id.edit);
        ambil = (Button) findViewById(R.id.ambil);
        hapus = (Button) findViewById(R.id.simpandata);
        upload = (Button) findViewById(R.id.upload);
        imggambarBarang = (ImageView) findViewById(R.id.gambarinput);

        Intent i = getIntent();
        strkodebarang = i.getStringExtra("kodebarang");
        strnamabarang = i.getStringExtra("namabarang");
        strhargabarang = i.getStringExtra("hargabarang");
        strkategori = i.getStringExtra("kategori");
        strdeskripsi = i.getStringExtra("deskripsi");
        strgambar = i.getStringExtra("gambar");
        _id = i.getStringExtra("_id");

        Log.e("kat", String.valueOf(strkategori));
        Log.e("desk",  String.valueOf(strdeskripsi));

        txtkode.setText(strkodebarang);
        txtnama.setText(strnamabarang);
        txtharga.setText(strhargabarang);
        txtkategori.setText(strkategori);
        txtdeskripsi.setText(strdeskripsi);
        Picasso.get().load(BaseURL.baseURL + "gambar/" + strgambar)
                .into(imggambarBarang);

        ambil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeImage();
            }
        });
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDataTanpaGambar();
            }
        });

        upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDataDenganGambar(bitmap);
            }
        });

        hapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(EditdanDeleteAc.this);

                builder.setTitle("Konfirmasi");
                builder.setMessage("Yakin ingin menghapus Menu " + strnamabarang + " ? ");

                builder.setPositiveButton("Hapus", new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        // Do nothing but close the dialog
                        hapusData();
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
        });

    }

    public void takeImage(){
        addPermission();
        Log.e("take", "Okeee");
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (cameraIntent.resolveActivity(EditdanDeleteAc.this.getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.i("Tags", "IOException");
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(photoFile));
                startActivityForResult(cameraIntent, CameraR_PP);
            }
        }
    }


    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  // prefix
                ".jpg",         // suffix
                storageDir      // directory
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == EditdanDeleteAc.RESULT_CANCELED) {
            return;
        }
        if (requestCode == CameraR_PP) {
            try {

                bitmap = MediaStore.Images.Media.getBitmap(EditdanDeleteAc.this.getContentResolver(), Uri.parse(mCurrentPhotoPath));
                imggambarBarang.setImageBitmap(bitmap);
//                uploadPotoProfile(bitmap);
                if (imggambarBarang.getDrawable() != null) {
                    int newHeight = 300; // New height in pixels
                    int newWidth = 300;
                    imggambarBarang.requestLayout();
                    imggambarBarang.getLayoutParams().height = newHeight;
                    // Apply the new width for ImageView programmatically
                    imggambarBarang.getLayoutParams().width = newWidth;
                    // Set the scale type for ImageView image scaling
                    imggambarBarang.setScaleType(ImageView.ScaleType.FIT_XY);
                    ViewGroup.MarginLayoutParams marginParams = (ViewGroup.MarginLayoutParams) imggambarBarang.getLayoutParams();
                    marginParams.setMargins(0, 10, 0, 0);
                    upload.setVisibility(View.VISIBLE);
                }
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(EditdanDeleteAc.this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void addPermission() {
        Dexter.withActivity(EditdanDeleteAc.this)
                .withPermissions(

                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE)
                .withListener(new MultiplePermissionsListener() {
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        // check if all permissions are granted
                        if (report.areAllPermissionsGranted()) {
                            Toast.makeText(getApplication(), "All permissions are granted by user!", Toast.LENGTH_SHORT).show();
                        }

                        // check for permanent denial of any permission
                        if (report.isAnyPermissionPermanentlyDenied()) {
                            // show alert dialog navigating to Settings

                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {

                    }


                }).
                withErrorListener(new PermissionRequestErrorListener() {
                    @Override
                    public void onError(DexterError error) {
                        Toast.makeText(EditdanDeleteAc.this, "Some Error! ", Toast.LENGTH_SHORT).show();
                    }
                })
                .onSameThread()
                .check();
    }

    public byte[] getFileDataFromDrawable(Bitmap bitmap) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if (bitmap != null){
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, byteArrayOutputStream);
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void editDataDenganGambar(final Bitmap bitmap) {

        pDialog.setMessage("Mohon Tunggu Sedang Mengupload Gambar !!!!!");
        showDialog();
        VolleyMultipart volleyMultipartRequest = new VolleyMultipart(Request.Method.PUT, BaseURL.Editdatabarang + _id,
                new Response.Listener<NetworkResponse>() {
                    @Override
                    public void onResponse(NetworkResponse response) {
                        mRequestQueue.getCache().clear();
                        hideDialog();
                        try {
                            JSONObject jsonObject = new JSONObject(new String(response.data));
                            System.out.println("ress = " + jsonObject.toString());
                            String strMsg = jsonObject.getString("msg");
                            boolean status= jsonObject.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(EditdanDeleteAc.this, ActivityDataBarang.class);
                                startActivity(i);
                                finish();
                            }else {
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        hideDialog();
                        Toast.makeText(EditdanDeleteAc.this, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }) {

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("kodebarang", txtkode.getText().toString());
                params.put("namabarang", txtnama.getText().toString());
                params.put("hargabarang", txtharga.getText().toString());
                params.put("kategori", txtkategori.getText().toString());
                params.put("deskripsi", txtdeskripsi.getText().toString());
                return params;
            }
            @Override
            protected Map<String, DataPart> getByteData() {
                Map<String, DataPart> params = new HashMap<>();
                long imagename = System.currentTimeMillis();
                params.put("gambar", new VolleyMultipart.DataPart(imagename + ".jpg", getFileDataFromDrawable(bitmap)));
                return params;
            }
        };

        volleyMultipartRequest.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        mRequestQueue = Volley.newRequestQueue(EditdanDeleteAc.this);
        mRequestQueue.add(volleyMultipartRequest);
    }

    public void editDataTanpaGambar(){

        // Post params to be sent to the server
        HashMap<String, String> params = new HashMap<String, String>();
        params.put("kodebarang", txtkode.getText().toString());
        params.put("namabarang", txtnama.getText().toString());
        params.put("hargabarang", txtharga.getText().toString());
        params.put("kategori", txtkategori.getText().toString());
        params.put("deskripsi", txtdeskripsi.getText().toString());
        params.put("gambar", strgambar);

        pDialog.setMessage("Mohon Tunggu.....");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.PUT,BaseURL.Editdatabarang + _id, new JSONObject(params),
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(EditdanDeleteAc.this, ActivityDataBarang.class);
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

    public void hapusData(){

        pDialog.setMessage("Mohon Tunggu.....");
        showDialog();

        JsonObjectRequest req = new JsonObjectRequest(Request.Method.DELETE,BaseURL.hapusdatabarang + _id, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        hideDialog();
                        try {
                            String strMsg = response.getString("msg");
                            boolean status= response.getBoolean("error");
                            if(status == false){
                                Toast.makeText(getApplicationContext(), strMsg, Toast.LENGTH_LONG).show();
                                Intent i = new Intent(EditdanDeleteAc.this, ActivityDataBarang.class);
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