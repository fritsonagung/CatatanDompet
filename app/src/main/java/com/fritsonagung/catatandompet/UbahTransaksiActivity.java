package com.fritsonagung.catatandompet;

import android.Manifest;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class UbahTransaksiActivity extends AppCompatActivity {

    private Button buttonHapus, buttonUbah, buttonYa, buttonTidak;
    private Spinner spinnerTipeTransaksi, spinnerKategori;
    private Toolbar toolbar;
    private EditText tanggalTransaksi, keterangan, jumlah;
    private TextView dialogHapus;
    public static DatabaseAplikasi db;
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private EntitasTransaksi entitasTransaksi;
    private Dialog dialog;

    private String[] jenisKategori = {
            "Gaji Bulanan",
            "Belanja Umum"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_transaksi);

        spinnerTipeTransaksi = findViewById(R.id.Spinner_Jenis_Transaksi);
        tanggalTransaksi = findViewById(R.id.ET_Tanggal);
        spinnerKategori = findViewById(R.id.Spinner_Jenis_Kategori);
        jumlah = findViewById(R.id.ET_Jumlah);
        keterangan = findViewById(R.id.ET_Keterangan);
        buttonUbah = findViewById(R.id.BT_Ubah_Transaksi);
        buttonHapus = findViewById(R.id.BT_Hapus_Transaksi);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(UbahTransaksiActivity.this,
                R.layout.spinner_item, getResources().
                getStringArray(R.array.jenis_transaksi_dan_kategori));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinnerTipeTransaksi.setAdapter(adapter);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapterKategori = new ArrayAdapter<String>(this,
                R.layout.spinner_item, jenisKategori);

        // mengeset Array Adapter ke Spinner
        adapterKategori.setDropDownViewResource(R.layout.spinner_item);
        spinnerKategori.setAdapter(adapterKategori);

        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ubah Transaksi");
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                Intent intent = new Intent(UbahTransaksiActivity.this, MainActivity.class);
                startActivity(intent);
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
                datePickerDialog = new DatePickerDialog(UbahTransaksiActivity.this, R.style.DialogTheme,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                                tanggalTransaksi.setText(day + "/" + (month + 1) + "/" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        buttonUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if (!jumlah.getText().toString().isEmpty() && !keterangan.getText().toString().isEmpty()) {

                    entitasTransaksi = new EntitasTransaksi();
                    entitasTransaksi.setTipe(spinnerTipeTransaksi.getItemAtPosition(spinnerTipeTransaksi.getSelectedItemPosition()).toString());
                    entitasTransaksi.setKategori(spinnerKategori.getItemAtPosition(spinnerKategori.getSelectedItemPosition()).toString());
                    entitasTransaksi.setTanggal(tanggalTransaksi.getText().toString());
                    entitasTransaksi.setJumlah(Double.valueOf(jumlah.getText().toString()));
                    entitasTransaksi.setKeterangan(keterangan.getText().toString());

                    //Update data in database
                    db.transaksiDao().ubahTransaksi(entitasTransaksi);

                    Toast.makeText(UbahTransaksiActivity.this, "Data Berhasil Diubah!", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(UbahTransaksiActivity.this, MainActivity.class));

                } else {
                    Toast.makeText(UbahTransaksiActivity.this, "Mohon Lengkapi Data Transaksi!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogKonfirmasiHapus();
            }
        });
    }

    public void showDialogKonfirmasiHapus() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(UbahTransaksiActivity.this, R.style.DialogTheme);
        View view = getLayoutInflater().inflate(R.layout.dialog_konfirmasi_hapus, null);

        dialogHapus = view.findViewById(R.id.TV_Konfirmasi_Hapus);
        dialogHapus.setText("Apakah Anda Yakin Ingin Menghapus Transaksi Ini?");

        mbuilder.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                db.transaksiDao().hapusTransaksi(entitasTransaksi);
                Toast.makeText(UbahTransaksiActivity.this, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
            }
        });

        mbuilder.setNegativeButton("BATAL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });

        mbuilder.setView(view);
        AlertDialog dialog = mbuilder.create();
        dialog.show();

    }


}

