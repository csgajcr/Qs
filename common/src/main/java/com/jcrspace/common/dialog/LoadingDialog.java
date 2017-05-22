package com.jcrspace.common.dialog;

import android.content.Context;
import android.support.v7.app.AlertDialog;

import com.jcrspace.common.R;

/**
 * Created by Jcr on 2017/5/22 0022.
 */

public class LoadingDialog {
    private AlertDialog dialog;
    private Context context;

    public LoadingDialog(Context context) {
        this.context = context;
        dialog = new AlertDialog.Builder(context).setView(R.layout.dialog_loading).create();
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
