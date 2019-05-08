package com.project.fotogram.utility;

import android.util.Log;

import com.android.volley.VolleyError;

import java.net.HttpURLConnection;

public class UtilityMethods {

    public static void manageCommunicationError(VolleyError error){
        Log.d("fotogramLogs","Error occured: "+error.getMessage());
        int statusCode = error.networkResponse.statusCode;
        switch(statusCode){
            case HttpURLConnection.HTTP_BAD_REQUEST:
                Log.d("fotogramLogs", "parameter invalid");
                break;
            case HttpURLConnection.HTTP_NOT_FOUND:
                Log.d("fotogramLogs","token session invalid");
                break;
            default:
                Log.d("fotogramLogs", "unexpected error: "+statusCode);
                break;
        }
    }
}
