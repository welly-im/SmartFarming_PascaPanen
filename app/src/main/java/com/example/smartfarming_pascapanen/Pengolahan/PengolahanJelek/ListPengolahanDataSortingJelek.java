package com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListPengolahanDataSortingJelek extends RecyclerView.Adapter<ListPengolahanDataSortingJelek.Holder> {

    List<DataModelPengolahanSortingJelek> listData;
    Context context;

    public ListPengolahanDataSortingJelek(List<DataModelPengolahanSortingJelek> listData, Context context) {
        this.listData = listData;
        this.context = context;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View layout = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_data_sorting_jelek, parent, false);
        Holder holder = new Holder(layout);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.id_sorting.setText(listData.get(position).getId_sorting());
        holder.tanggal_sorting.setText(listData.get(position).getTanggal_sorting());
        holder.nama_pengguna.setText(listData.get(position).getNama_pengguna());
        holder.berat.setText(listData.get(position).getBerat());
        holder.id_panen.setText(listData.get(position).getId_panen());
        holder.tanggal_panen.setText(listData.get(position).getTanggal_panen());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    class Holder extends RecyclerView.ViewHolder {
        TextView id_sorting, tanggal_sorting, berat, id_panen, tanggal_panen, nama_pengguna;
        public Holder(@NonNull View itemView) {
            super(itemView);
            id_sorting = itemView.findViewById(R.id.info_id_sorting_jelek);
            tanggal_sorting = itemView.findViewById(R.id.info_tanggal_sorting_jelek);
            berat = itemView.findViewById(R.id.info_berat_sorting_jelek);
            id_panen = itemView.findViewById(R.id.info_id_panen_jelek);
            tanggal_panen = itemView.findViewById(R.id.info_tanggal_panen_jelek);
            nama_pengguna = itemView.findViewById(R.id.info_diinput_oleh_sorting_jelek);
        }
    }
}
