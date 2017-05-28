package com.jcrspace.ui_account.model;

import com.jcrspace.manager_account.model.AccountDO;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class AccountVO {
    public AccountDO accountDO;

    public AccountVO(AccountDO accountDO) {
        this.accountDO = accountDO;
    }

    public int getId(){
        return accountDO.aid;
    }

    public String getMobile(){

        return accountDO.mobile;
    }

    public long getRegisterTime(){
        return accountDO.register_time;
    }

    public String getSex(){
        return accountDO.sex;
    }

    public String getNickName(){
        return accountDO.nick_name;
    }
}
