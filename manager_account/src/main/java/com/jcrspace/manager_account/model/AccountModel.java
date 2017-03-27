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
}
