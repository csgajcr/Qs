package com.jcrspace.manager_account.model;

import cn.bmob.v3.BmobObject;

/**
 * Created by jiangchaoren on 2017/3/28.
 */

public class AccountSO extends BmobObject {
    public Integer id;
    public String mobile;
    public String password;
    public long register_time;
    public long last_login_time;
    public int status;
    public String device_token;
    public String sex;
    public String nick_name;

    public AccountSO() {
        this.setTableName("user");
    }
}
