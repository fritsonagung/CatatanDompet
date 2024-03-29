package com.fritsonagung.catatandompet.Database;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 5 Juli 2019
 **/

@Entity(tableName = "transaksi")
public class EntitasTransaksi {

    @PrimaryKey(autoGenerate = true)
    private int id_transaksi;

    @ColumnInfo(name = "tipe")
    private String tipe;

    @ColumnInfo(name = "tanggal")
    private Date tanggal;

    @ColumnInfo(name = "kategori")
    private String kategori;

    @ColumnInfo(name = "jumlah")
    private int jumlah;

    @ColumnInfo(name = "keterangan")
    private String keterangan;


    public EntitasTransaksi(String tipe, Date tanggal, String kategori, int jumlah, String keterangan) {
        this.tipe = tipe;
        this.tanggal = tanggal;
        this.kategori = kategori;
        this.jumlah = jumlah;
        this.keterangan = keterangan;
    }

    @Ignore
    public EntitasTransaksi(int idTransaksi) {
        this.id_transaksi = idTransaksi;
    }

    @Ignore
    public EntitasTransaksi(String tipe, int jumlah) {
        this.tipe = tipe;
        this.jumlah = jumlah;
    }

    @Ignore
    public EntitasTransaksi(String tipe,String kategori, int jumlah) {
        this.tipe = tipe;
        this.kategori = kategori;
        this.jumlah = jumlah;
    }

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

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
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
