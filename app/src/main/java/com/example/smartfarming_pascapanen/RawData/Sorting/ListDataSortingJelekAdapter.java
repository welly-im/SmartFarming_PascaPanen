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

public class ListDataSortingJelekAdapter extends RecyclerView.Adapter<ListDataSortingJelekAdapter.HolderData> {

    List<DataModelSortingJelek> listData2;
    LayoutInflater inflater2;

    public ListDataSortingJelekAdapter(Context context, List<DataModelSortingJelek> listData2) {
        this.listData2 = listData2;
        this.inflater2 = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater2.inflate(R.layout.list_data_sorting_jelek, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.id_sorting.setText(listData2.get(position).getId_sorting());
        holder.tanggal_sorting.setText(listData2.get(position).getTanggal_sorting());
        holder.nama_pengguna.setText(listData2.get(position).getNama_pengguna());
        holder.berat.setText(listData2.get(position).getBerat());
        holder.id_panen.setText(listData2.get(position).getId_panen());
        holder.tanggal_panen.setText(listData2.get(position).getTanggal_panen());
    }

    @Override
    public int getItemCount() {
        return listData2.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView id_sorting, tanggal_sorting, berat, nama_pengguna, id_panen, tanggal_panen;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            id_sorting = itemView.findViewById(R.id.info_id_sorting_jelek);
            tanggal_sorting = itemView.findViewById(R.id.info_tanggal_sorting_jelek);
            berat = itemView.findViewById(R.id.info_berat_sorting_jelek);
            nama_pengguna = itemView.findViewById(R.id.info_diinput_oleh_sorting_jelek);
            id_panen = itemView.findViewById(R.id.info_id_panen_jelek);
            tanggal_panen = itemView.findViewById(R.id.info_tanggal_panen_jelek);
        }
    }
}
