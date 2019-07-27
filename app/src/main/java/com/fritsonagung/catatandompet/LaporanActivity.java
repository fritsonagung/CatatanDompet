package com.fritsonagung.catatandompet;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;
import com.fritsonagung.catatandompet.Database.ExecutorAplikasi;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


/**
 * Developed By:
 * Nama : Fritson Agung Julians Ayomi
 * NIM  : 10116076
 * Kelas: AKB-2
 * Tanggal Pengerjaan : 5 Juli 2019
 **/

public class LaporanActivity extends AppCompatActivity {

    private ImageButton buttonPrev, buttonNext;
    private Calendar calendar;
    private TextView namaBulan, totalSelisihBulanan, totalPemasukanBulanan,
            totalPengeluaranBulanan, namaKategoriPengeluaran, namaKategoriPemasukan,
            totalKategoriPemasukan, totalKategoriPengeluaran, tanggalAntara;
    private int totalSelisih, totalPemasukan, totalPengeluaran, totalTopKategoriPemasukan,
            totalTopKategoriPengeluaran;
    private String topKategoriPemasukan, topKategoriPengeluaran;
    private DatabaseAplikasi db;
    private String formattedDate;
    private DateFormat df;

    @SuppressLint("SimpleDateFormat")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_laporan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Laporan");

        // Tambah panah kembali
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        namaBulan = findViewById(R.id.TV_Nama_Bulan);
        totalPemasukanBulanan = findViewById(R.id.TV_Total_Pemasukan_Perbulan);
        totalPengeluaranBulanan = findViewById(R.id.TV_Total_Pengeluaran_Perbulan);
        totalSelisihBulanan = findViewById(R.id.TV_Total_Selisih_Perbulan);
        namaKategoriPemasukan = findViewById(R.id.TV_Top_Kategori_Pemasukan);
        namaKategoriPengeluaran = findViewById(R.id.TV_Top_Kategori_Pengeluaran);
        totalKategoriPemasukan = findViewById(R.id.TV_Total_Top_Kategori_Pemasukan_Perbulan);
        totalKategoriPengeluaran = findViewById(R.id.TV_Total_Top_Kategori_Pengeluaran_Perbulan);
        buttonNext = findViewById(R.id.IBT_Next);
        buttonPrev = findViewById(R.id.IBT_Prev);

        calendar = Calendar.getInstance();
        df = new SimpleDateFormat("MMMM yyyy");
        formattedDate = df.format(calendar.getTime());
        namaBulan.setText(formattedDate);

        fetchDataFromRoom();
        try {
            getTotalBulanan();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getTotalBulananNext();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

        buttonPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    getTotalBulananPrev();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void getTotalBulanan() throws ParseException {

        DateFormat df2 = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String startDate, endDate;

        calendar.set(Calendar.DAY_OF_MONTH,1);
        startDate = df2.format(calendar.getTime());
        Date sDate=df2.parse(startDate);
        final long sdate=sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df2.format(calendar.getTime());
        Date eDate=df2.parse(endDate);
        final long edate=eDate.getTime();

        String dateString = startDate + " - " + endDate;
        tanggalAntara = findViewById(R.id.tanggalAntara);
        tanggalAntara.setText(dateString);

        ExecutorAplikasi.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int pemasukan = db.transaksiDao().totalTransaksiBulanan("Pemasukan",sdate,edate);
                totalPemasukan= pemasukan;
                int pengeluaran = db.transaksiDao().totalTransaksiBulanan("Pengeluaran",sdate,edate);
                totalPengeluaran= pengeluaran;
                int selisih = pemasukan - pengeluaran;
                totalSelisih = selisih;
            }
        });
        ExecutorAplikasi.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                totalPemasukanBulanan.setText(String.valueOf(totalPemasukan));
                totalPengeluaranBulanan.setText(String.valueOf(totalPengeluaran));
                totalSelisihBulanan.setText(String.valueOf(totalSelisih));;
            }
        });
    }

    private void getTotalBulananNext() throws ParseException {

        df = new SimpleDateFormat("MMMM yyyy");
        calendar.add(Calendar.MONTH, +1);
        formattedDate = df.format(calendar.getTime());
        namaBulan.setText(formattedDate);
        DateFormat df2 = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String startDate, endDate;

        calendar.set(Calendar.DAY_OF_MONTH,1);
        startDate = df2.format(calendar.getTime());
        Date sDate= null;
        try {
            sDate = df2.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long sdate=sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df2.format(calendar.getTime());
        Date eDate= null;
        try {
            eDate = df2.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long edate=eDate.getTime();

        String dateString = startDate + " - " + endDate;
        tanggalAntara = findViewById(R.id.tanggalAntara);
        tanggalAntara.setText(dateString);

        ExecutorAplikasi.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int pemasukan = db.transaksiDao().totalTransaksiBulanan("Pemasukan",sdate,edate);
                totalPemasukan= pemasukan;
                int pengeluaran = db.transaksiDao().totalTransaksiBulanan("Pengeluaran",sdate,edate);
                totalPengeluaran= pengeluaran;
                int selisih = pemasukan - pengeluaran;
                totalSelisih = selisih;

            }
        });
        ExecutorAplikasi.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                totalPemasukanBulanan.setText(String.valueOf(totalPemasukan));
                totalPengeluaranBulanan.setText(String.valueOf(totalPengeluaran));
                totalSelisihBulanan.setText(String.valueOf(totalSelisih));
            }
        });

    }

    private void getTotalBulananPrev() throws ParseException {

        df = new SimpleDateFormat("MMMM yyyy");
        calendar.add(Calendar.MONTH, -1);
        formattedDate = df.format(calendar.getTime());
        namaBulan.setText(formattedDate);
        DateFormat df2 = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String startDate, endDate;

        calendar.set(Calendar.DAY_OF_MONTH,1);
        startDate = df2.format(calendar.getTime());
        Date sDate= null;
        try {
            sDate = df2.parse(startDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long sdate=sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df2.format(calendar.getTime());
        Date eDate= null;
        try {
            eDate = df2.parse(endDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        final long edate=eDate.getTime();

        String dateString = startDate + " - " + endDate;
        tanggalAntara = findViewById(R.id.tanggalAntara);
        tanggalAntara.setText(dateString);

        ExecutorAplikasi.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int pemasukan = db.transaksiDao().totalTransaksiBulanan("Pemasukan",sdate,edate);
                totalPemasukan= pemasukan;
                int pengeluaran = db.transaksiDao().totalTransaksiBulanan("Pengeluaran",sdate,edate);
                totalPengeluaran= pengeluaran;
                int selisih = pemasukan - pengeluaran;
                totalSelisih = selisih;

            }
        });
        ExecutorAplikasi.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                totalPemasukanBulanan.setText(String.valueOf(totalPemasukan));
                totalPengeluaranBulanan.setText(String.valueOf(totalPengeluaran));
                totalSelisihBulanan.setText(String.valueOf(totalSelisih));
            }
        });

    }


    private void getTopKategoriBulanan() throws ParseException {

        DateFormat df2 = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String startDate, endDate;

        calendar.set(Calendar.DAY_OF_MONTH,1);
        startDate = df2.format(calendar.getTime());
        Date sDate=df2.parse(startDate);
        final long sdate=sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df2.format(calendar.getTime());
        Date eDate=df2.parse(endDate);
        final long edate=eDate.getTime();

        ExecutorAplikasi.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int topKategoriPemasukanBulanan = db.transaksiDao().topKategoriBulanan("Pemasukan",sdate,edate);
                totalTopKategoriPemasukan= topKategoriPemasukanBulanan;
                int topKategroiPengeluaranBulanan = db.transaksiDao().topKategoriBulanan("Pengeluaran",sdate,edate);
                totalTopKategoriPengeluaran= topKategroiPengeluaranBulanan;
            }
        });
        ExecutorAplikasi.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {
                totalKategoriPemasukan.setText(String.valueOf(totalTopKategoriPemasukan));
                totalKategoriPengeluaran.setText(String.valueOf(totalTopKategoriPengeluaran));
            }
        });
    }

    private void fetchDataFromRoom() {
        db = Room.databaseBuilder(getApplicationContext(),
                DatabaseAplikasi.class, "transaksi")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

}

