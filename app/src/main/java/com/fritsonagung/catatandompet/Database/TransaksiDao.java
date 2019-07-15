package com.fritsonagung.catatandompet.Database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 7 Juli 2019
 **/

@Dao
public interface TransaksiDao {

    @Query("SELECT * FROM transaksi")
    List<EntitasTransaksi>tampilSemuaTransaksi();

    @Query("SELECT SUM (jumlah) FROM transaksi as totalPemasukan WHERE tipe =:tipeTransaksi")
    int hitungTotalPemasukan(String tipeTransaksi);

    @Query("SELECT * FROM transaksi WHERE tanggal = :curTanggal")
    List<EntitasTransaksi>tampilTransaksiHariIni(String curTanggal);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void tambahTransaksi(EntitasTransaksi entitasTransaksi);

    @Delete
    void hapusTransaksi (EntitasTransaksi entitasTransaksi);

    @Update(onConflict = OnConflictStrategy.REPLACE)
    void ubahTransaksi(EntitasTransaksi entitasTransaksi);

}
