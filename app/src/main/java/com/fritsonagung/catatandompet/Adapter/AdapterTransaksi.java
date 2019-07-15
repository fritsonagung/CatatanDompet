package com.fritsonagung.catatandompet.Adapter;


import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.EditText;
import android.widget.TextView;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;
import com.fritsonagung.catatandompet.R;
import com.fritsonagung.catatandompet.UbahTransaksiActivity;

import java.util.List;

public class AdapterTransaksi extends RecyclerView.Adapter<AdapterTransaksi.MyViewHolder> {

    private Context mContext;
    private List<EntitasTransaksi> listTransaksi;
    private OnTransaksiListener mOnTransaksiListener;
    private DatabaseAplikasi databaseAplikasi;


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

        holder.tanggal.setText(transaksi.getTanggal());
        holder.kategori.setText(transaksi.getKategori());
        holder.keterangan.setText(transaksi.getKeterangan());
        holder.jumlah.setText("Rp." + transaksi.getJumlah());
    }


    @Override
    public int getItemCount() {
        return listTransaksi.size();
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
            Intent intent = new Intent(mContext, UbahTransaksiActivity.class);

            intent.putExtra("tipe", listTransaksi.get(getAdapterPosition()).getTipe());
            intent.putExtra("tanggal",listTransaksi.get(getAdapterPosition()).getTanggal());
            intent.putExtra("kategori",listTransaksi.get(getAdapterPosition()).getKategori());
            intent.putExtra("jumlah", listTransaksi.get(getAdapterPosition()).getJumlah());
            intent.putExtra("keterangan", listTransaksi.get(getAdapterPosition()).getKeterangan());

            mContext.startActivity(intent);
        }
    }

    public interface OnTransaksiListener {
        void onClickTransaksi(int position);
    }
}




