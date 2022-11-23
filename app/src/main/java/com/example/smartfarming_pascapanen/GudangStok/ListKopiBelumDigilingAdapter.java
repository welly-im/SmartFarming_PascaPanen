package com.example.smartfarming_pascapanen.GudangStok;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek.DataModelSortingJelekSedangPenjemuran;
import com.example.smartfarming_pascapanen.Pengolahan.PengolahanJelek.ListSortingJelekSedangPenjemuran;
import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListKopiBelumDigilingAdapter extends RecyclerView.Adapter<ListKopiBelumDigilingAdapter.Holder> {

    List<DataModelKopiBelumDigiling> listDataKopiBelumDigiling;
    LayoutInflater inflater;

    private OnItemClickCallback onItemClickCallback;

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public ListKopiBelumDigilingAdapter(Context context, List<DataModelKopiBelumDigiling> listData) {
        this.listDataKopiBelumDigiling = listData;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_data_kopi_belum_digiling, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.id_penjemuran.setText(listDataKopiBelumDigiling.get(position).getId_penjemuran());
        holder.berat_akhir.setText(listDataKopiBelumDigiling.get(position).getBerat_akhir());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listDataKopiBelumDigiling.get(holder.getAdapterPosition()));
            }
        });
    }

    @Override
    public int getItemCount() {
        return listDataKopiBelumDigiling.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView id_penjemuran, berat_akhir;
        public Holder(@NonNull View itemView) {
            super(itemView);
            id_penjemuran = itemView.findViewById(R.id.id_kopi_belum_digiling);
            berat_akhir = itemView.findViewById(R.id.berat_kopi_belum_digiling);
        }
    }
    public interface OnItemClickCallback {
        void onItemClicked(DataModelKopiBelumDigiling data);
    }
}
