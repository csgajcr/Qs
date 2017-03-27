package com.jcrspace.manager_account.model;

import com.jcrspace.common.model.BaseModel;

/**
 * Created by jiangchaoren on 2017/3/27.
 */

public class AccountModel extends BaseModel {
    public String name;
    public String password;
    public long register_time;
    public long last_login_time;
    public int status;
    public String device_token;
    public String sex;
    public String phone;
    public String nick_name;

    public AccountModel() {
    }

    public AccountModel(String name, String password, long register_time, long last_login_time, int status, String device_token, String sex, String phone, String nick_name) {
        this.name = name;
        this.password = password;
        this.register_time = register_time;
        this.last_login_time = last_login_time;
        this.status = status;
        this.device_token = device_token;
        this.sex = sex;
        this.phone = phone;
        this.nick_name = nick_name;
    }
}
