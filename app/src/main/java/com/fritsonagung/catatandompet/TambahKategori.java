package com.fritsonagung.catatandompet;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 6 Juli 2019
 **/

public class TambahKategori extends AppCompatActivity {

    private Button buttonBatal;
    private Spinner spinner_tipe_kategori;
    private String[] tipeKategori = {
            "Pemasukan",
            "Pengeluaran"
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_tambah_kategori);


        spinner_tipe_kategori = (Spinner) findViewById(R.id.Spinner_Jenis_Kategori);

        // inisialiasi Array Adapter dengan memasukkan string array di atas
        final ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                R.layout.spinner_item, tipeKategori);


        // mengeset Array Adapter ke Spinner
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner_tipe_kategori.setAdapter(adapter);


        buttonBatal = findViewById(R.id.BT_Batal_Kategori);
        buttonBatal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
}
