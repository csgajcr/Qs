package com.jcrspace.manager_bill.event;

/**
 * Created by Jcr on 2017/5/29 0029.
 */

public class SyncCompleteEvent {
    public boolean isSuccess;
    public String errorMessage;

    public SyncCompleteEvent(boolean isSuccess,String errorMessage) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
    }
}
