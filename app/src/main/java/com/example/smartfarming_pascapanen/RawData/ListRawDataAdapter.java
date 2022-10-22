package com.example.smartfarming_pascapanen.RawData;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListRawDataAdapter extends BaseAdapter {

    Activity activity;
    List<ListRawData> items;
    Context context;

    private LayoutInflater inflater;

    public ListRawDataAdapter(Activity activity, List<ListRawData> items, Context context) {
        this.activity = activity;
        this.items = items;
        this.context = context;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public Object getItem(int i) {
        return items.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if(inflater == null)
            inflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (view == null) {
            view = inflater.inflate(R.layout.list_raw_data, null);
        }
        TextView txt_id_panen  = view.findViewById(R.id.id_panen);
        TextView txt_tanggal_panen  = view.findViewById(R.id.tanggal_panen);
        TextView txt_berat = view.findViewById(R.id.berat_panen);
        TextView txt_nama_pengguna  = view.findViewById(R.id.nama_pengguna);

        ListRawData data = items.get(i);

        txt_id_panen.setText(data.getId_panen());
        txt_tanggal_panen.setText(data.getTanggal_panen());
        txt_berat.setText(data.getBerat());
        txt_nama_pengguna.setText(data.getNama_pengguna());
        return view;
    }
}
