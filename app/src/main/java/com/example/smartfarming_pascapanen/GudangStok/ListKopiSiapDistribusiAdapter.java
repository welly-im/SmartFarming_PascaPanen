package com.example.smartfarming_pascapanen.GudangStok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListKopiSiapDistribusiAdapter extends RecyclerView.Adapter<ListKopiSiapDistribusiAdapter.ViewHolder> {

    List<DataModelKopiSiapDistribusi> listData;
    LayoutInflater inflater;

    public ListKopiSiapDistribusiAdapter(Context context, List<DataModelKopiSiapDistribusi> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_data_kopi_siap_distribusi, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.id_stok.setText(listData.get(position).getId_stok());
        holder.berat.setText(listData.get(position).getBerat_kopi());
        holder.harga.setText(listData.get(position).getHarga_jual());
        holder.grade.setText(listData.get(position).getGrade());
    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView id_stok, berat, harga, grade;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            id_stok = itemView.findViewById(R.id.id_stok);
            berat = itemView.findViewById(R.id.berat_stok);
            harga = itemView.findViewById(R.id.harga_stok);
            grade = itemView.findViewById(R.id.grade);
        }
    }
}
