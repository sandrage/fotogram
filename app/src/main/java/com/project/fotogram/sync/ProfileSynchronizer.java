package com.project.fotogram.sync;

import android.util.Log;

public class ProfileSynchronizer {
    private static ProfileSynchronizer sync;
    private boolean finished = false;

    public static synchronized ProfileSynchronizer getInstance() {
        if (sync == null) {
            sync = new ProfileSynchronizer();
        }
        return sync;
    }

    public synchronized void acquire() {
        while (!finished) {
            try {
                Log.d("fotogramLogs", "acquire profile nanna!");
                this.wait();
                Log.d("fotogramLogs", "acquire profile sveglio!");
            } catch (Exception e) {

            }
        }
        this.finished = false;
    }

    public synchronized void release() {
        Log.d("fotogramLogs", "finito caricamento profilo! ti sveglio!");
        this.finished = true;
        this.notify();
    }
}
