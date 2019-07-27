package com.fritsonagung.catatandompet;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;

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

public class TambahTransaksiActivity extends AppCompatActivity {


    private Button buttonBatal, buttonSimpan;
    private Spinner spinnerTipeTransaksi, spinnerKategori;
    private Toolbar toolbar;
    private EditText tanggalTransaksi, keterangan, jumlah;
    public static DatabaseAplikasi db;
    private DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private EntitasTransaksi entitasTransaksi;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah_transaksi);

        spinnerTipeTransaksi = findViewById(R.id.Spinner_Jenis_Transaksi);
        tanggalTransaksi = findViewById(R.id.ET_Tanggal);
        spinnerKategori = findViewById(R.id.Spinner_Jenis_Kategori);
        jumlah = findViewById(R.id.ET_Jumlah);
        keterangan = findViewById(R.id.ET_Keterangan);
        buttonSimpan = findViewById(R.id.BT_Simpan_Transaksi);
        buttonBatal = findViewById(R.id.BT_Batal_Transaksi);


        final ArrayAdapter<String> adapterTipeTransaksi = new ArrayAdapter<String>(TambahTransaksiActivity.this,
                R.layout.spinner_item, getResources().
                getStringArray(R.array.jenis_transaksi_dan_kategori));
        adapterTipeTransaksi.setDropDownViewResource(R.layout.spinner_item);
        spinnerTipeTransaksi.setAdapter(adapterTipeTransaksi);


        final ArrayAdapter<String> adapterKategori = new ArrayAdapter<String>(TambahTransaksiActivity.this,
                R.layout.spinner_item, getResources().
                getStringArray(R.array.listKategoriPemasukan));
        adapterTipeTransaksi.setDropDownViewResource(R.layout.spinner_item);


        spinnerTipeTransaksi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerValue = spinnerTipeTransaksi.getSelectedItem().toString();
                if (spinnerValue.equals("Pemasukan")) {
                    final ArrayAdapter<String> adapterKategori = new ArrayAdapter<String>(TambahTransaksiActivity.this,
                            R.layout.spinner_item, getResources().
                            getStringArray(R.array.listKategoriPemasukan));
                    adapterTipeTransaksi.setDropDownViewResource(R.layout.spinner_item);
                    spinnerKategori.setAdapter(adapterKategori);
                }else if (spinnerValue.equals("Pengeluaran")){
                    final ArrayAdapter<String> adapterKategori = new ArrayAdapter<String>(TambahTransaksiActivity.this,
                            R.layout.spinner_item, getResources().
                            getStringArray(R.array.listKategoriPengeluaran));
                    adapterTipeTransaksi.setDropDownViewResource(R.layout.spinner_item);
                    spinnerKategori.setAdapter(adapterKategori);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(TambahTransaksiActivity.this, "Mohon Lengkapi Data Transaksi!", Toast.LENGTH_SHORT).show();
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tambah Transaksi");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        // Tambah panah kembali
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = Room.databaseBuilder(getApplicationContext(),
                DatabaseAplikasi.class, "transaksi")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();


        // Set Current Date
        String date_n = new SimpleDateFormat("dd-M-yyyy", Locale.getDefault()).format(new Date());
        tanggalTransaksi.setText(date_n);

        //ET Date Click Listener
        tanggalTransaksi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calendar = Calendar.getInstance();
                mYear = calendar.get(Calendar.YEAR);
                mMonth = calendar.get(Calendar.MONTH);
                mDay = calendar.get(Calendar.DAY_OF_MONTH);
                datePickerDialog = new DatePickerDialog(TambahTransaksiActivity.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tanggalTransaksi.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                simpanDataTransaski();
            }
        });

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    public void simpanDataTransaski(){

        if (!jumlah.getText().toString().isEmpty() && !keterangan.getText().toString().isEmpty()) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

            Date tanggal = null;
            try {
                tanggal = sdf.parse(String.valueOf(tanggalTransaksi.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            String tipe = spinnerTipeTransaksi.getSelectedItem().toString();
            String kategori = spinnerKategori.getSelectedItem().toString();
            int jumlahTransaksi = Integer.parseInt(jumlah.getText().toString());
            String keteranganTransaksi = keterangan.getText().toString();

            entitasTransaksi = new EntitasTransaksi();
            entitasTransaksi.setTipe(tipe);
            entitasTransaksi.setTanggal(tanggal);
            entitasTransaksi.setKategori(kategori);
            entitasTransaksi.setJumlah(jumlahTransaksi);
            entitasTransaksi.setKeterangan(keteranganTransaksi);
            //Insert data in database
            db.transaksiDao().tambahTransaksi(entitasTransaksi);

            Toast.makeText(TambahTransaksiActivity.this, "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(TambahTransaksiActivity.this, MainActivity.class));
        } else {
            Toast.makeText(TambahTransaksiActivity.this, "Mohon Lengkapi Data Transaksi!", Toast.LENGTH_SHORT).show();
        }
    }

}
