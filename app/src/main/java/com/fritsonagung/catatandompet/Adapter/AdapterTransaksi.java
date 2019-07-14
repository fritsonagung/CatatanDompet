package com.fritsonagung.catatandompet.Adapter;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.fritsonagung.catatandompet.Database.EntitasTransaksi;
import com.fritsonagung.catatandompet.R;

import java.util.List;

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.MyViewHolder> {

    private Context mContext;
    private List<EntitasTransaksi> listTransaksi;
    private OnTransaksiListener mOnTransaksiListener;


    public AdapterTransaksi(Context mContext, List<EntitasTransaksi> listTransaksi, OnTransaksiListener onTransaksiListener) {
        this.mContext = mContext;
        this.listTransaksi = listTransaksi;
        this.mOnTransaksiListener = onTransaksiListener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_transaksi, parent, false);

        return new MyViewHolder(itemView, mOnTransaksiListener);
    }


    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final EntitasTransaksi transaksi = listTransaksi.get(position);
        holder.kategori.setText(transaksi.getKategori());
        holder.keterangan.setText(transaksi.getKeterangan());
        holder.jumlah.setText("Rp." + transaksi.getJumlah());
    }


    @Override
    public int getItemCount() {
        return listTransaksi.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView kategori, keterangan, jumlah;
        OnTransaksiListener onTransaksiListener;

        public MyViewHolder(View v, OnTransaksiListener onTransaksiListener) {
            super(v);

            kategori = v.findViewById(R.id.TV_Kategori);
            keterangan = v.findViewById(R.id.TV_Keterangan_Transaksi);
            jumlah = v.findViewById(R.id.TV_Jumlah);
            this.onTransaksiListener = onTransaksiListener;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTransaksiListener.onClickTransaksi(getAdapterPosition());
        }
    }

    public interface OnTransaksiListener{
        void onClickTransaksi(int position);
    }
}




