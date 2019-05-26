package com.project.fotogram.utility;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.android.volley.VolleyError;
import com.project.fotogram.model.SessionInfo;

import java.net.HttpURLConnection;

public class UtilityMethods {

    public static void manageCommunicationError(VolleyError error){
        Log.d("fotogramLogs","Error occured: "+error.getMessage());
        Integer statusCode = error.networkResponse!=null?error.networkResponse.statusCode:null;
        if(statusCode!=null){
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
        } else{
            Log.d("fotogramLogs","Status code null!");
        }

    }

    public static void checkPermissions(Activity activity, int requestCode){

            if(ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)){
                //TODO SHOW AN EXPLANATION
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);

            } else{
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
            }

    }
}
