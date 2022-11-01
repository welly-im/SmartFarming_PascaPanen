package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListPengolahanDataSortingBagus extends RecyclerView.Adapter<ListPengolahanDataSortingBagus.Holder> {

    List<DataModelPengolahanSortingBagus> listDataSort;
    LayoutInflater inflater;

    public ListPengolahanDataSortingBagus(Context context, List<DataModelPengolahanSortingBagus> listData) {
        this.listDataSort = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pengolahan_list_data_sorting_bagus, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.id_sorting.setText(listDataSort.get(position).getId_sorting());
        holder.tanggal_sorting.setText(listDataSort.get(position).getTanggal_sorting());
        holder.nama_pengguna.setText(listDataSort.get(position).getNama_pengguna());
        holder.berat.setText(listDataSort.get(position).getBerat());
        holder.id_panen.setText(listDataSort.get(position).getId_panen());
        holder.tanggal_panen.setText(listDataSort.get(position).getTanggal_panen());
    }

    @Override
    public int getItemCount() {
        return listDataSort.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView id_sorting, tanggal_sorting, berat, nama_pengguna, id_panen, tanggal_panen;
        public Holder(@NonNull View itemView) {
            super(itemView);
            id_sorting = itemView.findViewById(R.id.p_info_id_sorting_bagus);
            tanggal_sorting = itemView.findViewById(R.id.p_info_tanggal_sorting_bagus);
            berat = itemView.findViewById(R.id.p_info_berat_sorting_bagus);
            nama_pengguna = itemView.findViewById(R.id.p_info_diinput_oleh_sorting_bagus);
            id_panen = itemView.findViewById(R.id.p_info_id_panen);
            tanggal_panen = itemView.findViewById(R.id.p_info_tanggal_panen);
        }
    }
}
