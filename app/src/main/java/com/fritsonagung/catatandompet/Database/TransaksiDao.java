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
 Tanggal Pengerjaan : 7 Juli 2019
 **/

@Dao
public interface TransaksiDao {

    @Query("SELECT * FROM transaksi")
    List<EntitasTransaksi>tampilSemuaTransaksi();

    @Query("SELECT SUM('jumlah') WHERE 'tipe'='Pemasukan'")
    int hitungTotalPemasukan();


    @Insert
    void tambahTransaksi(EntitasTransaksi entitasTransaksi);

    @Delete
    void hapusTransaksi (EntitasTransaksi entitasTransaksi);

    @Update
    void ubahTransaksi(EntitasTransaksi entitasTransaksi);

}
