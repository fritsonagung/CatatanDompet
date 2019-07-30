package com.fritsonagung.catatandompet.View;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.fritsonagung.catatandompet.R;

import java.util.Timer;
import java.util.TimerTask;

/**
 Developed By:
    Nama : Fritson Agung Julians Ayomi
    NIM  : 10116076
    Kelas: AKB-2
    Tanggal Pengerjaan : 22 Juni 2019
**/

public class SplashScreen extends AppCompatActivity {

    //Create variable
    private static int SPLASH_TIME_OUT = 0;
    private ImageView logo;
    private TextView title;
    private TextView subtitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        logo = findViewById(R.id.logoSplashScreen);
        title = findViewById(R.id.titleSplashScreen);
        subtitle = findViewById(R.id.subtitleSplashScreen);

        Animation splash_animation = AnimationUtils.loadAnimation(this, R.anim.splash_anim);
        logo.startAnimation(splash_animation);
        title.startAnimation(splash_animation);
        subtitle.startAnimation(splash_animation);

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                startActivity(new Intent(SplashScreen.this, IntroActivity.class));
                finish();
            }
        }, 0);
    }
}
