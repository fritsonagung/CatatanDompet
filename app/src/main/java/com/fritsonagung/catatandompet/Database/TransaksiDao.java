package com.fritsonagung.catatandompet.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 5 Juli 2019
 **/

@Dao
public interface TransaksiDao {


    @Query("SELECT * FROM transaksi ORDER BY tanggal DESC")
    List<EntitasTransaksi> tampilSemuaTransaksi();


    @Query("SELECT SUM (jumlah) FROM transaksi AS totalPemasukan " +
            "WHERE tipe =:tipeTransaksi")
    int hitungTotalTransaksi(String tipeTransaksi);


    @Query("SELECT SUM(jumlah) FROM transaksi WHERE tipe= :tipeTransaksi " +
            "AND tanggal BETWEEN :tanggalAwal AND :tanggalAkhir")
    int hitungJumlahGraphTransaksi(String tipeTransaksi,long tanggalAwal, long tanggalAkhir);


    @Query("SELECT SUM(jumlah) FROM transaksi WHERE tipe= :tipeTransaksi AND kategori= :jenisKategori " +
            "AND tanggal BETWEEN :tanggalAwal AND :tanggalAkhir")
    int hitungJumlahGraphKategori(String tipeTransaksi,String jenisKategori,long tanggalAwal, long tanggalAkhir);


    @Query("SELECT SUM (jumlah) FROM transaksi AS totalBulanan " +
            "WHERE tipe =:tipeTransaksi " +
            "AND tanggal BETWEEN :tanggalAwal AND :tanggalAkhir")
    int hitungTotalTransaksiBulanan(String tipeTransaksi, long tanggalAwal, long tanggalAkhir);


    @Query("SELECT * FROM transaksi WHERE tipe LIKE:cari " +
            "OR tanggal LIKE :cari OR kategori LIKE :cari OR jumlah LIKE :cari " +
            "OR keterangan LIKE :cari ")
    List<EntitasTransaksi>tampilCariTransaksi(String cari);



    @Insert
    void tambahTransaksi(EntitasTransaksi entitasTransaksi);

    @Delete
    void hapusTransaksi (EntitasTransaksi entitasTransaksi);

    @Update
    void ubahTransaksi(EntitasTransaksi entitasTransaksi);

}
