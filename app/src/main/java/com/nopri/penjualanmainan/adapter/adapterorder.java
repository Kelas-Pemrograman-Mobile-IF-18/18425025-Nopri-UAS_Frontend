package com.nopri.penjualanmainan.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nopri.penjualanmainan.R;
import com.nopri.penjualanmainan.model.modelOrder;

import java.util.List;

public class adapterorder extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<modelOrder> item;

    public adapterorder(Activity activity, List<modelOrder> item) {
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
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null)
            convertView = inflater.inflate(R.layout.content_order, null);


        TextView username = (TextView) convertView.findViewById(R.id.namauser);
        TextView namabarang     = (TextView) convertView.findViewById(R.id.namabrg);
        TextView harga         = (TextView) convertView.findViewById(R.id.harga);
        TextView jumlah         = (TextView) convertView.findViewById(R.id.jumlahh);
        TextView total    = (TextView) convertView.findViewById(R.id.total);
        TextView status        = (TextView) convertView.findViewById(R.id.statuss);

        username.setText(item.get(position).getUsername());
        namabarang.setText(item.get(position).getNamabarang());
        harga.setText("Rp." + item.get(position).getHarga());
        jumlah.setText(item.get(position).getJumlah());
        total.setText(item.get(position).getTotal());
        status.setText(item.get(position).getStatus());
        if (item.get(position).getStatus().equals("Sedang Diroses")) {
            status.setBackgroundResource(R.drawable.button_yellow);
        }else if (item.get(position).getStatus().equals("Pesanan Diterima")) {
            status.setBackgroundResource(R.drawable.button_green);
        } else {
            status.setBackgroundResource(R.drawable.button_blue);
        }
        return convertView;
    }

}
