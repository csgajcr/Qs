package com.jcrspace.common.dialog;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v7.app.AlertDialog;

import com.jcrspace.common.R;

/**
 * Created by Jcr on 2017/5/29 0029.
 */

public class TipDialog {
    private AlertDialog dialog;
    private Context context;
    private DialogInterface.OnClickListener onClickListener;

    public TipDialog(Context context,String tip) {
        this.context = context;
        dialog = new AlertDialog.Builder(context).setMessage(tip).setPositiveButton(R.string.confirm,null)
                .setPositiveButton(R.string.confirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (onClickListener!=null){
                            onClickListener.onClick(dialog,which);
                        }
                    }
                }).create();

    }

    public void setOnClickListener(DialogInterface.OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public void setCancelable(boolean flag){
        dialog.setCancelable(flag);
    }

    public void show(){
        dialog.show();
    }

    public void dismiss(){
        dialog.dismiss();
    }
}
