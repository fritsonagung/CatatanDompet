package com.fritsonagung.catatandompet;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

/*
 Nama : Fritson Agung Julians Ayomi
         NIM  : 10116076
         Kelas: AKB-2
         Tanggal Pengerjaan : 22 Juni 2019
*/

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_TIME_OUT = 3000;
    private ImageView logo;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logoSplashScreen);
        title = findViewById(R.id.titleSplashScreen);

        Animation splash_animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        logo.startAnimation(splash_animation);
        title.startAnimation(splash_animation);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        }, SPLASH_TIME_OUT);
    }
}
