package com.jcrspace.manager_account.event;

import com.jcrspace.manager_account.model.AccountDO;

/**
 * Created by Jcr on 2017/5/29 0029.
 */

public class ChangeNicknameEvent {

    public AccountDO accountDO;

    public ChangeNicknameEvent(AccountDO accountDO) {
        this.accountDO = accountDO;
    }
}
