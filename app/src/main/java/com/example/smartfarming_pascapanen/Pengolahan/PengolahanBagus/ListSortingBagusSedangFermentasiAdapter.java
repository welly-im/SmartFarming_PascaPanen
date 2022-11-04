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

public class ListSortingBagusSedangFermentasiAdapter extends RecyclerView.Adapter<ListSortingBagusSedangFermentasiAdapter.HolderData> {
    List<DataModelSortingBagusSedangFermentasi> listDataSedangFermentasi;
    LayoutInflater inflater;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListSortingBagusSedangFermentasiAdapter(Context context, List<DataModelSortingBagusSedangFermentasi> listDataSedangFermentasi) {
        this.listDataSedangFermentasi = listDataSedangFermentasi;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public HolderData onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pengolahan_list_sedang_fermentasi_sorting_bagus, parent, false);
        return new HolderData(view);
    }

    @Override
    public void onBindViewHolder(@NonNull HolderData holder, int position) {
        holder.id_fermentasi.setText(listDataSedangFermentasi.get(position).getId_fermentasi());
        holder.tanggal_mulai.setText(listDataSedangFermentasi.get(position).getTanggal_mulai());
        holder.tanggal_akhir.setText(listDataSedangFermentasi.get(position).getTanggal_akhir());
        holder.berat_fermentasi.setText(listDataSedangFermentasi.get(position).getBerat_fermentasi());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listDataSedangFermentasi.get(holder.getAdapterPosition()));
            }
        });

    }

    @Override
    public int getItemCount() {
        return listDataSedangFermentasi.size();
    }

    public class HolderData extends RecyclerView.ViewHolder {
        TextView id_fermentasi, tanggal_mulai, tanggal_akhir, berat_fermentasi;
        public HolderData(@NonNull View itemView) {
            super(itemView);
            id_fermentasi = itemView.findViewById(R.id.id_fermentasi);
            tanggal_mulai = itemView.findViewById(R.id.list_tanggal_mulai_fermentasi);
            tanggal_akhir = itemView.findViewById(R.id.list_tanggal_selesai_fermentasi);
            berat_fermentasi = itemView.findViewById(R.id.berat_fermentasi);
        }
    }

    public interface OnItemClickCallback {
        void onItemClicked(DataModelSortingBagusSedangFermentasi data);
    }
}
