package com.fritsonagung.catatandompet.Activity;

import android.arch.persistence.room.Room;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

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

public class GrafikKategoriPengeluaranActivity extends AppCompatActivity {

    private Calendar calendar;
    private DatabaseAplikasi db;
    private DateFormat df;
    public PieChart pieChart;
    private int graphHiburan, graphPajak, graphTagihan, graphMakanan,
            graphMinuman, graphTransportasi, graphTelepon, graphLainlain;

    private List<EntitasTransaksi> listTransaksi = new ArrayList<>();

    int[] color = {Color.rgb(0, 172, 194), Color.rgb(0, 143, 129), Color.rgb(46, 105, 47),
            Color.rgb(161, 16, 66), Color.rgb(122, 30, 137), Color.rgb(190, 143, 0),
            Color.rgb(175, 42, 0), Color.rgb(87, 62, 52), Color.rgb(40, 52, 116),
            Color.rgb(55, 71, 80)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_kategori_pengeluaran);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Grafik Kategori Pengeluaran");

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

        pieChart = findViewById(R.id.chart_pengeluaran);

        calendar = Calendar.getInstance();

        fetchDataFromRoom();
        setupPieChart();
    }

    private void setupPieChart() {

        ExecutorAplikasi.getInstance().diskIO().execute(new Runnable() {
            @Override
            public void run() {

                try {
                    getMonthKategoriPemasukanPieValues();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                listTransaksi.clear();
                if (graphHiburan != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Hiburan", graphHiburan));
                if (graphPajak != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Pajak", graphPajak));
                if (graphTagihan != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Tagihan", graphTagihan));
                if (graphMakanan != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Makanan", graphMakanan));
                if (graphMinuman != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Minuman", graphMinuman));
                if (graphTransportasi != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Transportasi", graphTransportasi));
                if (graphTelepon != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Telepon", graphTelepon));
                if (graphLainlain != 0)
                    listTransaksi.add(new EntitasTransaksi("Pengeluaran", "Lain-lain", graphLainlain));
            }
        });

        ExecutorAplikasi.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {

                List<PieEntry> pieEntries = new ArrayList<>();
                for (int i = 0; i < listTransaksi.size(); i++) {
                    pieEntries.add(new PieEntry(listTransaksi.get(i).getJumlah(), listTransaksi.get(i).getKategori()));
                }
                if (pieEntries.isEmpty()) {

                    pieChart.clear();
                    pieChart.setNoDataText("Data belum tersedia");

                } else {

                    pieChart.setVisibility(View.VISIBLE);
                    PieDataSet dataSet = new PieDataSet(pieEntries, null);
                    dataSet.setColors(color);
                    PieData pieData = new PieData(dataSet);

                    pieData.setValueTextSize(16);
                    pieData.setValueTextColor(Color.WHITE);
                    pieData.setValueFormatter(new PercentFormatter());
                    pieChart.setUsePercentValues(true);
                    pieChart.animateY(1000);
                    pieChart.setData(pieData);
                    pieChart.invalidate();

                    pieChart.getDescription().setText("");
                    Legend l = pieChart.getLegend();
                    l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.CENTER);
                    l.setWordWrapEnabled(true);
                }
            }
        });
    }

    private void getMonthKategoriPemasukanPieValues() throws ParseException {
        df = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault());
        String startDate, endDate;

        calendar.set(Calendar.DAY_OF_MONTH, 1);
        startDate = df.format(calendar.getTime());
        Date sDate = df.parse(startDate);
        final long sdate = sDate.getTime();

        calendar.set(Calendar.DAY_OF_MONTH, calendar.getActualMaximum(Calendar.DAY_OF_MONTH));
        endDate = df.format(calendar.getTime());
        Date eDate = df.parse(endDate);
        final long edate = eDate.getTime();

        graphHiburan = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Hiburan", sdate, edate);
        graphPajak = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Pajak", sdate, edate);
        graphTagihan = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Tagihan", sdate, edate);
        graphMakanan = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Makanan", sdate, edate);
        graphMinuman = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Minuman", sdate, edate);
        graphTransportasi = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Transportasi", sdate, edate);
        graphTelepon = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Telepon", sdate, edate);
        graphLainlain = db.transaksiDao().hitungJumlahGraphKategori("Pengeluaran", "Lain-lain", sdate, edate);
    }

    private void fetchDataFromRoom() {
        db = Room.databaseBuilder(getApplicationContext(),
                DatabaseAplikasi.class, "transaksi")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }

}
