package com.jcrspace.manager_bill.listener;

/**
 * Created by Jcr on 2017/5/31 0031.
 */

public interface SyncCompleteListener {
    void onSuccess();

    void onError(Exception e);
}
