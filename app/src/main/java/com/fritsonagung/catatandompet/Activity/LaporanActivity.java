package com.fritsonagung.catatandompet.Activity;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;
import com.fritsonagung.catatandompet.R;
import com.fritsonagung.catatandompet.Util.ExecutorAplikasi;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;


/**
 * Developed By:
 * Nama : Fritson Agung Julians Ayomi
 * NIM  : 10116076
 * Kelas: AKB-2
 * Tanggal Pengerjaan : 22 Juli 2019
 **/

public class LaporanActivity extends AppCompatActivity {

    private Calendar calendar;
    private TextView namaBulan, totalSelisihBulanan, totalPemasukanBulanan,
            totalPengeluaranBulanan, tanggalAntara;
    private LinearLayout buttonGrafikKategoriPemasukan, buttonGrafikKategoriPengeluaran;
    private int totalSelisih, totalPemasukan, totalPengeluaran,
            graphPemasukan, graphPengeluaran;
    private DatabaseAplikasi db;
    private String formattedDate;
    private DateFormat df;
    public PieChart pieChart;

    private List<EntitasTransaksi> listTransaksi = new ArrayList<>();

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
        pieChart = findViewById(R.id.chart);
        totalPemasukanBulanan = findViewById(R.id.TV_Total_Pemasukan_Perbulan);
        totalPengeluaranBulanan = findViewById(R.id.TV_Total_Pengeluaran_Perbulan);
        totalSelisihBulanan = findViewById(R.id.TV_Total_Selisih_Perbulan);
        buttonGrafikKategoriPemasukan = findViewById(R.id.button_grafik_kategori_pemasukan);
        buttonGrafikKategoriPengeluaran = findViewById(R.id.button_grafik_kategori_pengeluaran);

        calendar = Calendar.getInstance();
        df = new SimpleDateFormat("MMMM yyyy");
        formattedDate = df.format(calendar.getTime());
        namaBulan.setText(formattedDate);


        buttonGrafikKategoriPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaporanActivity.this, GrafikKategoriPemasukanActivity.class);
                startActivity(i);
            }
        });

        buttonGrafikKategoriPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LaporanActivity.this, GrafikKategoriPengeluaranActivity.class);
                startActivity(i);
            }
        });

        fetchDataFromRoom();

        try {
            getTotalBulanan();
            setupPieChart();
        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private void setupPieChart() {

        ExecutorAplikasi.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    getMonthPieValues();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                listTransaksi.clear();
                if (graphPemasukan != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", graphPemasukan));
                if (graphPengeluaran != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", graphPengeluaran));
            }
        });

        ExecutorAplikasi.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {

                List<PieEntry> pieEntries = new ArrayList<>();

                for (int i = 0; i < listTransaksi.size(); i++) {
                    pieEntries.add(new PieEntry(listTransaksi.get(i).getJumlah(), listTransaksi.get(i).getTipe()));
                }

                if (pieEntries.isEmpty()) {

                    pieChart.clear();
                    pieChart.setNoDataText("Data belum tersedia");

                } else {

                    pieChart.setVisibility(View.VISIBLE);
                    PieDataSet dataSet = new PieDataSet(pieEntries, null);
                    dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
                    PieData pieData = new PieData(dataSet);

                    pieData.setValueTextSize(16);
                    pieData.setValueTextColor(Color.WHITE);
                    pieData.setValueFormatter(new PercentFormatter());
                    pieChart.setUsePercentValues(true);
                    pieChart.setData(pieData);
                    pieChart.animateY(1000);
                    pieChart.invalidate();

                    pieChart.getDescription().setText("");
                    Legend l = pieChart.getLegend();
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    l.setWordWrapEnabled(true);
                }
            }
        });
    }

    private void getMonthPieValues() throws ParseException {

        DateFormat df = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String startDate, endDate;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = df.format(calendar.getTime());
        Date sDate = df.parse(startDate);
        final long sdate = sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df.format(calendar.getTime());
        Date eDate = df.parse(endDate);
        final long edate = eDate.getTime();

        graphPemasukan = db.transaksiDao().hitungJumlahGraphTransaksi("Pemasukan", sdate, edate);
        graphPengeluaran = db.transaksiDao().hitungJumlahGraphTransaksi("Pengeluaran", sdate, edate);
    }


    private void getTotalBulanan() throws ParseException {

        DateFormat df2 = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String startDate, endDate;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = df2.format(calendar.getTime());
        Date sDate = df2.parse(startDate);
        final long sdate = sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df2.format(calendar.getTime());
        Date eDate = df2.parse(endDate);
        final long edate = eDate.getTime();

        String dateString = startDate + " - " + endDate;
        tanggalAntara = findViewById(R.id.tanggalAntara);
        tanggalAntara.setText(dateString);

        ExecutorAplikasi.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {
                int pemasukan = db.transaksiDao().hitungTotalTransaksiBulanan("Pemasukan", sdate, edate);
                totalPemasukan = pemasukan;
                int pengeluaran = db.transaksiDao().hitungTotalTransaksiBulanan("Pengeluaran", sdate, edate);
                totalPengeluaran = pengeluaran;
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


    private void fetchDataFromRoom() {
        db = Room.databaseBuilder(getApplicationContext(),
                DatabaseAplikasi.class, "transaksi")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

}

