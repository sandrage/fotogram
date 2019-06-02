package com.project.fotogram.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;

import com.project.fotogram.R;

public class MyDialog extends DialogFragment {
    private String msg;

    public void setMsg(String msg) {
        this.msg = msg;
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

            }
        });
        return alert.create();
    }
}
