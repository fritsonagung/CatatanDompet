package com.fritsonagung.catatandompet.Util;

import android.annotation.SuppressLint;
import android.content.Context;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 6 Juli 2019
 **/

public class SharedPreferences {

    Context context;
    android.content.SharedPreferences pref;
    android.content.SharedPreferences.Editor prefEditor;

    private static final String SHAREPREF = "walkthrough";
    private static final String FIRSTLAUNCH = "firstLaunch";

    @SuppressLint("CommitPrefEdits")
    public SharedPreferences(Context context){
        this.context   = context;
        int privateMode = 0;
        pref            = context.getSharedPreferences(SHAREPREF, privateMode);
        prefEditor          = pref.edit();
    }

    public void setFirstTimeLaunch(boolean isFisTime){
        prefEditor.putBoolean(FIRSTLAUNCH, isFisTime);
        prefEditor.commit();
    }

    public boolean isFirstTimeLaunch() {
        return pref.getBoolean(FIRSTLAUNCH, true);
    }

}
