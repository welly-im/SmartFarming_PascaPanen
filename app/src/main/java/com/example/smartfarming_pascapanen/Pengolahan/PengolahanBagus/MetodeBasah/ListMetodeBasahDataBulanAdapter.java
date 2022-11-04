package com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.MetodeBasah;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartfarming_pascapanen.Pengolahan.PengolahanBagus.ListSortingBagusSedangPenjemuranAdapter;
import com.example.smartfarming_pascapanen.R;

import java.util.List;

public class ListMetodeBasahDataBulanAdapter  extends RecyclerView.Adapter<ListMetodeBasahDataBulanAdapter.Holder>{

    List<DataModelMetodeBasahDataBulan> listDataBulan;
    LayoutInflater inflater;

    public ListMetodeBasahDataBulanAdapter(Context context, List<DataModelMetodeBasahDataBulan> listDataBulan) {
        this.listDataBulan = listDataBulan;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public ListMetodeBasahDataBulanAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.list_data_bulan, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListMetodeBasahDataBulanAdapter.Holder holder, int position) {
        holder.nama_bulan.setText(listDataBulan.get(position).getNama_bulan());
    }

    @Override
    public int getItemCount() {
        return listDataBulan.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView nama_bulan;
        public Holder(@NonNull View itemView) {
            super(itemView);
            nama_bulan = itemView.findViewById(R.id.nama_bulan);
        }
    }
}
