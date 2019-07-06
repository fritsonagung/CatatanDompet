package com.fritsonagung.catatandompet;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.app.AppCompatDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private boolean sedangMencari = false;
    private AppCompatDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate menu; Menambahkan item ke actionbar jika ada.
        getMenuInflater().inflate(R.menu.menu_utama, menu);

        //Kaitkan konfigurasi yang dapat dicari dengan SearchView
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_cari).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnCloseListener(new SearchView.OnCloseListener()
        {
            @Override
            public boolean onClose()
            {
                sedangMencari = false;

                // Mempopulasikan kembali transaksi
                onResume();

                //false: memungkinkan perilaku pembersihan default pada tampilan pencarian pada penutupan.
                return false;
            }
        });

        searchView.setOnSearchClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                sedangMencari = true;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_tambah) {
            Intent i = new Intent(getApplicationContext(), Transaksi.class);
            final Bundle b = new Bundle();
            startActivity(i);
            return true;
        }

        if (id == R.id.kategori) {
            Intent i = new Intent(getApplicationContext(), Kategori.class);
            final Bundle b = new Bundle();
            startActivity(i);
            return true;
        }
        if (id == R.id.laporan) {

        }
        if (id == R.id.tentang) {
            showDialogInput();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    public void showDialogInput() {
        dialog = new AppCompatDialog(this);
        dialog.setContentView(R.layout.activity_tentang);

        if (!dialog.isShowing()) {
            dialog.show();
        }
    }
}
