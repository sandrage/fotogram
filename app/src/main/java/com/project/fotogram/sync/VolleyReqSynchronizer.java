package com.project.fotogram.sync;

import android.util.Log;

public class VolleyReqSynchronizer {
    private static VolleyReqSynchronizer instance;
    private int requestToBeSyncNumber;
    private boolean finished = false;

    private VolleyReqSynchronizer() {

    }

    public static synchronized VolleyReqSynchronizer getInstance() {
        if (instance == null) {
            instance = new VolleyReqSynchronizer();
        }
        return instance;
    }

    public synchronized void setSyncNumber(int syncNumber) {
        this.requestToBeSyncNumber = syncNumber;
    }

    public synchronized void acquire() {
        while (!finished) {
            try {
                Log.d("fotogramLogs", "Io " + Thread.currentThread().getId() + " vado a nanna");
                instance.wait();
                Log.d("fotogramLogs", "Io " + Thread.currentThread().getId() + " mi sveglio");
            } catch (Exception e) {
                Log.d("fotogramLogs", "Io " + Thread.currentThread().getId() + " mi interrompo");
            }
        }
    }

    public synchronized void release() {
        this.requestToBeSyncNumber -= 1;
        Log.d("fotogramLogs", "Ho terminato una volley request: " + this.requestToBeSyncNumber);
        if (this.requestToBeSyncNumber <= 0) {
            Log.d("fotogramLogs", "Ora risveglio il main!");
            this.finished = true;
            instance.notify();
        }
    }
}
