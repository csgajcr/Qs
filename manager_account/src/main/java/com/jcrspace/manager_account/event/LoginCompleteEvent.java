package com.jcrspace.manager_account.event;

import com.jcrspace.manager_account.model.AccountSO;

/**
 * Created by Jcr on 2017/5/28 0028.
 */

public class LoginCompleteEvent {
    public boolean isSuccess;

    public String errorMessage;

    public AccountSO accountSO;

    public LoginCompleteEvent(boolean isSuccess, String errorMessage, AccountSO accountSO) {
        this.isSuccess = isSuccess;
        this.errorMessage = errorMessage;
        this.accountSO = accountSO;
    }
}
