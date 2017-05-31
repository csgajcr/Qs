package com.jcrspace.manager_account.event;

/**
 * Created by Jcr on 2017/5/28 0028.
 */

public class RegisterCompleteEvent {
    public boolean isSuccess;

    public String errorMessage;

    public String userName;

    public RegisterCompleteEvent(boolean isSuccess, String errorMessage, String userName) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.userName = userName;
    }
}
