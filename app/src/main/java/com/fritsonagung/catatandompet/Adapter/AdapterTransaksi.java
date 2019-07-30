package com.fritsonagung.catatandompet.Adapter;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.TextView;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;
import com.fritsonagung.catatandompet.R;
import com.fritsonagung.catatandompet.View.UbahTransaksiActivity;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Developed By:
 * Nama : Fritson Agung Julians Ayomi
 * NIM  : 10116076
 * Kelas: AKB-2
 * Tanggal Pengerjaan : 5 Juli 2019
 **/

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.MyViewHolder> {

    private Context mContext;
    private List<EntitasTransaksi> listTransaksi;
    private OnTransaksiListener mOnTransaksiListener;
    private static DatabaseAplikasi databaseAplikasi;


    public AdapterTransaksi(Context mContext, List<EntitasTransaksi> listTransaksi, OnTransaksiListener onTransaksiListener) {
        this.mContext = mContext;
        this.listTransaksi = listTransaksi;
        this.mOnTransaksiListener = onTransaksiListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_view_transaksi, parent, false);

        return new MyViewHolder(itemView, mOnTransaksiListener);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, final int position) {
        final EntitasTransaksi transaksi = listTransaksi.get(position);

        String jumlah;
        if (listTransaksi.get(position).getTipe().equals("Pemasukan")) {
            jumlah = "+ Rp. " + listTransaksi.get(position).getJumlah();
            holder.jumlah.setText(jumlah);
            holder.jumlah.setTextColor(Color.parseColor("#33C4B3"));
        } else {
            jumlah = "- Rp. " + listTransaksi.get(position).getJumlah();
            holder.jumlah.setText(jumlah);
            holder.jumlah.setTextColor(Color.parseColor("#F68E4F"));
        }

        SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        String dateToBeSet = sdf.format(listTransaksi.get(position).getTanggal());
        holder.tanggal.setText(dateToBeSet);
        holder.kategori.setText(transaksi.getKategori());
        holder.keterangan.setText(transaksi.getKeterangan());
    }


    @Override
    public int getItemCount() {
        if (listTransaksi == null || listTransaksi.size() == 0) {
            return 0;
        } else {
            return listTransaksi.size();
        }
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView kategori, keterangan, jumlah, tanggal;
        OnTransaksiListener onTransaksiListener;

        public MyViewHolder(View v, OnTransaksiListener onTransaksiListener) {
            super(v);

            tanggal = v.findViewById(R.id.TV_Tanggal_Transaksi);
            kategori = v.findViewById(R.id.TV_Kategori);
            keterangan = v.findViewById(R.id.TV_Keterangan_Transaksi);
            jumlah = v.findViewById(R.id.TV_Jumlah);
            this.onTransaksiListener = onTransaksiListener;


            databaseAplikasi = DatabaseAplikasi.getDatabase(mContext);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            onTransaksiListener.onClickTransaksi(getAdapterPosition());

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
            String date = sdf.format(listTransaksi.get(getAdapterPosition()).getTanggal());

            Intent intent = new Intent(mContext, UbahTransaksiActivity.class);
            intent.putExtra("id", listTransaksi.get(getAdapterPosition()).getId_transaksi());
            intent.putExtra("tipe", listTransaksi.get(getAdapterPosition()).getTipe());
            intent.putExtra("tanggal", date);
            intent.putExtra("kategori", listTransaksi.get(getAdapterPosition()).getKategori());
            intent.putExtra("jumlah", listTransaksi.get(getAdapterPosition()).getJumlah());
            intent.putExtra("keterangan", listTransaksi.get(getAdapterPosition()).getKeterangan());

            mContext.startActivity(intent);
        }
    }

    public interface OnTransaksiListener {
        void onClickTransaksi(int position);
    }
}




