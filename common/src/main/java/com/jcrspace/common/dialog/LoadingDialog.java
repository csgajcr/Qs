package com.jcrspace.common.dialog;


import android.content.Context;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;

import com.jcrspace.common.R;

/**
 * Created by Jcr on 2017/5/22 0022.
 */

public class LoadingDialog {
    private AlertDialog dialog;
    private Context context;

    public LoadingDialog(Context context) {
        this.context = context;
        dialog = new AlertDialog.Builder(context).create();
        View view = LayoutInflater.from(context).inflate(R.layout.dialog_loading,null,false);
        dialog.setView(view);

    }

    public void show(){
        dialog.show();
//        dialog.getWindow().setBackgroundDrawableResource(R.drawable.bg_loading_dialog);
        dialog.getWindow().getDecorView().setBackgroundColor(context.getResources().getColor(R.color.transparent));

    }

    public void dismiss(){
        dialog.dismiss();
    }
}
