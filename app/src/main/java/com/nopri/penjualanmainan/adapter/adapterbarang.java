package com.nopri.penjualanmainan.adapter;


import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.model.modelBarang;
import com.nopri.penjualanmainan.server.BaseURL;
import com.squareup.picasso.Picasso;

import java.util.List;

public class adapterbarang extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<modelBarang> item;

    public adapterbarang(Activity activity, List<modelBarang> item) {
        this.activity = activity;
        this.item = item;
    }

    @Override
    public int getCount() {
        return item.size();
    }

    @Override
    public Object getItem(int position) {
        return item.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_barang, null);


        TextView namaBarang    = (TextView) convertView.findViewById(R.id.txtnamabarang);
        TextView kodebarang    = (TextView) convertView.findViewById(R.id.txtkodebarang);
        TextView harga         = (TextView) convertView.findViewById(R.id.txthargabarang);
        ImageView gambarbarang = (ImageView) convertView.findViewById(R.id.gambarbarang);

        namaBarang.setText(item.get(position).getNamabarang());
        kodebarang.setText(item.get(position).getKodebarang());
        harga.setText(item.get(position).getHargabarang());
        Picasso.get().load(BaseURL.baseURL + "gambar/" + item.get(position).getGambar())
                .resize(103, 103)
                .centerCrop()
                .into(gambarbarang);
        return convertView;
    }
}
