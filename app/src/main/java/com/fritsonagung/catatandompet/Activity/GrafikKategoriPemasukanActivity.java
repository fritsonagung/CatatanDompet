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

public class GrafikKategoriPemasukanActivity extends AppCompatActivity {

    private Calendar calendar;
    private DatabaseAplikasi db;
    private DateFormat df;
    public PieChart pieChart;
    private int graphGaji, graphBonus, graphDeposito, graphInvestasi, graphPenjualan,
            graphPenyewaan, graphKupon, graphDividen, graphLainlain;

    private List<EntitasTransaksi> listTransaksi = new ArrayList<>();

    int [] color={ Color.rgb(0, 172, 194), Color.rgb(0, 143, 129), Color.rgb(46, 105, 47),
            Color.rgb(161, 16, 66), Color.rgb(122, 30, 137), Color.rgb(190, 143, 0),
            Color.rgb(175, 42, 0), Color.rgb(87, 62, 52), Color.rgb(40, 52, 116),
            Color.rgb(55, 71, 80)
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_grafik_kategori_pemasukan);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Grafik Kategori Pemasukan");

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

        pieChart = findViewById(R.id.chart_pemasukan);

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
                if (graphGaji != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Gaji", graphGaji));
                if (graphDeposito != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Deposito", graphDeposito));
                if (graphInvestasi != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Investasi", graphInvestasi));
                if (graphPenjualan != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Penjualan", graphPenjualan));
                if (graphPenyewaan != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Penyewaan", graphPenyewaan));
                if (graphKupon != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Kupon", graphKupon));
                if (graphBonus != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Bonus", graphBonus));
                if (graphDividen != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Dividen", graphDividen));
                if (graphLainlain != 0)
                    listTransaksi.add(new EntitasTransaksi("Pemasukan", "Lain-lain", graphLainlain));
            }
        });

        ExecutorAplikasi.getInstance().mainThread().execute(new Runnable() {
            @Override
            public void run() {

                List<PieEntry> pieEntries = new ArrayList<>();
                for (int i = 0; i < listTransaksi.size(); i++) {
                    pieEntries.add(new PieEntry(listTransaksi.get(i).getJumlah(), listTransaksi.get(i).getKategori()));
                }
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
                l.setEnabled(false);


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

        graphGaji = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Gaji", sdate, edate);
        graphDeposito = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Deposito", sdate, edate);
        graphInvestasi = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Investasi", sdate, edate);
        graphPenjualan = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Penjualan", sdate, edate);
        graphPenyewaan = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Penyewaan", sdate, edate);
        graphKupon = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Kupon", sdate, edate);
        graphBonus = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Bonus", sdate, edate);
        graphDividen = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Dividen", sdate, edate);
        graphLainlain = db.transaksiDao().hitungJumlahGraphKategori("Pemasukan", "Lain-lain", sdate, edate);
    }

    private void fetchDataFromRoom() {
        db = Room.databaseBuilder(getApplicationContext(),
                DatabaseAplikasi.class, "transaksi")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();
    }
}
