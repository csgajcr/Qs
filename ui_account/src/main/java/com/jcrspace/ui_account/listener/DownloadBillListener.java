package com.jcrspace.ui_account.listener;

/**
 * Created by jiangchaoren on 2017/5/31.
 */

public interface DownloadBillListener {
    void onSuccess();

    void onError(Exception e);
}
