package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah.ListMetodeBasahDataBulanAdapter;
import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListPengolahanBagusKonfirmasiDataCuacaAdapter extends RecyclerView.Adapter<ListPengolahanBagusKonfirmasiDataCuacaAdapter.Holder>{
    List<DataModelPengolahanBagusKonfirmasiDataCuacaAdapter> listDataCuaca;
    LayoutInflater inflater;

    public ListPengolahanBagusKonfirmasiDataCuacaAdapter(Context context, List<DataModelPengolahanBagusKonfirmasiDataCuacaAdapter> listDataCuaca) {
        this.listDataCuaca = listDataCuaca;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_info_cuaca, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.hari1.setText(listDataCuaca.get(position).getHari1());
        holder.cuaca1.setText(listDataCuaca.get(position).getCuaca1());
        holder.suhu1.setText(listDataCuaca.get(position).getSuhu1());
        holder.hari2.setText(listDataCuaca.get(position).getHari2());
        holder.cuaca2.setText(listDataCuaca.get(position).getCuaca2());
        holder.suhu2.setText(listDataCuaca.get(position).getSuhu2());
        holder.hari3.setText(listDataCuaca.get(position).getHari3());
        holder.cuaca3.setText(listDataCuaca.get(position).getCuaca3());
        holder.suhu3.setText(listDataCuaca.get(position).getSuhu3());
        holder.hari4.setText(listDataCuaca.get(position).getHari4());
        holder.cuaca4.setText(listDataCuaca.get(position).getCuaca4());
        holder.suhu4.setText(listDataCuaca.get(position).getSuhu4());
        holder.hari5.setText(listDataCuaca.get(position).getHari5());
        holder.cuaca5.setText(listDataCuaca.get(position).getCuaca5());
        holder.suhu5.setText(listDataCuaca.get(position).getSuhu5());
        holder.hari6.setText(listDataCuaca.get(position).getHari6());
        holder.cuaca6.setText(listDataCuaca.get(position).getCuaca6());
        holder.suhu6.setText(listDataCuaca.get(position).getSuhu6());
    }

    @Override
    public int getItemCount() {
        return listDataCuaca.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView hari1, cuaca1, suhu1, hari2, cuaca2, suhu2, hari3, cuaca3, suhu3, hari4, cuaca4, suhu4, hari5, cuaca5, suhu5, hari6, cuaca6, suhu6;
        public Holder(@NonNull View itemView) {
            super(itemView);
            hari1 = itemView.findViewById(R.id.tanggal_cuaca1);
            cuaca1 = itemView.findViewById(R.id.cuaca1);
            suhu1 = itemView.findViewById(R.id.derajat1);
            hari2 = itemView.findViewById(R.id.tanggal_cuaca2);
            cuaca2 = itemView.findViewById(R.id.cuaca2);
            suhu2 = itemView.findViewById(R.id.derajat2);
            hari3 = itemView.findViewById(R.id.tanggal_cuaca3);
            cuaca3 = itemView.findViewById(R.id.cuaca3);
            suhu3 = itemView.findViewById(R.id.derajat3);
            hari4 = itemView.findViewById(R.id.tanggal_cuaca4);
            cuaca4 = itemView.findViewById(R.id.cuaca4);
            suhu4 = itemView.findViewById(R.id.derajat4);
            hari5 = itemView.findViewById(R.id.tanggal_cuaca5);
            cuaca5 = itemView.findViewById(R.id.cuaca5);
            suhu5 = itemView.findViewById(R.id.derajat5);
            hari6 = itemView.findViewById(R.id.tanggal_cuaca6);
            cuaca6 = itemView.findViewById(R.id.cuaca6);
            suhu6 = itemView.findViewById(R.id.derajat6);
        }
    }
}
