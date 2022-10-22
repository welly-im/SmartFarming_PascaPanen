package com.example.smartfarming_pascapanen.RawData.Sorting;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.R;

import java.util.List;


public class ListDataSortingBagusAdapter extends RecyclerView.Adapter<ListDataSortingBagusAdapter.HolderData> {

    List<DataModelSortingBagus> listData;
    LayoutInflater inflater;

    public ListDataSortingBagusAdapter(Context context, List<DataModelSortingBagus> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_data_sorting_bagus, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
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

    public class HolderData extends RecyclerView.ViewHolder {
        TextView id_sorting, tanggal_sorting, berat, nama_pengguna, id_panen, tanggal_panen;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            id_sorting = itemView.findViewById(R.id.info_id_sorting_bagus);
            tanggal_sorting = itemView.findViewById(R.id.info_tanggal_sorting_bagus);
            berat = itemView.findViewById(R.id.info_berat_sorting_bagus);
            nama_pengguna = itemView.findViewById(R.id.info_diinput_oleh_sorting_bagus);
            id_panen = itemView.findViewById(R.id.info_id_panen);
            tanggal_panen = itemView.findViewById(R.id.info_tanggal_panen);
        }
    }
}
