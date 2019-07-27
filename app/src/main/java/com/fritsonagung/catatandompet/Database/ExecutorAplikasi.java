package com.fritsonagung.catatandompet.Database;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;

import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/**
 Developed By:
 Nama : Fritson Agung Julians Ayomi
 NIM  : 10116076
 Kelas: AKB-2
 Tanggal Pengerjaan : 7 Juli 2019
 **/

public class ExecutorAplikasi {

    // For Singleton instantiation
    private static final Object LOCK = new Object();
    private static  ExecutorAplikasi sInstance;
    private final Executor diskIO;
    private final Executor mainThread;
    private final Executor networkIO;

    private ExecutorAplikasi(Executor diskIO, Executor networkIO, Executor mainThread) {
        this.diskIO = diskIO;
        this.networkIO = networkIO;
        this.mainThread = mainThread;
    }

    public static ExecutorAplikasi getInstance() {
        if (sInstance == null) {
            synchronized (LOCK) {
                sInstance = new ExecutorAplikasi(Executors.newSingleThreadExecutor(),
                        Executors.newFixedThreadPool(3),
                        new MainThreadExecutor());
            }
        }
        return sInstance;
    }

    public Executor diskIO() {
        return diskIO;
    }

    public Executor mainThread() {
        return mainThread;
    }

    public Executor networkIO() {
        return networkIO;
    }

    private static class MainThreadExecutor implements Executor {
        private Handler mainThreadHandler = new Handler(Looper.getMainLooper());

        @Override
        public void execute(@NonNull Runnable command) {
            mainThreadHandler.post(command);
        }
    }

}
