package com.example.smartfarming_pascapanen.GudangStok.JualKopi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.GudangStok.DataModelKopiBelumDigiling;
import com.example.smartfarming_pascapanen.GudangStok.ListKopiBelumDigilingAdapter;
import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListDataGudangStokPenjualanKopi extends RecyclerView.Adapter<ListDataGudangStokPenjualanKopi.Holder> {

    List<DataModelGudangStokPenjualanKopi> listDataGudangStokPenjualanKopi;
    LayoutInflater inflater;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListDataGudangStokPenjualanKopi(Context context, List<DataModelGudangStokPenjualanKopi> listDataGudangStokPenjualanKopi) {
        this.listDataGudangStokPenjualanKopi = listDataGudangStokPenjualanKopi;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_data_kopi_dijual, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.id_stok.setText(listDataGudangStokPenjualanKopi.get(position).getId_stok());
        holder.berat.setText(listDataGudangStokPenjualanKopi.get(position).getBerat());
        holder.harga_jual.setText(listDataGudangStokPenjualanKopi.get(position).getHarga_jual());
        holder.grade.setText(listDataGudangStokPenjualanKopi.get(position).getGrade());
        holder.harga_jual_per_kg.setText(listDataGudangStokPenjualanKopi.get(position).getHarga_jual_per_kg());
        holder.keterangan.setText(listDataGudangStokPenjualanKopi.get(position).getKeterangan());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listDataGudangStokPenjualanKopi.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDataGudangStokPenjualanKopi.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView id_stok, grade, berat, harga_jual,harga_jual_per_kg, keterangan;
        public Holder(@NonNull android.view.View itemView) {
            super(itemView);
            id_stok = itemView.findViewById(R.id.id_stok);
            grade = itemView.findViewById(R.id.grade);
            berat = itemView.findViewById(R.id.berat_stok);
            harga_jual = itemView.findViewById(R.id.harga_stok);
            harga_jual_per_kg = itemView.findViewById(R.id.harga_stok_per_kg);
            keterangan = itemView.findViewById(R.id.ket_stok_kopi);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(DataModelGudangStokPenjualanKopi data);
    }
}
