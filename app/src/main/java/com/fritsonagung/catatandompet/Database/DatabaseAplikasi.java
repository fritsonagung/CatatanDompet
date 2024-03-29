package com.fritsonagung.catatandompet.Database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

import com.fritsonagung.catatandompet.Util.DateConverter;


/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 5 Juli 2019
 **/

@Database(entities = {EntitasTransaksi.class}, version = 1, exportSchema = false)
@TypeConverters(DateConverter.class)
public abstract class DatabaseAplikasi extends RoomDatabase {

    private static volatile DatabaseAplikasi INSTANCE;

    public static DatabaseAplikasi getDatabase(final Context context) {

        if (INSTANCE == null) {
            synchronized (DatabaseAplikasi.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                            DatabaseAplikasi.class, "catatan_dompet_database")
                            .fallbackToDestructiveMigration().build();
                }
            }
        }

        return INSTANCE;
    }

    public abstract TransaksiDao transaksiDao();
}



