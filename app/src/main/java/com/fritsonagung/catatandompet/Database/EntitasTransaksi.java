package com.fritsonagung.catatandompet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.arch.persistence.room.TypeConverters;

import java.util.Date;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 7 Juli 2019
 **/

@Entity(tableName = "transaksi")
public class EntitasTransaksi {

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id_transaksi;

    @ColumnInfo(name = "tipe")
    private String tipe;

    @ColumnInfo(name = "tanggal")
    private String tanggal;

    @ColumnInfo(name = "kategori")
    private String kategori;

    @ColumnInfo(name = "jumlah")
    private int jumlah;

    @ColumnInfo(name = "keterangan")
    private String keterangan;

    public int getId_transaksi() {
        return id_transaksi;
    }

    public void setId_transaksi(int id_transaksi) {
        this.id_transaksi = id_transaksi;
    }

    public String getTipe() {
        return tipe;
    }

    public void setTipe(String tipe) {
        this.tipe = tipe;
    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public int getJumlah() {
        return jumlah;
    }

    public void setJumlah(int jumlah) {
        this.jumlah = jumlah;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }
}
