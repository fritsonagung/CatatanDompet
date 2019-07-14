package com.fritsonagung.catatandompet;

import android.app.DatePickerDialog;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;

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
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private EntitasTransaksi entitasTransaksi;
    private String[] tipeTransaksi = {
            "Pemasukan",
            "Pengeluaran"
    };

    private String[] jenisKategori = {
            "Gaji Bulanan",
            "Belanja Umum"
    };

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


        // inisialiasi Array Adapter Transaksi dengan memasukkan string array di atas
        final ArrayAdapter<String> adapterTransaksi = new ArrayAdapter<String>(this,
                R.layout.spinner_item, tipeTransaksi);

        // mengeset Array Adapter ke Spinner
        adapterTransaksi.setDropDownViewResource(R.layout.spinner_item);
        spinnerTipeTransaksi.setAdapter(adapterTransaksi);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapterKategori = new ArrayAdapter<String>(this,
                R.layout.spinner_item, jenisKategori);

        // mengeset Array Adapter ke Spinner
        adapterKategori.setDropDownViewResource(R.layout.spinner_item);
        spinnerKategori.setAdapter(adapterKategori);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Tambah Transaksi");

        // Tambah panah kembali
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        db = Room.databaseBuilder(getApplicationContext(),
                DatabaseAplikasi.class, "transaksi")
                .fallbackToDestructiveMigration().allowMainThreadQueries().build();

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        // Set Current Date
        String date_n = new SimpleDateFormat("dd/M/yyyy", Locale.getDefault()).format(new Date());
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
                                tanggalTransaksi.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        buttonSimpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!jumlah.getText().toString().isEmpty() && !keterangan.getText().toString().isEmpty()) {

                    entitasTransaksi = new EntitasTransaksi();
                    entitasTransaksi.setTipe(spinnerTipeTransaksi.getItemAtPosition(spinnerTipeTransaksi.getSelectedItemPosition()).toString());
                    entitasTransaksi.setKategori(spinnerKategori.getItemAtPosition(spinnerKategori.getSelectedItemPosition()).toString());
                    entitasTransaksi.setTanggal(tanggalTransaksi.getText().toString());
                    entitasTransaksi.setJumlah(Double.valueOf(jumlah.getText().toString()));
                    entitasTransaksi.setKeterangan(keterangan.getText().toString());

                    //Insert data in database
                    db.transaksiDao().tambahTransaksi(entitasTransaksi);

                    Toast.makeText(TambahTransaksiActivity.this, "Data Berhasil Disimpan!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(TambahTransaksiActivity.this, MainActivity.class));

                } else {
                    Toast.makeText(TambahTransaksiActivity.this, "Mohon Lengkapi Data Transaksi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
