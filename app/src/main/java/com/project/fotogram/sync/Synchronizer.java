package com.project.fotogram.sync;

import android.util.Log;

public class Synchronizer {
    private static Synchronizer sync;
    private boolean finished = false;

    public static synchronized Synchronizer getInstance() {
        if (sync == null) {
            sync = new Synchronizer();
        }
        return sync;
    }

    public synchronized void acquire() {
        while (!finished) {
            try {
                Log.d("fotogramLogs", "acquire profile wait!");
                this.wait();
                Log.d("fotogramLogs", "acquire profile after wait!");
            } catch (Exception e) {

            }
        }
        this.finished = false;
    }

    public synchronized void release() {
        Log.d("fotogramLogs", "finito caricamento profilo! sveglio il thread!");
        this.finished = true;
        this.notify();
    }
}
