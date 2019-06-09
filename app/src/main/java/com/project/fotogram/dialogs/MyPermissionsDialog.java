package com.project.fotogram.dialogs;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.DialogFragment;

import com.project.fotogram.R;

public class MyPermissionsDialog extends DialogFragment {
    private String msg;
    private Activity activity;
    private int requestCode;

    public void configurePermissionsDialog(String msg, Activity activity, int requestCode) {
        this.msg = msg;
        this.activity = activity;
        this.requestCode = requestCode;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
        if (msg == null) {
            alert.setMessage(R.string.alert_unexp_error);
        } else {
            alert.setMessage(msg);
        }
        alert.setPositiveButton(R.string.alert_ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        requestCode);
            }
        });
        return alert.create();
    }
}
