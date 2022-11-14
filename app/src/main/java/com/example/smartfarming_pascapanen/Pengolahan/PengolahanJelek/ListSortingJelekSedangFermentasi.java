package com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.DataModelSortingBagusSedangFermentasi;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.ListSortingBagusSedangFermentasiAdapter;
import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListSortingJelekSedangFermentasi extends RecyclerView.Adapter<ListSortingJelekSedangFermentasi.Holder>{

    List<DataModelSortingJelekSedangFermentasi> listData;
    LayoutInflater inflater;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListSortingJelekSedangFermentasi(Context context, List<DataModelSortingJelekSedangFermentasi> listData) {
        this.listData = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pengolahan_list_sedang_fermentasi_sorting_bagus, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.id_fermentasi.setText(listData.get(position).getId_fermentasi());
        holder.tanggal_mulai.setText(listData.get(position).getTanggal_mulai());
        holder.tanggal_akhir.setText(listData.get(position).getTanggal_akhir());
        holder.berat_fermentasi.setText(listData.get(position).getBerat_fermentasi());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listData.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listData.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView id_fermentasi, tanggal_mulai, tanggal_akhir, berat_fermentasi;
        public Holder(@NonNull View itemView) {
            super(itemView);
            id_fermentasi = itemView.findViewById(R.id.id_fermentasi);
            tanggal_mulai = itemView.findViewById(R.id.list_tanggal_mulai_fermentasi);
            tanggal_akhir = itemView.findViewById(R.id.list_tanggal_selesai_fermentasi);
            berat_fermentasi = itemView.findViewById(R.id.berat_fermentasi);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(DataModelSortingJelekSedangFermentasi data);
    }
}
