package com.project.fotogram.utility;

import android.Manifest;
import android.app.Activity;
import android.graphics.Bitmap;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.android.volley.VolleyError;

import java.io.ByteArrayOutputStream;
import java.net.HttpURLConnection;

public class UtilityMethods {

    public static void manageCommunicationError(VolleyError error) {
        Log.d("fotogramLogs", "Error occured: " + error.getMessage());
        Integer statusCode = error.networkResponse != null ? error.networkResponse.statusCode : null;
        if (statusCode != null) {
            switch (statusCode) {
                case HttpURLConnection.HTTP_BAD_REQUEST:
                    Log.d("fotogramLogs", "parameter invalid");
                    break;
                case HttpURLConnection.HTTP_NOT_FOUND:
                    Log.d("fotogramLogs", "token session invalid");
                    break;
                default:
                    Log.d("fotogramLogs", "unexpected error: " + statusCode);
                    break;
            }
        } else {
            Log.d("fotogramLogs", "Status code null!");
        }

    }

    public static void checkPermissions(Activity activity, int requestCode) {

        if (ActivityCompat.shouldShowRequestPermissionRationale(activity, Manifest.permission.READ_EXTERNAL_STORAGE)) {
            //TODO SHOW AN EXPLANATION
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);

        } else {
            ActivityCompat.requestPermissions(activity,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    requestCode);
        }

    }

    public static byte[] resizePhoto(int maxFileSize, Bitmap image) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        float scaleFactor = 0.95f;
        if (byteArray.size() > maxFileSize) {
            while (byteArray.size() > maxFileSize) {
                image = Bitmap.createScaledBitmap(image, (int) (image.getWidth() * scaleFactor), (int) (image.getHeight() * scaleFactor), false);
                byteArray.reset();
                image.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
            }
        }
        return byteArray.toByteArray();
    }

    public static byte[] resizePhoto(Bitmap image) {
        ByteArrayOutputStream byteArray = new ByteArrayOutputStream();
        image = Bitmap.createScaledBitmap(image, 50, 50, false);
        byteArray.reset();
        image.compress(Bitmap.CompressFormat.JPEG, 100, byteArray);
        return byteArray.toByteArray();
    }
}
