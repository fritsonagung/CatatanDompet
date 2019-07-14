package com.fritsonagung.catatandompet;

import android.app.SearchManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.fritsonagung.catatandompet.Adapter.AdapterTransaksi;
import com.fritsonagung.catatandompet.Database.DatabaseAplikasi;
import com.fritsonagung.catatandompet.Database.EntitasTransaksi;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Developed By:
 * Nama : Fritson Agung Julians Ayomi
 * NIM  : 10116076
 * Kelas: AKB-2
 * Tanggal Pengerjaan : 5 Juli 2019
 **/


public class MainActivity extends AppCompatActivity implements AdapterTransaksi.OnTransaksiListener {

    private boolean sedangMencari = false;
    private AppCompatDialog dialog;
    private Button buttonTentang;
    private AdapterTransaksi adapterTransaksi;
    public static DatabaseAplikasi db;
    private RecyclerView recyclerView;
    List<EntitasTransaksi> listTransaksi = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("Utama");

        // Set Current Date
        String date_n = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault()).format(new Date());
        TextView date = (TextView) findViewById(R.id.TV_Tanggal);
        date.setText(date_n);

        fetchDataFromRoom();
        initRecyclerView();
        setAdapter();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate menu; Menambahkan item ke actionbar jika ada.
        getMenuInflater().inflate(R.menu.menu_utama, menu);

        //Kaitkan konfigurasi yang dapat dicari dengan SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_cari).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                sedangMencari = false;

                // Mempopulasikan kembali transaksi
                onResume();

                //false: memungkinkan perilaku pembersihan default pada tampilan pencarian pada penutupan.
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sedangMencari = true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_tambah) {
            Intent i = new Intent(getApplicationContext(), TambahTransaksiActivity.class);
            final Bundle b = new Bundle();
            startActivity(i);
            return true;
        } else if (id == R.id.kategori) {
            Intent i = new Intent(getApplicationContext(), KategoriActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.laporan) {
            Intent i = new Intent(getApplicationContext(), LaporanActivity.class);
            startActivity(i);
            return true;
        } else if (id == R.id.tentang) {
            showDialogTentang();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showDialogTentang() {
        dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.dialog_tentang);

        buttonTentang = dialog.findViewById(R.id.BT_OK_Tentang);
        buttonTentang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
    private void fetchDataFromRoom() {
        db = Room.databaseBuilder(getApplicationContext(),
                DatabaseAplikasi.class,"transaksi").fallbackToDestructiveMigration().allowMainThreadQueries().build();
        listTransaksi = (List<EntitasTransaksi>) db.transaksiDao().tampilSemuaTransaksi();

        //just checking data from db
        for (int i = 0 ;i <listTransaksi.size();i++){
            Log.e("Aplikasi",listTransaksi.get(i).getKategori()+i);
            Log.e("Aplikasi",listTransaksi.get(i).getKeterangan()+i);
            Log.e("Aplikasi", String.valueOf(listTransaksi.get(i).getJumlah()+i));
        }
    }
    private void initRecyclerView() {
        recyclerView = (RecyclerView)findViewById(R.id.list_transaksi);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(llm);
        adapterTransaksi = new AdapterTransaksi(this,listTransaksi, this);
    }
    private void setAdapter() {
        recyclerView.setAdapter(adapterTransaksi);
    }

    @Override
    public void onClickTransaksi(int position) {
        listTransaksi.get(position);
        Intent intent = new Intent(this, TambahTransaksiActivity.class);
        startActivity(intent);

    }
}
