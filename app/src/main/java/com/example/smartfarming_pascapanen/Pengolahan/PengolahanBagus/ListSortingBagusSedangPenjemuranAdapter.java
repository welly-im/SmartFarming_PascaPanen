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

public class ListSortingBagusSedangPenjemuranAdapter extends RecyclerView.Adapter<ListSortingBagusSedangPenjemuranAdapter.Holder> {

    List<DataModelSortingBagusSedangPenjemuran> listDataSedangPenjemuran;
    LayoutInflater inflater;

//    private OnItemClickCallback onItemClickCallback;
//
//    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
//        this.onItemClickCallback = onItemClickCallback;
//    }

    public ListSortingBagusSedangPenjemuranAdapter(Context context, List<DataModelSortingBagusSedangPenjemuran> listDataSedangPenjemuran) {
        this.listDataSedangPenjemuran = listDataSedangPenjemuran;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.pengolahan_list_sedang_penjemuran_sorting_bagus, parent, false);
        return new Holder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.id_penjemuran.setText(listDataSedangPenjemuran.get(position).getId_penjemuran());
        holder.tanggal_mulai.setText(listDataSedangPenjemuran.get(position).getTanggal_mulai());
        holder.tanggal_akhir.setText(listDataSedangPenjemuran.get(position).getTanggal_akhir());
        holder.berat_penjemuran.setText(listDataSedangPenjemuran.get(position).getBerat_penjemuran());
    }

    @Override
    public int getItemCount() {
        return listDataSedangPenjemuran.size();
    }

    public class Holder extends RecyclerView.ViewHolder {
        TextView id_penjemuran, tanggal_mulai, tanggal_akhir, berat_penjemuran;
        public Holder(@NonNull View itemView) {
            super(itemView);
            id_penjemuran = itemView.findViewById(R.id.id_penjemuran);
            tanggal_mulai = itemView.findViewById(R.id.list_tanggal_mulai_penjemuran);
            tanggal_akhir = itemView.findViewById(R.id.list_tanggal_selesai_penjemuran);
            berat_penjemuran = itemView.findViewById(R.id.berat_penjemuran);
        }
    }

//    public interface OnItemClickCallback {
//        void onItemClicked(DataModelSortingBagusSedangPenjemuran data);
//    }
}
