package com.fritsonagung.catatandompet.View;

import android.content.Intent;
import android.os.Bundle;

import com.cuneytayyildiz.onboarder.OnboarderActivity;
import com.cuneytayyildiz.onboarder.OnboarderPage;
import com.cuneytayyildiz.onboarder.utils.OnboarderPageChangeListener;
import com.fritsonagung.catatandompet.R;
import com.fritsonagung.catatandompet.Util.SharedPreferences;

import java.util.Arrays;
import java.util.List;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 6 Juli 2019
 **/

public class IntroActivity extends OnboarderActivity implements OnboarderPageChangeListener {

    private SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Cek lauch activity sebelum memanggil setContentView()
        sharedPreferences = new SharedPreferences(this);
        if (!sharedPreferences.isFirstTimeLaunch()) {

            sharedPreferences.setFirstTimeLaunch(false);
            startActivity(new Intent(IntroActivity.this, MainActivity.class));
            finish();
        }

        List<OnboarderPage> pages = Arrays.asList(
                new OnboarderPage.Builder()
                        .title("Pemasukan")
                        .description("Catat Pemasukan Harian Anda Untuk Pengelolaan Keuangan Bulanan Yang Lebih Baik")
                        .imageResourceId(R.drawable.income)
                        .backgroundColor(R.color.colorLightBlue)
                        .titleColor(R.color.white)
                        .descriptionColor(R.color.white)
                        .multilineDescriptionCentered(true)
                        .build(),

                new OnboarderPage.Builder()
                        .title("Pengeluaran")
                        .description("Catat Pengeluaran Harian Anda Untuk Pengelolaan Keuangan Bulanan Yang Lebih Baik")
                        .imageResourceId(R.drawable.expenses)
                        .backgroundColor(R.color.colorLightBlue)
                        .titleColor(R.color.white)
                        .descriptionColor(R.color.white)
                        .multilineDescriptionCentered(true)
                        .build(),

                new OnboarderPage.Builder()
                        .title("Laporan")
                        .description("Lihat Laporan Keuangan Bulanan, Ketahui Pemasukan dan Pengeluaran Perbulan Anda")
                        .imageResourceId(R.drawable.report)
                        .backgroundColor(R.color.colorLightBlue)
                        .titleColor(R.color.white)
                        .descriptionColor(R.color.white)
                        .multilineDescriptionCentered(true)
                        .build()
        );

        setActiveIndicatorColor(android.R.color.white);
        setOnboarderPageChangeListener(this);
        initOnboardingPages(pages);
    }


    @Override
    public void onFinishButtonPressed() {
        // mengecek lauch activity - sebelum memanggil setContentView()
        sharedPreferences = new SharedPreferences(this);

        sharedPreferences.setFirstTimeLaunch(false);
        startActivity(new Intent(IntroActivity.this, MainActivity.class));
        finish();
    }

    @Override
    protected void onSkipButtonPressed() {

        super.onSkipButtonPressed();
    }

    @Override
    public void onPageChanged(int position) {

    }

}
