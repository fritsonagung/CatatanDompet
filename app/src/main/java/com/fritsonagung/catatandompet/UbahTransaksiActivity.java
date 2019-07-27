package com.fritsonagung.catatandompet;


import android.app.DatePickerDialog;

import android.arch.persistence.room.Room;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
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
import android.widget.TextView;
import android.widget.Toast;

import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * Developed By:
 * Nama : Fritson Agung Julians Ayomi
 * NIM  : 10116076
 * Kelas: AKB-2
 * Tanggal Pengerjaan : 5 Juli 2019
 **/

public class UbahTransaksiActivity extends AppCompatActivity {

    private Button buttonHapus, buttonUbah;
    private Spinner spinnerTipeTransaksi;
    private Spinner spinnerKategori;
    private Toolbar toolbar;
    private EditText tanggalTransaksi, keteranganTransaksi, jumlahTransaksi;
    private TextView konfirmHapus;
    public static DatabaseAplikasi db;
    DatePickerDialog datePickerDialog;
    private Calendar calendar;
    private int mYear, mMonth, mDay;
    private EntitasTransaksi entitasTransaksi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ubah_transaksi);

        spinnerTipeTransaksi = findViewById(R.id.Spinner_Jenis_Transaksi);
        tanggalTransaksi = findViewById(R.id.ET_Tanggal);
        spinnerKategori = findViewById(R.id.Spinner_Jenis_Kategori);
        jumlahTransaksi = findViewById(R.id.ET_Ubah_Jumlah);
        keteranganTransaksi = findViewById(R.id.ET_Keterangan);
        buttonUbah = findViewById(R.id.BT_Ubah_Transaksi);
        buttonHapus = findViewById(R.id.BT_Hapus_Transaksi);

        final ArrayAdapter<String> adapterTipeTransaksi = new ArrayAdapter<String>(UbahTransaksiActivity.this,
                R.layout.spinner_item, getResources().
                getStringArray(R.array.jenis_transaksi_dan_kategori));
        adapterTipeTransaksi.setDropDownViewResource(R.layout.spinner_item);
        spinnerTipeTransaksi.setAdapter(adapterTipeTransaksi);

        spinnerTipeTransaksi.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String spinnerValue = spinnerTipeTransaksi.getSelectedItem().toString();
                Intent intent = getIntent();
                assert intent != null;
                if (spinnerValue.equals("Pemasukan")) {
                    final ArrayAdapter<String> adapterKategori = new ArrayAdapter<String>(UbahTransaksiActivity.this,
                            R.layout.spinner_item, getResources().
                            getStringArray(R.array.listKategoriPemasukan));
                    adapterTipeTransaksi.setDropDownViewResource(R.layout.spinner_item);
                    spinnerKategori.setAdapter(adapterKategori);
                    spinnerKategori.setSelection(adapterKategori.getPosition(intent.getStringExtra("kategori")));
                } else if (spinnerValue.equals("Pengeluaran")) {
                    final ArrayAdapter<String> adapterKategori = new ArrayAdapter<String>(UbahTransaksiActivity.this,
                            R.layout.spinner_item, getResources().
                            getStringArray(R.array.listKategoriPengeluaran));
                    adapterTipeTransaksi.setDropDownViewResource(R.layout.spinner_item);
                    spinnerKategori.setAdapter(adapterKategori);
                    spinnerKategori.setSelection(adapterKategori.getPosition(intent.getStringExtra("kategori")));
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Toast.makeText(UbahTransaksiActivity.this, "Mohon Lengkapi Data Transaksi!", Toast.LENGTH_SHORT).show();
            }
        });


        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Ubah Transaksi");
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

        Intent intent = getIntent();
        assert intent != null;

        spinnerTipeTransaksi.setSelection(adapterTipeTransaksi.getPosition(intent.getStringExtra("tipe")));
        tanggalTransaksi.setText(intent.getStringExtra("tanggal"));
        jumlahTransaksi.setText(String.valueOf(intent.getIntExtra("jumlah", 0)));
        keteranganTransaksi.setText(intent.getStringExtra("keterangan"));


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
                                tanggalTransaksi.setText(day + "-" + (month + 1) + "-" + year);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
        });

        buttonUbah.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ubahDataTransaksi();
            }
        });

        buttonHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialogKonfirmasiHapus();
            }
        });
    }

    public void ubahDataTransaksi(){
        if (!jumlahTransaksi.getText().toString().isEmpty() && !keteranganTransaksi.getText().toString().isEmpty()) {

            SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");

            Date tanggal = null;
            try {
                tanggal = sdf.parse(String.valueOf(tanggalTransaksi.getText()));
            } catch (ParseException e) {
                e.printStackTrace();
            }

            int idTransaksi = getIntent().getIntExtra("id", 0);
            String tipe = spinnerTipeTransaksi.getSelectedItem().toString();
            String kategori = spinnerKategori.getSelectedItem().toString();
            int jumlah = Integer.parseInt(jumlahTransaksi.getText().toString());
            String keterangan = keteranganTransaksi.getText().toString();

            entitasTransaksi = new EntitasTransaksi();
            entitasTransaksi.setId_transaksi(idTransaksi);
            entitasTransaksi.setTipe(tipe);
            entitasTransaksi.setTanggal(tanggal);
            entitasTransaksi.setKategori(kategori);
            entitasTransaksi.setJumlah(jumlah);
            entitasTransaksi.setKeterangan(keterangan);

            //Update data in database
            db.transaksiDao().ubahTransaksi(entitasTransaksi);

            Toast.makeText(UbahTransaksiActivity.this, "Data Berhasil Diubah!", Toast.LENGTH_SHORT).show();

            startActivity(new Intent(UbahTransaksiActivity.this, MainActivity.class));
        } else {
            Toast.makeText(UbahTransaksiActivity.this, "Mohon Lengkapi Data Transaksi!", Toast.LENGTH_SHORT).show();
        }
    }

    public void showDialogKonfirmasiHapus() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(UbahTransaksiActivity.this, R.style.DialogTheme);
        View view = getLayoutInflater().inflate(R.layout.dialog_konfirmasi_hapus, null);

        konfirmHapus = view.findViewById(R.id.TV_Konfirmasi_Hapus);
        konfirmHapus.setText(R.string.konfirmasi_hapus);


        mbuilder.setPositiveButton("HAPUS", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                int idTransaksi = getIntent().getIntExtra("id", 0);
                entitasTransaksi = new EntitasTransaksi();
                entitasTransaksi.setId_transaksi(idTransaksi);
                db.transaksiDao().hapusTransaksi(entitasTransaksi);
                Toast.makeText(UbahTransaksiActivity.this, "Data Berhasil Dihapus!", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(UbahTransaksiActivity.this, MainActivity.class));
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

    @Override
    public void onBackPressed() {
        Intent i = new Intent(this, MainActivity.class);
        startActivity(i);
    }
}

