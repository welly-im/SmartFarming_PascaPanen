package com.example.smartfarming_pascapanen.GudangStok.HistoryPenjualanKopi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListDataHistoryPenjualan extends RecyclerView.Adapter<ListDataHistoryPenjualan.Holder> {

    List<DataModelHistoryPenjualan> listData;
    LayoutInflater inflater;

    public ListDataHistoryPenjualan(Context context, List<DataModelHistoryPenjualan> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_data_history_penjualan, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.nama_pembeli.setText(listData.get(position).getNama_pembeli());
        holder.berat.setText(listData.get(position).getBerat());
        holder.harga_jual.setText(listData.get(position).getHarga_jual());
        holder.id_penjualan.setText(listData.get(position).getId_penjualan());
        holder.nama_penjual.setText(listData.get(position).getNama_penjual());
        holder.tanggal_jual.setText(listData.get(position).getTanggal_jual());
        holder.grade.setText(listData.get(position).getGrade());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView nama_pembeli, berat, harga_jual, tanggal_jual, id_penjualan, nama_penjual, grade;
        public Holder(@NonNull android.view.View itemView) {
            super(itemView);
            nama_pembeli = itemView.findViewById(R.id.nama_pembeli);
            berat = itemView.findViewById(R.id.berat_penjualan);
            harga_jual = itemView.findViewById(R.id.harga_penjualan);
            tanggal_jual = itemView.findViewById(R.id.tanggal_penjualan);
            id_penjualan = itemView.findViewById(R.id.id_penjualan);
            nama_penjual = itemView.findViewById(R.id.nama_penjual);
            grade = itemView.findViewById(R.id.gradeKopi);
        }
    }

}
