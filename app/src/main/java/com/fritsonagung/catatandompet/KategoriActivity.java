package com.fritsonagung.catatandompet;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

/**
 * Developed By:
 * Nama : Fritson Agung Julians Ayomi
 * NIM  : 10116076
 * Kelas: AKB-2
 * Tanggal Pengerjaan : 6 Juli 2019
 **/

public class KategoriActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kategori);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Kategori");

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




    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu; Menambahkan item ke actionbar jika ada.
        getMenuInflater().inflate(R.menu.menu_kategori, menu);

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_tambah) {

            showDialogTambahKategori();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void showDialogTambahKategori() {
        AlertDialog.Builder mbuilder = new AlertDialog.Builder(KategoriActivity.this, R.style.DialogTheme2);
        View view = getLayoutInflater().inflate(R.layout.dialog_tambah_kategori, null);
        final Spinner spinner = view.findViewById(R.id.Spinner_Jenis_Kategori);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(KategoriActivity.this,
                R.layout.spinner_item, getResources().
                getStringArray(R.array.jenis_transaksi_dan_kategori));
        adapter.setDropDownViewResource(R.layout.spinner_item);
        spinner.setAdapter(adapter);

        mbuilder.setPositiveButton("SIMPAN", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                spinner.getSelectedItem().toString();
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
