package com.jcrspace.common.dialog;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;

import com.jcrspace.common.R;

/**
 * Created by jiangchaoren on 2017/5/23.
 */

public class ConfirmDialog {
    private AlertDialog dialog;
    private Context context;
    private DialogInterface.OnClickListener onPositiveClickListener;
    private DialogInterface.OnClickListener onNegativeClickListener;

    public ConfirmDialog(Context context,String message) {
        this.context = context;
        dialog = new AlertDialog.Builder(context).setMessage(message).setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onPositiveClickListener!=null){
                    onPositiveClickListener.onClick(dialog,which);
                }
            }
        }).setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (onNegativeClickListener!=null){
                    onNegativeClickListener.onClick(dialog,which);
                }
            }
        }).setTitle(R.string.tip).create();
    }

    public void setOnPositiveClickListener(DialogInterface.OnClickListener onPositiveClickListener) {
        this.onPositiveClickListener = onPositiveClickListener;
    }

    public void setOnNegativeClickListener(DialogInterface.OnClickListener onNegativeClickListener) {
        this.onNegativeClickListener = onNegativeClickListener;
    }

    public void setCancelable(boolean flag){
        dialog.setCancelable(flag);
    }

    public void show(){
        dialog.show();
        dialog.getButton(DialogInterface.BUTTON_POSITIVE).setTextColor(context.getResources().getColor(R.color.safeRed));
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
