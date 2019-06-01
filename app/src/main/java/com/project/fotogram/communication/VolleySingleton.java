package com.project.fotogram.communication;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    private static VolleySingleton instance;
    private RequestQueue requestQueue;

    public synchronized static VolleySingleton getInstance(Context context) {
        if (instance == null) {
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    private VolleySingleton(Context context) {
        this.requestQueue = Volley.newRequestQueue(context.getApplicationContext());
    }


    public synchronized <T> void addToRequestQueue(Request<T> req) {
        this.requestQueue.add(req);
    }
}
